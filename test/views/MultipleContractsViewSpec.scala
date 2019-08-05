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

import assets.messages.MultipleContractsMessages
import config.SessionKeys
import play.api.data.Form
import controllers.routes
import forms.MultipleContractsFormProvider
import views.behaviours.YesNoViewBehaviours
import models.NormalMode
import models.UserType.{Hirer, Worker}
import play.api.libs.json.Json
import play.api.mvc.Request
import views.html.MultipleContractsView

class MultipleContractsViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "multipleContracts"

  val form = new MultipleContractsFormProvider()()

  val view = injector.instanceOf[MultipleContractsView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "MultipleContractsView" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.MultipleContractsController.onSubmit(NormalMode).url)

    lazy val request = workerFakeRequest
    lazy val document = asDocument(createViewWithRequest(request))

    "have the correct title" in {
      document.title mustBe title(MultipleContractsMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe MultipleContractsMessages.heading
    }

    "have the correct radio option messages" in {
      document.select(Selectors.multichoice(1)).text mustBe MultipleContractsMessages.yes
      document.select(Selectors.multichoice(2)).text mustBe MultipleContractsMessages.no
    }
  }
}
