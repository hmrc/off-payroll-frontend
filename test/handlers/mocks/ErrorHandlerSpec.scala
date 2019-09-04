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

package handlers.mocks

import controllers.ControllerSpecBase
import handlers.ErrorHandler
import play.api.http.Status
import play.api.test.Helpers._
import views.html.errors.FourOhFourView
import views.html.templates.ErrorTemplate

class ErrorHandlerSpec extends ControllerSpecBase {


  object TestErrorHandler extends ErrorHandler(frontendAppConfig, messagesApi, app.injector.instanceOf[ErrorTemplate], app.injector.instanceOf[FourOhFourView])

  "ErrorHandler" must {

    "when calling .onClientError" when {

      "Error Status is 404" should {

        "Render the Page Not Found error" in {

          val result = TestErrorHandler.onClientError(fakeRequest, Status.NOT_FOUND, "Not Found")

          status(result) mustBe Status.NOT_FOUND
          assert(await(bodyOf(result)).contains(messages("common.standardPageNotFoundErrorMessageHeader")))
        }
      }

      "Error Status is 400" should {

        "Render the Bad Request error" in {

          val result = TestErrorHandler.onClientError(fakeRequest, Status.BAD_REQUEST, "Bad Req")

          status(result) mustBe Status.BAD_REQUEST
          assert(await(bodyOf(result)).contains(messages("common.standardErrorMessageHeader")))
        }
      }

      "Error Status is anything else" should {

        "Render the ISE error" in {

          val result = TestErrorHandler.onClientError(fakeRequest, Status.FORBIDDEN, "Forbidden")

          status(result) mustBe Status.INTERNAL_SERVER_ERROR
          assert(await(bodyOf(result)).contains(messages("common.standardErrorMessageHeader")))
        }
      }
    }
  }
}
