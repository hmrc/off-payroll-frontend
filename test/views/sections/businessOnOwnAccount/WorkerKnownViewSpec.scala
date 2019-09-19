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

package views.sections.businessOnOwnAccount

import assets.messages.WorkerKnownMessages
import controllers.sections.businessOnOwnAccount.routes
import forms.WorkerKnownFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.businessOnOwnAccount.WorkerKnownView

class WorkerKnownViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "hirer.workerKnown"

  val form = new WorkerKnownFormProvider()()

  val view = injector.instanceOf[WorkerKnownView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "WorkerKnownView" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.WorkerKnownController.onSubmit(NormalMode).url)

    "the UserType is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(WorkerKnownMessages.Hirer.title, Some(WorkerKnownMessages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe WorkerKnownMessages.Hirer.heading
      }

      "have the correct content" in {
        document.select(Selectors.p(1)).text mustBe WorkerKnownMessages.Hirer.p1
        document.select(Selectors.p(2)).text mustBe WorkerKnownMessages.Hirer.p2
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe WorkerKnownMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe WorkerKnownMessages.no
      }
    }
  }
}
