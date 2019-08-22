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

import assets.messages.TransferOfRightsMessages
import controllers.sections.businessOnOwnAccount.routes
import forms.TransferOfRightsFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.businessOnOwnAccount.TransferOfRightsView

class TransferOfRightsViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.transferOfRights"

  val form = new TransferOfRightsFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[TransferOfRightsView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "TransferOfRightsView" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.TransferOfRightsController.onSubmit(NormalMode).url)

    "UserType is Worker" must {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(TransferOfRightsMessages.Worker.title, Some(TransferOfRightsMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe TransferOfRightsMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe TransferOfRightsMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe TransferOfRightsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe TransferOfRightsMessages.no
      }
    }

    "UserType is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(TransferOfRightsMessages.Hirer.title, Some(TransferOfRightsMessages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe TransferOfRightsMessages.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe TransferOfRightsMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe TransferOfRightsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe TransferOfRightsMessages.no
      }
    }
  }
}
