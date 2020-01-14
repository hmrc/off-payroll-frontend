/*
 * Copyright 2020 HM Revenue & Customs
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

package controllers.actions

import base.GuiceAppSpecBase
import connectors.mocks.MockDataCacheConnector
import models.UserAnswers
import models.requests.DataRequest
import models.sections.setup.WhoAreYou
import org.scalatest.concurrent.ScalaFutures
import pages.sections.setup.WhoAreYouPage
import play.api.mvc.Result
import play.api.mvc.Results.Redirect

import scala.concurrent.Future

class UserTypeRequiredActionSpec extends GuiceAppSpecBase with MockDataCacheConnector with ScalaFutures {

  object Harness extends UserTypeRequiredActionImpl(messagesControllerComponents) {
    def callRefine[A](request: DataRequest[A]): Future[Either[Result, DataRequest[A]]] = refine(request)
  }

  "User type required Action" when {

    "there is no user type" must {

      "return a Left redirect to the the something went wrong page" in {

        val futureResult = Harness.callRefine(fakeDataRequest)
        val expectedResult = Left(Redirect(controllers.routes.StartAgainController.somethingWentWrong()))

        whenReady(futureResult) { actualResult =>
          actualResult mustBe expectedResult
        }
      }
    }

    "a user type is given" must {

      "return a Right with the given DataRequest" in {

        val dataRequest = workerFakeDataRequestWithAnswers(
          UserAnswers("id").set(WhoAreYouPage, WhoAreYou.Worker)
        )

        val futureResult = Harness.callRefine(dataRequest)

        val expectedResult = Right(dataRequest)

        whenReady(futureResult) { actualResult =>
          actualResult mustBe expectedResult
        }
      }
    }
  }
}
