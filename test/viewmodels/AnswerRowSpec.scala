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

import base.SpecBase
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks._
import play.twirl.api.Html


class AnswerRowSpec extends SpecBase {

  "AnswerRow" should {

    "when given a list of answers" should {

      s"return a MultiAnswerRow with correctly formatted HTML" in {

        val gen = for {
          answers <- arbitrary[Seq[String]]
          isMessageKey <- arbitrary[Boolean]
        } yield (answers, isMessageKey)

        forAll(gen) { case (answers, isMessageKey) =>

          lazy val result = AnswerRow("label", answers, isMessageKey, "/change")

          result mustBe MultiAnswerRow("label", answers, isMessageKey, "/change")
          result.answerHtml mustBe Html(s"<ul>${answers.foldLeft("")((o,x) => o + s"<li>$x</li>")}</ul>")
        }
      }
    }

    "when given a single answer" should {

      "return a SingleAnswerRow and correct HTML" in {

        val gen = for {
          answer <- arbitrary[String]
          isMessageKey <- arbitrary[Boolean]
        } yield (answer, isMessageKey)

        forAll(gen) { case (answer, isMessageKey) =>

          lazy val result = AnswerRow("label", answer, isMessageKey, "/change")

          result mustBe SingleAnswerRow("label", answer, isMessageKey, "/change")
          result.answerHtml mustBe Html(answer)
        }
      }
    }
  }
}
