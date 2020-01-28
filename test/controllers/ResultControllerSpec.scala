/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import config.SessionKeys
import controllers.actions._
import forms.{DeclarationFormProvider, DownloadPDFCopyFormProvider}
import models._
import models.requests.DataRequest
import navigation.mocks.FakeNavigators.FakeCYANavigator
import pages.Timestamp
import play.api.http.Status
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.twirl.api.Html
import services.mocks.{MockCheckYourAnswersService, MockDecisionService}
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.FakeTimestamp
import utils.SessionUtils._
import viewmodels.AnswerSection

class ResultControllerSpec extends ControllerSpecBase with MockDecisionService with MockCheckYourAnswersService with Enumerable.Implicits {

  val formProvider = new DeclarationFormProvider()
  val form = formProvider()

  val formProviderPDF = new DownloadPDFCopyFormProvider()
  val formPDF = formProvider()

  val postAction = routes.ResultController.onSubmit()

  val answers = Seq(
    AnswerSection(Messages("result.peopleInvolved.h2"), None, Seq(), section = Section.setup),
    AnswerSection(Messages("result.workersDuties.h2"), whyResult = Some(Html(messages("result.officeHolderInsideIR35.whyResult.p1"))), Seq(), section = Section.earlyExit),
    AnswerSection(Messages("result.substitutesHelpers.h2"), whyResult = Some(Html(messages("result.substitutesAndHelpers.summary"))), Seq(), section = Section.personalService),
    AnswerSection(Messages("result.workArrangements.h2"), whyResult = Some(Html(messages("result.workArrangements.summary"))), Seq(), section = Section.control),
    AnswerSection(Messages("result.financialRisk.h2"), whyResult = Some(Html(messages("result.financialRisk.summary"))), Seq(), section = Section.financialRisk),
    AnswerSection(Messages("result.partAndParcel.h2"), whyResult = Some(Html(messages("result.partParcel.summary"))), Seq(), section = Section.partAndParcel)
  )

  val version = frontendAppConfig.decisionVersion

  def controller(requireUserType: FakeUserTypeRequiredAction = FakeUserTypeRequiredSuccessAction) = new ResultController(
    FakeIdentifierAction,
    FakeEmptyCacheMapDataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    requireUserType,
    formProvider,
    formProviderPDF,
    FakeCYANavigator,
    mockDataCacheConnector,
    FakeTimestamp,
    mockCompareAnswerService,
    mockDecisionService,
    mockCheckYourAnswersService,
    errorHandler,
    frontendAppConfig
  )

  "ResultPage Controller" must {

    "If the normal flow is enabled" should {

      "If a success response is returned from the Optimised Decision Service" should {

        "return OK and the HTML returned from the Decision Service" in {

          val validData = Map(Timestamp.toString -> Json.toJson(FakeTimestamp.timestamp()))
          val answers = userAnswers.set(Timestamp, FakeTimestamp.timestamp())
          val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.OUTSIDE_IR35)
          implicit val dataRequest = DataRequest(fakeRequest, "id", answers)

          mockConstructAnswers(dataRequest, FakeTimestamp.timestamp())(answers)
          mockDecide(Right(decisionResponse))
          mockDetermineResultView(decisionResponse)(Right(Html("Success")))
          mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

          val result = controller().onPageLoad(dataRequest)

          status(result) mustBe OK
          contentAsString(result) mustBe "Success"
          session(result).getModel[DecisionResponse](SessionKeys.decisionResponse) mustBe Some(decisionResponse)
        }
      }

      "If an error response is returned from the Optimised Decision Service determine decide method" should {

        "redirect to the something went wrong page" in {

          val validData = Map(Timestamp.toString -> Json.toJson(FakeTimestamp.timestamp()))
          val answers = userAnswers.set(Timestamp, FakeTimestamp.timestamp())
          implicit val dataRequest = DataRequest(fakeRequest, "id", answers)

          mockConstructAnswers(dataRequest, FakeTimestamp.timestamp())(answers)
          mockDecide(Left(ErrorResponse(Status.INTERNAL_SERVER_ERROR, "Err")))
          mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

          val result = controller().onPageLoad(dataRequest)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong().url)
        }
      }

      "If an error response is returned from the Optimised Decision Service determine result view method" should {

        "redirect to the something went wrong page" in {

          val validData = Map(Timestamp.toString -> Json.toJson(FakeTimestamp.timestamp()))
          val answers = userAnswers.set(Timestamp, FakeTimestamp.timestamp())
          val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.OUTSIDE_IR35)
          implicit val dataRequest = DataRequest(fakeRequest, "id", answers)

          mockConstructAnswers(dataRequest, FakeTimestamp.timestamp())(answers)
          mockDecide(Right(decisionResponse))
          mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))
          mockDetermineResultView(decisionResponse)(Left(Html("Error")))

          val result = controller().onPageLoad(dataRequest)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong().url)
        }
      }

      "If no user type is given" should {

        "redirect to the something went wrong page" in {

          val answers = userAnswers.set(Timestamp, FakeTimestamp.timestamp())
          implicit val dataRequest = DataRequest(fakeRequest, "id", answers)

          val result = controller(FakeUserTypeRequiredFailureAction).onPageLoad(dataRequest)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong().url)
        }
      }

      "redirect to next page" in {

        val validData = Map(Timestamp.toString -> Json.toJson(FakeTimestamp.timestamp()))

        val answers = userAnswers.set(Timestamp, FakeTimestamp.timestamp())

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

        mockConstructAnswers(DataRequest(postRequest, "id", answers), FakeTimestamp.timestamp())(answers)
        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val result = controller().onSubmit(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }
  }

}
