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
import scala.io.Source

class MessagesSpec extends SpecBase {

  val englishFileName = "conf/messages.en"
  val welshFileName = "conf/messages.cy"

  val expectedWelshFileName = "test/resources/welshMessages/messages.cy"

  val sanitize: Iterator[String] => List[String] = _.filterNot(_.isEmpty).filterNot(_.contains("#")).toList
  val getKey: String => String = _.split("=").head.trim

  lazy val expectedWelshMessages = sanitize(Source.fromFile(expectedWelshFileName).getLines)
  lazy val actualWelshMessages = sanitize(Source.fromFile(welshFileName).getLines)

  lazy val englishKeys = sanitize(Source.fromFile(englishFileName).getLines map getKey)
  lazy val welshKeys = actualWelshMessages map getKey

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

    "for the expected welsh messages" should {

      "have the same number of lines between the expected file and the actual file" in {
        expectedWelshMessages.length mustBe actualWelshMessages.length
      }

      expectedWelshMessages.zip(actualWelshMessages).foreach {
        case (expectedMsg, actualMsg) =>
          s"expected message: '$expectedMsg' must equal actual message: '$actualMsg'" in {
            expectedMsg mustBe actualMsg
          }
      }
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
