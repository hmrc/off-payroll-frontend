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

package controllers

import akka.util.ByteString
import config.SessionKeys
import config.featureSwitch.PrintPDF
import connectors.httpParsers.PDFGeneratorHttpParser
import connectors.httpParsers.PDFGeneratorHttpParser.{BadRequest, SuccessfulPDF}
import controllers.actions._
import forms.AdditionalPdfDetailsFormProvider
import models._
import models.requests.DataRequest
import navigation.mocks.FakeNavigators.FakeCYANavigator
import pages.{CustomisePDFPage, ResultPage, Timestamp}
import play.api.data.Form
import play.api.libs.json.{JsString, Json}
import play.api.test.Helpers._
import play.twirl.api.Html
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.{FakeTimestamp, MockSourceUtil}
import views.html.{AddDetailsView, CustomisePDFView}

class PDFControllerSpec extends ControllerSpecBase {

  override def beforeEach = {
    super.beforeEach()
    enable(PrintPDF)
  }


  val optFormProvider = new AdditionalPdfDetailsFormProvider()
  val optForm = optFormProvider()

  val formProvider = new AdditionalPdfDetailsFormProvider()
  val form = formProvider()

  val customisePdfView = injector.instanceOf[CustomisePDFView]
  val addDetailsView = injector.instanceOf[AddDetailsView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new PDFController(
    mockDataCacheConnector,
    FakeCYANavigator,
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    customisePdfView,
    addDetailsView,
    mockOptimisedDecisionService,
    mockPDFService,
    errorHandler,
    FakeTimestamp,
    mockCompareAnswerService,
    mockCheckYourAnswersService,
    mockEncryptionService,
    MockSourceUtil,
    frontendAppConfig
  )

  def optController(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new PDFController(
    mockDataCacheConnector,
    FakeCYANavigator,
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    optFormProvider,
    controllerComponents = messagesControllerComponents,
    customisePdfView,
    addDetailsView,
    mockOptimisedDecisionService,
    mockPDFService,
    errorHandler,
    FakeTimestamp,
    mockCompareAnswerService,
    mockCheckYourAnswersService,
    mockEncryptionService,
    MockSourceUtil,
    frontendAppConfig
  )

  def viewAsString(form: Form[_] = form) = customisePdfView(frontendAppConfig, form, NormalMode)(fakeRequest, messages).toString
  def optViewAsString(form: Form[_] = form) = addDetailsView(form, NormalMode)(fakeRequest, messages,frontendAppConfig).toString

  val testAnswer = "answer"

  "CustomisePDF Controller" must {

    "download the pdf" in {


      val decisionResponse = DecisionResponse("","",Score(),ResultEnum.OUTSIDE_IR35)
      val request = fakeRequest.withSession(SessionKeys.decisionResponse -> Json.toJson(decisionResponse).toString)

      mockDetermineResultView(decisionResponse)(Right(Html("Html")))

      val validData = Map(
        CustomisePDFPage.toString -> Json.toJson(Answers(AdditionalPdfDetails(Some("answer"), reference = Some("filenames,,,,,,")), 0)),
        Timestamp.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 1))
      )

      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      mockDecryptDetails(AdditionalPdfDetails(Some("answer"), reference = Some("filenames,,,,,,")))

      val response: PDFGeneratorHttpParser.Response = Right(SuccessfulPDF(ByteString("PDF")))

      mockGeneratePdf(response)
      mockCheckYourAnswers(Seq())

      val result = controller(getRelevantData).downloadPDF()(request)

      status(result) mustBe OK

      contentAsString(result) mustBe "PDF"
    }

    "download the pdf and default the filename if it's not ascii" in {


      val decisionResponse = DecisionResponse("","",Score(),ResultEnum.OUTSIDE_IR35)
      val request = fakeRequest.withSession(SessionKeys.decisionResponse -> Json.toJson(decisionResponse).toString)

      mockDetermineResultView(decisionResponse)(Right(Html("Html")))

      val validData = Map(
        CustomisePDFPage.toString -> Json.toJson(Answers(AdditionalPdfDetails(Some("answer"), reference =  Some("€€€€€###¢¢¢€€#¢,,,,,,,,")), 0)),
        Timestamp.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 1))
      )

      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      mockDecryptDetails(AdditionalPdfDetails(Some("answer"), reference = Some("€€€€€###¢¢¢€€#¢,,,,,,,,")))

      val response: PDFGeneratorHttpParser.Response = Right(SuccessfulPDF(ByteString("PDF")))

      mockGeneratePdf(response)
      mockCheckYourAnswers(Seq())

      val result = controller(getRelevantData).downloadPDF()(request)

      status(result) mustBe OK

