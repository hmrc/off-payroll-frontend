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

package views

import assets.messages.CheckYourAnswersMessages
import models.NormalMode
import play.api.mvc.Request
import play.twirl.api.Html
import viewmodels.{AnswerRow, AnswerSection, Section}
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class CheckYourAnswersViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors {
    val h2 = (i: Int) => s"h2:nth-of-type($i)"
    val accordionHeader = (i: Int) => s"a.accordion__button:nth-of-type($i)"
  }

  val messageKeyPrefix = "checkYourAnswers"

  val view = injector.instanceOf[CheckYourAnswersView]

  def createView = () => view(Seq())(fakeRequest, messages, frontendAppConfig)

  def createViewWithData(userAnswers: Seq[Section]) = view(userAnswers)(fakeRequest, messages, frontendAppConfig)

  "CheckYourAnswers view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    lazy val singleAnswerRow = AnswerRow("question", "answer", answerIsMessageKey = true)
    lazy val multiAnswerRow = AnswerRow("question", Seq(singleAnswerRow, singleAnswerRow))

    lazy val cyaSections = Seq(
      AnswerSection(
        headingKey = Some("heading1"),
        whyResult = Some(Html("Additional Content1")),
        rows = Seq(
          singleAnswerRow -> None,
          multiAnswerRow -> None
        )),
      AnswerSection(
        headingKey = Some("heading2"),
        whyResult = Some(Html("Additional Content2")),
        rows = Seq(
          singleAnswerRow -> None,
          multiAnswerRow -> None
        ))
    )

    lazy val document = asDocument(createViewWithData(cyaSections))

    "have the correct title" in {
      document.title mustBe title(CheckYourAnswersMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe CheckYourAnswersMessages.heading
    }

    "have an accordion section" which {

      "has the correct heading" in {

      }
    }

    }

    "have the correct h2" in {
      document.select(Selectors.h2(1)).text.trim mustBe CheckYourAnswersMessages.h2
    }
    "have the correct p1" in {
      document.select(Selectors.p(1)).text mustBe CheckYourAnswersMessages.p1
    }
}
