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
import assets.messages.results.UndeterminedDecisionMessages
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import forms.DeclarationFormProvider
import models.AboutYouAnswer.{Worker, Client}
import models.UserAnswers
import models.requests.DataRequest
import play.api.libs.json.Json
import play.api.mvc.Call
import play.twirl.api.HtmlFormat
import views.ViewSpecBase
import views.html.results.IR35UndeterminedView

class IR35UndeterminedViewSpec extends ViewSpecBase {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors {
    override val subheading = "p.font-large"
  }

  val form = new DeclarationFormProvider()()

  val view = injector.instanceOf[IR35UndeterminedView]

  val postAction = Call(HttpMethods.POST.value, "/")

  def createView(req: DataRequest[_], isPrivateSector: Boolean = false): HtmlFormat.Appendable =
    view(form, postAction, isPrivateSector)(req, messages, frontendAppConfig)

  "The IR35UndeterminedView page" should {

    "If the UserType is Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))
      lazy val document = asDocument(createView(dataRequest))

      "Have the correct title" in {
        document.title mustBe title(UndeterminedDecisionMessages.WorkerIR35.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.WorkerIR35.heading
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.h2(1)).text mustBe UndeterminedDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe UndeterminedDecisionMessages.WorkerIR35.whyResult
      }

      "For a Public Sector contract" should {

        "Have the correct Do Next section which" in {
          document.select(Selectors.h2(2)).text mustBe UndeterminedDecisionMessages.doNextHeading
          document.select(Selectors.p(2)).text mustBe UndeterminedDecisionMessages.WorkerIR35.doNextPublicP1
          document.select(Selectors.p(3)).text() mustBe UndeterminedDecisionMessages.WorkerIR35.doNextPublicP2
        }
      }

      "For a Private Sector contract" should {

        lazy val document = asDocument(createView(dataRequest, isPrivateSector = true))

        "Have the correct Do Next section which" in {
          document.select(Selectors.h2(2)).text mustBe UndeterminedDecisionMessages.doNextHeading
          document.select(Selectors.p(2)).text mustBe UndeterminedDecisionMessages.WorkerIR35.doNextPrivateP1
          document.select(Selectors.p(3)).text() mustBe UndeterminedDecisionMessages.WorkerIR35.doNextPrivateP2
        }
      }

      "Have the correct Download section" in {
        document.select(Selectors.h2(3)).text mustBe UndeterminedDecisionMessages.downloadHeading
        document.select(Selectors.p(4)).text mustBe UndeterminedDecisionMessages.download_p1
      }
    }

    "If the UserType is Client" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Client).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))
      lazy val document = asDocument(createView(dataRequest))

      "Have the correct title" in {
        document.title mustBe title(UndeterminedDecisionMessages.HirerIR35.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.HirerIR35.heading
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.h2(1)).text mustBe UndeterminedDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe UndeterminedDecisionMessages.HirerIR35.whyResult
      }

      "For a Public Sector contract" should {

        "Have the correct Do Next section which" in {
          document.select(Selectors.h2(2)).text mustBe UndeterminedDecisionMessages.doNextHeading
          document.select(Selectors.p(2)).text mustBe UndeterminedDecisionMessages.HirerIR35.doNextPublicP1
          document.select(Selectors.p(3)).text() mustBe UndeterminedDecisionMessages.HirerIR35.doNextPublicP2
        }
      }

      "For a Private Sector contract" should {

        lazy val document = asDocument(createView(dataRequest, isPrivateSector = true))

        "Have the correct Do Next section which" in {
          document.select(Selectors.h2(2)).text mustBe UndeterminedDecisionMessages.doNextHeading
          document.select(Selectors.p(2)).text mustBe UndeterminedDecisionMessages.HirerIR35.doNextPrivateP1
          document.select(Selectors.p(3)).text() mustBe UndeterminedDecisionMessages.HirerIR35.doNextPrivateP2
        }
      }

      "Have the correct Download section" in {
        document.select(Selectors.h2(3)).text mustBe UndeterminedDecisionMessages.downloadHeading
        document.select(Selectors.p(4)).text mustBe UndeterminedDecisionMessages.download_p1
      }
    }
  }
}