      contentAsString(result) mustBe "PDF"

    }

    "handle errors from the pdf" in {


      val decisionResponse = DecisionResponse("","",Score(),ResultEnum.OUTSIDE_IR35)
      val request = fakeRequest.withSession(SessionKeys.decisionResponse -> Json.toJson(decisionResponse).toString)

      mockDetermineResultView(decisionResponse)(Left(Html("Html")))

      val validData = Map(
        CustomisePDFPage.toString -> Json.toJson(Answers(AdditionalPdfDetails(Some("answer")), 0)),
        Timestamp.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 1))
      )

      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      mockDecryptDetails(AdditionalPdfDetails(Some("answer")))

      val response: PDFGeneratorHttpParser.Response = Right(SuccessfulPDF(ByteString("PDF")))

      mockCheckYourAnswers(Seq())

      val result = controller(getRelevantData).downloadPDF()(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong().url)
    }

    "download the pdf when no data is entered" in {


      val decisionResponse = DecisionResponse("","",Score(),ResultEnum.OUTSIDE_IR35)
      val request = fakeRequest.withSession(SessionKeys.decisionResponse -> Json.toJson(decisionResponse).toString)

      mockDetermineResultView(decisionResponse)(Right(Html("Html")))

      val validData = Map(
        Timestamp.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 1))
      )

      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val response: PDFGeneratorHttpParser.Response = Right(SuccessfulPDF(ByteString("PDF")))

      mockGeneratePdf(response)
      mockCheckYourAnswers(Seq())

      val result = controller(getRelevantData).downloadPDF()(request)

      status(result) mustBe OK

      contentAsString(result) mustBe "PDF"
    }

    "If the OptimisedFlow is enabled" should {

      "return OK and the correct view for a GET" in {


        val result = controller().onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe OK

        contentAsString(result) mustBe optViewAsString(form.fill(AdditionalPdfDetails()))
      }

      "populate the view correctly on a GET when the question has previously been answered" in {


        val validData = Map(CustomisePDFPage.toString -> Json.toJson(Answers(AdditionalPdfDetails(Some("answer")), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        mockDecryptDetails(AdditionalPdfDetails(Some("answer")))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe optViewAsString(form.fill(AdditionalPdfDetails(Some(testAnswer))))
      }

      "show the PDF view" in {


        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val validData = Map(CustomisePDFPage.toString -> Json.toJson(Answers(AdditionalPdfDetails(Some("answer")), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))
        val response: PDFGeneratorHttpParser.Response = Right(SuccessfulPDF(ByteString("PDF")))

        mockEncryptDetails(AdditionalPdfDetails(Some("answer")))
        val answers = userAnswers.set(CustomisePDFPage,0,AdditionalPdfDetails(Some("answer")))
        mockOptimisedConstructAnswers(DataRequest(postRequest,"id",answers),AdditionalPdfDetails)(answers)

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val result = controller(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe 303
      }

      "show the PDF view with a default timestamp" in {


        val validData = Map(CustomisePDFPage.toString -> Json.toJson(Answers(AdditionalPdfDetails(Some("answer")), 0)))

        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val response: PDFGeneratorHttpParser.Response = Right(SuccessfulPDF(ByteString("PDF")))

        mockEncryptDetails(AdditionalPdfDetails(Some("answer")))

        val answers = userAnswers.set(CustomisePDFPage,0,AdditionalPdfDetails(Some("answer")))
        mockOptimisedConstructAnswers(DataRequest(postRequest,"id",answers),AdditionalPdfDetails)(answers)

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val result = controller().onSubmit(NormalMode)(postRequest)

        status(result) mustBe 303
      }

      "show the PDF view when the feature is disabled" in {

        disable(PrintPDF)

        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val validData = Map(CustomisePDFPage.toString -> Json.toJson(Answers(AdditionalPdfDetails(Some("answer")), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        mockEncryptDetails(AdditionalPdfDetails(Some("answer")))
        val answers = userAnswers.set(CustomisePDFPage,0,AdditionalPdfDetails(Some("answer")))
        mockOptimisedConstructAnswers(DataRequest(postRequest,"id",answers),AdditionalPdfDetails)(answers)
        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val result = controller(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe 303
      }

      "return a Bad Request and errors when invalid data is submitted" in {


        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", "a" * (AdditionalPdfDetailsFormProvider.maxFieldLength + 1)))
        val boundForm = optForm.bind(Map("completedBy" -> "a" * (AdditionalPdfDetailsFormProvider.maxFieldLength + 1)))

        val validData = Map(CustomisePDFPage.toString -> Json.toJson(Answers(AdditionalPdfDetails(Some("answer")), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = optController(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe optViewAsString(boundForm)
      }

      "redirect to Index Controller for a GET if no existing data is found" in {


        val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }

      "redirect to Index Controller for a POST if no existing data is found" in {


        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", testAnswer))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }
  }
}
