/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import base.SpecBase
import org.scalatest.Assertion
import scala.io.Source

class MessagesSpec extends SpecBase {

  val englishFileName = "conf/messages.en"
  val welshFileName = "conf/messages.cy"

  val expectedWelshFileName = "test/utils/welshMessages/messages.cy"

  val sanitize: Iterator[String] => List[String] = _.filterNot(_.isEmpty).filterNot(_.contains("#")).toList
  val getKey: String => String = _.split("=").head.trim

  lazy val englishKeys = sanitize(Source.fromFile(englishFileName).getLines map getKey)
  lazy val welshKeys = sanitize(Source.fromFile(welshFileName).getLines map getKey)

  lazy val actualWelshMessages = sanitize(Source.fromFile(welshFileName).getLines)
  lazy val expectedWelshMessages = sanitize(Source.fromFile(expectedWelshFileName).getLines)

  "Welsh file" should {

    "not contain duplicate keys" in {
      val differences = welshKeys.diff(welshKeys.distinct)
      assert(differences.isEmpty)
    }

    "for all English language keys" should {
      for (keyValue <- englishKeys) {
        s"contain the key '$keyValue'" in {
          assert(welshKeys.contains(keyValue))
        }
      }
    }

    "contains the correct welsh messages" in {
      def welshMessageList(actualList: List[String],expectedList: List[String]): Assertion ={
        if(actualList.isEmpty && expectedList.isEmpty){
          succeed
        } else {
          assert(actualList.head==expectedList.head)
          welshMessageList(actualList.tail,expectedList.tail)
        }
      }
      welshMessageList(actualWelshMessages,expectedWelshMessages)
    }
  }

  "English file" should {

    "not contain duplicate keys" in {
      val differences = englishKeys.diff(englishKeys.distinct)
      assert(differences.isEmpty)
    }

    "for all Welsh language keys" should {
      for (keyValue <- welshKeys) {
        s"contain the key '$keyValue'" in {
          assert(englishKeys.contains(keyValue))
        }
      }
    }
  }

  "Both files" should {

    "contain the same keys" in {
      assert(englishKeys.diff(welshKeys).isEmpty)
    }
  }


}
