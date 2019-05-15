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
import models.{AdditionalPdfDetails, Timestamp}
import pages.ResultPage
import play.api.mvc.Call
import views.behaviours.ViewBehaviours
import views.html.results.ControlView

class ControlViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "result.control"

  val form = new DeclarationFormProvider()()

  val view = injector.instanceOf[ControlView]

  val postAction = Call(HttpMethods.POST.value, "/")

  val answers = Seq()

  val version = "1.0"

  val timestamp = Timestamp.timestamp

  val model = AdditionalPdfDetails(Some("Gerald"), Some("PBPlumbin"), Some("Plumber"), Some("Boiler man"))

  def createView = () => view(answers, version, form, postAction)(fakeRequest, messages, frontendAppConfig)

  def createPrintView = () => view(answers, version, form, postAction, true, Some(model), Some(timestamp))(fakeRequest, messages, frontendAppConfig)

  "result page" must {

    "to string correctly" in {
      ResultPage.toString mustBe "result"
    }
  }

  "ResultPage view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)
  }

  "ResultPage print view" must {
    behave like printPage(createPrintView, model, timestamp, messageKeyPrefix)
  }
}
