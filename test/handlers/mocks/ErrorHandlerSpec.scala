/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package handlers.mocks

import controllers.ControllerSpecBase
import handlers.ErrorHandler
import play.api.http.Status
import play.api.test.Helpers._
import views.html.errors.NotFoundView
import views.html.templates.ErrorTemplate

class ErrorHandlerSpec extends ControllerSpecBase {


  object TestErrorHandler extends ErrorHandler(frontendAppConfig, messagesApi, app.injector.instanceOf[ErrorTemplate], app.injector.instanceOf[NotFoundView])

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

      "Error Status is 403" should {

        "Render the ISE error" in {

          val result = TestErrorHandler.onClientError(fakeRequest, Status.FORBIDDEN, "Forbidden")

          status(result) mustBe Status.FORBIDDEN
          assert(await(bodyOf(result)).contains(messages("common.standardErrorMessageHeader")))
        }
      }

      "Error Status is 500" should {

        "Render the Internal Server error" in {

          val result = TestErrorHandler.onClientError(fakeRequest, Status.INTERNAL_SERVER_ERROR, "Internal server error")

          status(result) mustBe Status.INTERNAL_SERVER_ERROR
          assert(await(bodyOf(result)).contains(messages("common.standardErrorMessageHeader")))
        }
      }

      "Error Status is anything else" should {

        "Render the ISE error" in {

          val result = TestErrorHandler.onClientError(fakeRequest, Status.UNPROCESSABLE_ENTITY, "Unprocessable Entity")

          status(result) mustBe Status.INTERNAL_SERVER_ERROR
          assert(await(bodyOf(result)).contains(messages("common.standardErrorMessageHeader")))
        }
      }
    }
  }
}
