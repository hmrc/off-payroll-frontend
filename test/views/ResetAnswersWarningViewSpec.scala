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

import assets.messages.ResetAnswersMessages
import config.featureSwitch.OptimisedFlow
import forms.ResetAnswersWarningFormProvider
import views.behaviours.ViewBehaviours
import views.html.ResetAnswersWarningView

class ResetAnswersWarningViewSpec extends ViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors {
    override val h2 = (i: Int) => s"h2:nth-of-type($i)"
    val accordionHeader = (i: Int) => s".accordion:nth-of-type($i) > .accordion__row a"
    val sectionQuestion = (i: Int, x: Int) => s".accordion:nth-of-type($i) > .accordion__body > dl:nth-of-type($x) dt.cya-question"
    val sectionSingleAnswer = (i: Int, x: Int) => s".accordion:nth-of-type($i) > .accordion__body > dl:nth-of-type($x) dd.cya-answer"
    val sectionMultiQuestion = (i: Int, x: Int) => s".accordion:nth-of-type($i) > .accordion__body > dl:nth-of-type(${x+1}) dt.cya-question"
    val sectionMultiAnswer = (i: Int, x: Int) => s".accordion:nth-of-type($i) > .accordion__body > dl:nth-of-type(${x+1}) dd.cya-answer"
  }

  val messageKeyPrefix = "resetAnswersWarning"

  val view = injector.instanceOf[ResetAnswersWarningView]

  val form = new ResetAnswersWarningFormProvider()

  def createView = () => view(form())(fakeRequest, messages, frontendAppConfig)

  lazy val document = asDocument(createView())

  "CheckYourAnswers view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    "have the correct title" in {

      document.title mustBe title(ResetAnswersMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe ResetAnswersMessages.heading
    }

    "have the correct hint" in {
      document.select(Selectors.hint(1)).text mustBe ResetAnswersMessages.hint
    }

  }
}
