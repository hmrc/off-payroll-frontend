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

package views.subOptimised.results

import config.SessionKeys
import forms.DeclarationFormProvider
import models.AboutYouAnswer.Worker
import play.api.libs.json.Json
import play.api.mvc.Request
import views.html.subOptimised.results.FinancialRiskView

class FinancialRiskViewSpec extends ResultViewFixture {

  val messageKeyPrefix = "result.financialRisk"

  val form = new DeclarationFormProvider()()

  val view = injector.instanceOf[FinancialRiskView]

  def createView = () => view(answers, version, form, postAction,"worker")(fakeRequest, messages, frontendAppConfig)

  def createPrintView = () => view(answers, version, form, postAction,"worker", true, Some(model), Some(timestamp))(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(answers, version, form, postAction,"worker")(req, messages, frontendAppConfig)

  "ResultPrintPage view" must {
    behave like printPage(createPrintView, model, timestamp, messageKeyPrefix)
  }

  "ResultPage view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)
  }

  "The result page" should {

    lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
    lazy val document = asDocument(createViewWithRequest(request))

    "include the 'Which of these describes you best?' question" in {
      document.toString must include("Which of these describes you best?")
    }

    "include the 'what describes you best' answer'" in {
      document.toString must include("The worker")
    }

    "include the 'contract started' question'" in {
      document.toString must include("Has the worker already started this particular engagement for the end client?")
    }

    "include the 'worker provides' question'" in {
      document.toString must include("What does the worker have to provide for this engagement that they cannot claim as an expense from the end client or an agency?")
    }

    "include the 'worker provides' answer'" in {
      document.toString must include("Vehicle - including purchase, fuel and all running costs (used for work tasks, not commuting)")
    }

    "include the 'office duty' question'" in {
      document.toString must include("Will the worker (or their business) perform office holder duties for the end client as part of this engagement?")
    }

  }
}
