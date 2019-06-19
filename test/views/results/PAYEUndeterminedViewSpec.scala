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

import assets.messages.results.UndeterminedDecisionMessages
import config.SessionKeys
import models.UserType.{Hirer, Worker}
import play.api.libs.json.Json
import play.api.mvc.Request
import views.html.results.undetermined.PAYEUndeterminedView

class PAYEUndeterminedViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[PAYEUndeterminedView]

  def createView(req: Request[_]) = view(postAction)(req, messages, frontendAppConfig)

  "The PAYEUndeterminedView page" should {

    "The UserType is a Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createView(request))

      "Have the correct title" in {
        document.title mustBe title(UndeterminedDecisionMessages.WorkerPAYE.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.WorkerPAYE.heading
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.h2(1)).text mustBe UndeterminedDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.whyResult
      }

      "Have the correct Do Next section" in {
        document.select(Selectors.h2(2)).text mustBe UndeterminedDecisionMessages.doNextHeading
        document.select(Selectors.p(2)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.doNextP1
        document.select(Selectors.p(3)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.doNextP2
      }

      "Have the correct Download section" in {
        document.select(Selectors.h2(3)).text mustBe UndeterminedDecisionMessages.downloadHeading
        document.select(Selectors.p(4)).text mustBe UndeterminedDecisionMessages.download_p1
      }

      "The UserType is a Hirer" should {

        lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
        lazy val document = asDocument(createView(request))

        "Have the correct title" in {
          document.title mustBe title(UndeterminedDecisionMessages.HirerPAYE.title)
        }

        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.HirerPAYE.heading
        }

        "Have the correct Why Result section" in {
          document.select(Selectors.h2(1)).text mustBe UndeterminedDecisionMessages.whyResultHeading
          document.select(Selectors.p(1)).text mustBe UndeterminedDecisionMessages.HirerPAYE.whyResult
        }

        "Have the correct Do Next section" in {
          document.select(Selectors.h2(2)).text mustBe UndeterminedDecisionMessages.doNextHeading
          document.select(Selectors.p(2)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP1
          document.select(Selectors.p(3)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP2
        }

        "Have the correct Download section" in {
          document.select(Selectors.h2(3)).text mustBe UndeterminedDecisionMessages.downloadHeading
          document.select(Selectors.p(4)).text mustBe UndeterminedDecisionMessages.download_p1
        }
      }
    }
  }
}
