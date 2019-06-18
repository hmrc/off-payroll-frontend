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

import assets.messages.results.OutDecisionMessages
import config.SessionKeys
import models.AboutYouAnswer.Worker
import models.UserAnswers
import models.UserType.Hirer
import models.requests.DataRequest
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import views.html.results.PAYEOutView

class PAYEOutsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[PAYEOutView]

  def createView(req: DataRequest[_],
                 isSubstituteToDoWork: Boolean = true,
                 isClientNotControlWork: Boolean = true,
                 isIncurCostNoReclaim: Boolean = true): HtmlFormat.Appendable =
    view(postAction, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim)(req, messages, frontendAppConfig)

  "The PAYEOutsideView page" should {

    "If the UserType is Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))
      lazy val document = asDocument(createView(dataRequest))

      "Have the correct title" in {
        document.title mustBe title(OutDecisionMessages.WorkerPAYE.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe OutDecisionMessages.WorkerPAYE.heading
      }

      "Have the correct Why Result section when all reasons are given" in {
        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP1
        document.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB1
        document.select(Selectors.bullet(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB2
        document.select(Selectors.bullet(3)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB3
        document.select(Selectors.p(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP2
      }

      "Have the correct Why Result section when isSubstituteToDoWork reason is given" in {

        lazy val document = asDocument(createView(
          dataRequest,
          isClientNotControlWork = false,
          isIncurCostNoReclaim = false
        ))

        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP1
        document.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB1
        document.select(Selectors.bullet(2)).isEmpty mustBe true
        document.select(Selectors.bullet(3)).isEmpty mustBe true
        document.select(Selectors.p(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP2
      }

      "Have the correct Why Result section when isClientNotControlWork reason is given" in {

        lazy val document = asDocument(createView(
          dataRequest,
          isSubstituteToDoWork = false,
          isIncurCostNoReclaim = false
        ))

        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP1
        document.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB2
        document.select(Selectors.bullet(2)).isEmpty mustBe true
        document.select(Selectors.bullet(3)).isEmpty mustBe true
        document.select(Selectors.p(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP2
      }

      "Have the correct Why Result section when isIncurCostNoReclaim reason is given" in {

        lazy val document = asDocument(createView(
          dataRequest,
          isSubstituteToDoWork = false,
          isClientNotControlWork = false
        ))

        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP1
        document.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB3
        document.select(Selectors.bullet(2)).isEmpty mustBe true
        document.select(Selectors.bullet(3)).isEmpty mustBe true
        document.select(Selectors.p(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP2
      }

      "Have the correct Do Next section which" in {
        document.select(Selectors.h2(2)).text mustBe OutDecisionMessages.doNextHeading
        document.select(Selectors.p(3)).text mustBe OutDecisionMessages.WorkerPAYE.doNext
      }

      "Have the correct Download section" in {
        document.select(Selectors.h2(3)).text mustBe OutDecisionMessages.downloadHeading
        document.select(Selectors.p(4)).text mustBe OutDecisionMessages.download_p1
      }
    }

    "If the UserType is Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))
      lazy val document = asDocument(createView(dataRequest))

      "Have the correct title" in {
        document.title mustBe title(OutDecisionMessages.HirerPAYE.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe OutDecisionMessages.HirerPAYE.heading
      }

      "Have the correct Why Result section when all reasons are given" in {
        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP1
        document.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB1
        document.select(Selectors.bullet(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB2
        document.select(Selectors.bullet(3)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB3
        document.select(Selectors.p(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP2
      }

      "Have the correct Why Result section when isSubstituteToDoWork reason is given" in {

        lazy val document = asDocument(createView(
          dataRequest,
          isClientNotControlWork = false,
          isIncurCostNoReclaim = false
        ))

        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP1
        document.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB1
        document.select(Selectors.bullet(2)).isEmpty mustBe true
        document.select(Selectors.bullet(3)).isEmpty mustBe true
        document.select(Selectors.p(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP2
      }

      "Have the correct Why Result section when isClientNotControlWork reason is given" in {

        lazy val document = asDocument(createView(
          dataRequest,
          isSubstituteToDoWork = false,
          isIncurCostNoReclaim = false
        ))

        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP1
        document.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB2
        document.select(Selectors.bullet(2)).isEmpty mustBe true
        document.select(Selectors.bullet(3)).isEmpty mustBe true
        document.select(Selectors.p(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP2
      }

      "Have the correct Why Result section when isIncurCostNoReclaim reason is given" in {

        lazy val document = asDocument(createView(
          dataRequest,
          isSubstituteToDoWork = false,
          isClientNotControlWork = false
        ))

        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP1
        document.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB3
        document.select(Selectors.bullet(2)).isEmpty mustBe true
        document.select(Selectors.bullet(3)).isEmpty mustBe true
        document.select(Selectors.p(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP2

      }

      "Have the correct Do Next section which" in {
        document.select(Selectors.h2(2)).text mustBe OutDecisionMessages.doNextHeading
        document.select(Selectors.p(3)).text mustBe OutDecisionMessages.HirerPAYE.doNext
      }

      "Have the correct Download section" in {
        document.select(Selectors.h2(3)).text mustBe OutDecisionMessages.downloadHeading
        document.select(Selectors.p(4)).text mustBe OutDecisionMessages.download_p1
      }
    }
  }
}
