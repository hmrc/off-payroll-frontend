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

package viewmodels

import base.{GuiceAppSpecBase, SpecBase}
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks._
import play.twirl.api.Html


class AnswerRowSpec extends GuiceAppSpecBase with ModelGenerators {

  "AnswerRow" should {

    "when given a list of answers" should {

      s"return a MultiAnswerRow with correctly formatted HTML" in {

        val gen = for {
          answers <- arbitrary[Seq[SingleAnswerRow]]
          url <- arbitrary[Option[String]]
          changeContext <- arbitrary[Option[String]]
        } yield (answers, url, changeContext)

        forAll(gen) { case (answers, url, changeContext) =>

          lazy val result = AnswerRow("label", answers, url, changeContext)

          result mustBe MultiAnswerRow("label", answers, url, changeContext)
          result.answerHtml mustBe Html(s"<ul class='no-bullet-pdf'>${answers.foldLeft("")((o,x) => o + s"<li>${x.answer}</li>")}</ul>")
        }
      }
    }

//    "when given a single answer" should {
//
//      "return a SingleAnswerRow and correct HTML" in {
//
//        val gen = for {
//          answer <- arbitrary[String]
//          isMessageKey <- arbitrary[Boolean]
//          url <- arbitrary[Option[String]]
//          changeContext <- arbitrary[Option[String]]
//        } yield (answer, isMessageKey, url, changeContext)
//
//        forAll(gen) { case (answer, isMessageKey, url, changeContext) =>
//
//          lazy val result = AnswerRow("label", answer, isMessageKey, url, changeContext)
//
//          result mustBe SingleAnswerRow("label", answer, isMessageKey, url, changeContext)
//          result.answerHtml mustBe Html(answer)
//        }
//      }
//    }
  }
}
