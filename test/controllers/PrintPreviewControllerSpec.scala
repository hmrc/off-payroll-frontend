/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import config.SessionKeys
import controllers.actions._
import models._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.twirl.api.Html
import utils.FakeTimestamp
import views.html.FinishedCheckingView

class PrintPreviewControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[FinishedCheckingView]

  def testPrintPreviewController(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new PrintPreviewController(
    identify = FakeIdentifierAction,
    dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    decisionService = mockDecisionService,
    checkYourAnswersService = mockCheckYourAnswersService,
    finishedCheckingView = view,
    encryptionService = mockEncryptionService,
    errorHandler = errorHandler,
    time = FakeTimestamp,
    controllerComponents = messagesControllerComponents,
    appConfig = frontendAppConfig
  )

  "PrintPreviewController" should {

    "When there is a valid decisionResponse held in session" should {

      lazy val decisionResponse = DecisionResponse("","",Score(),ResultEnum.OUTSIDE_IR35)
      lazy val request = fakeRequest.withSession(SessionKeys.decisionResponse -> Json.toJson(decisionResponse).toString)

      "If a success is returned from the Determine Result View service" should {

        "Return OK (200)" in {

          mockDetermineResultView(decisionResponse)(Right(Html("Success")))
          mockCheckYourAnswers(Seq())

          val result = testPrintPreviewController().onPageLoad()(request)

          status(result) mustBe OK
        }
      }

      "If an error is returned from the Determine Result View service" should {

        "Return ISE (500)" in {

          mockDetermineResultView(decisionResponse)(Left(Html("Err")))
          mockCheckYourAnswers(Seq())

          val result = testPrintPreviewController().onPageLoad()(request)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong().url)
        }
      }
    }

    "When there is no decisionResponse held in session" should {

      "return ISE (500)" in {

        val result = testPrintPreviewController().onPageLoad()(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong().url)
      }
    }
  }
}
