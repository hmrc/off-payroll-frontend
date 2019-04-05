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

package views.results

import akka.http.scaladsl.model.HttpMethods
import forms.DeclarationFormProvider
import play.api.mvc.Call
import views.behaviours.ViewBehaviours
import views.html.results.FutureSubstitutionView

class CurrentSubstitutionViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "result.currentSubstitution"

  val form = new DeclarationFormProvider()()

  val view = injector.instanceOf[FutureSubstitutionView]

  val postAction = Call(HttpMethods.POST.value, "/")

  val answers = Seq()

  val version = "1.0"

  def createView = () => view(frontendAppConfig, answers, version, form, postAction)(fakeRequest, messages)

  "ResultPage view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)
  }
}
