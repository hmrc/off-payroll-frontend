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

package views.subOptimised.sections.setup

import controllers.sections.setup.routes
import forms.AboutYouFormProvider
import models.{AboutYouAnswer, NormalMode}
import play.api.data.Form
import views.behaviours.QuestionViewBehaviours
import views.html.subOptimised.sections.setup.AboutYouView

class AboutYouViewSpec extends QuestionViewBehaviours[AboutYouAnswer] {

  val messageKeyPrefix = "aboutYou"

  val form = new AboutYouFormProvider()()

  val view = injector.instanceOf[AboutYouView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  "AboutYou view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView, frontendAppConfig.govUkStartPageUrl)

    behave like pageWithTextFields(createViewUsingForm, messageKeyPrefix, routes.AboutYouController.onSubmit(NormalMode).url)
  }
}
