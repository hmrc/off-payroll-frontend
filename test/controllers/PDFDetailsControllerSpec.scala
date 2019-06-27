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
import config.featureSwitch.{OptimisedFlow, PrintPDF}
import connectors.httpParsers.PDFGeneratorHttpParser
import connectors.httpParsers.PDFGeneratorHttpParser.{BadRequest, SuccessfulPDF}
import controllers.actions._
import forms.CustomisePDFFormProvider
import models.{AdditionalPdfDetails, Answers, NormalMode}
import navigation.FakeNavigator
import pages.{CustomisePDFPage, ResultPage}
import play.api.data.Form
import play.api.libs.json.{JsString, Json}
import play.api.test.Helpers._
import play.twirl.api.Html
import services.DecisionService
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.FakeTimestamp
import views.html.{AddDetailsView, CustomisePDFView}

class PDFDetailsControllerSpec extends ControllerSpecBase {

  override def beforeEach = {
    super.beforeEach()
    enable(PrintPDF)
  }

  val formProvider = new CustomisePDFFormProvider()
  val form = formProvider()

  val customisePdfView = injector.instanceOf[CustomisePDFView]
  val addDetailsView = injector.instanceOf[AddDetailsView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new PDFDetailsController(
    mockDataCacheConnector,
    new FakeNavigator(onwardRoute),
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    customisePdfView,
    addDetailsView,
    injector.instanceOf[DecisionService],
    mockOptimisedDecisionService,
    mockPDFService,
    errorHandler,
    FakeTimestamp,
    mockCompareAnswerService,
    mockCheckYourAnswersService,
    mockEncryptionService,
    frontendAppConfig
  )

  def viewAsString(form: Form[_] = form) = customisePdfView(frontendAppConfig, form, NormalMode)(fakeRequest, messages).toString

  val testAnswer = "answer"

  "CustomisePDF Controller" must {

    "If the OptimisedFlow is enabled" should {

      "return OK and the correct view for a GET" in {
        enable(OptimisedFlow)

        val result = controller().onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe OK

        mockDecrypt("answer")

        contentAsString(result) mustBe viewAsString(form.fill(AdditionalPdfDetails(Some(testAnswer))))
      }

      "populate the view correctly on a GET when the question has previously been answered" in {
        enable(OptimisedFlow)

        val validData = Map(CustomisePDFPage.toString -> Json.toJson(Answers(AdditionalPdfDetails(Some("answer")), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe viewAsString(form.fill(AdditionalPdfDetails(Some(testAnswer))))
      }

      "show the PDF view" in {
        enable(OptimisedFlow)

        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val validData = Map(ResultPage.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val response: PDFGeneratorHttpParser.Response = Right(SuccessfulPDF(ByteString("PDF")))

        mockCheckYourAnswers(Seq())
        mockDetermineResultView()(Right(Html("html")))
        mockGeneratePdf(response)

        val result = controller(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe "PDF"
      }

      "show the PDF view with a default timestamp" in {
        enable(OptimisedFlow)

        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val response: PDFGeneratorHttpParser.Response = Right(SuccessfulPDF(ByteString("PDF")))

        mockCheckYourAnswers(Seq())
        mockDetermineResultView()(Right(Html("html")))
        mockGeneratePdf(response)

        val result = controller().onSubmit(NormalMode)(postRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe "PDF"
      }

      "show the PDF view when the feature is disabled" in {
        enable(OptimisedFlow)
        disable(PrintPDF)

        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val validData = Map(ResultPage.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        mockCheckYourAnswers(Seq())
        mockDetermineResultView()(Right(Html("html")))

        val result = controller(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe OK
      }

      "handle error from PDF service" in {
        enable(OptimisedFlow)

        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val response: PDFGeneratorHttpParser.Response = Left(BadRequest)

        val validData = Map(ResultPage.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        mockCheckYourAnswers(Seq())
        mockDetermineResultView()(Right(Html("html")))
        mockGeneratePdf(response)

        val result = controller(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentAsString(result) must include("Sorry we are experiencing technical problems")
        contentAsString(result) must include("Please try again in few moments")
        contentAsString(result) must not include "What do you want to find out?"
      }

      "handle error from the Optimised Decision Service service" in {
        enable(OptimisedFlow)

        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val validData = Map(ResultPage.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        mockCheckYourAnswers(Seq())
        mockDetermineResultView()(Left(Html("bad_html")))

        val result = controller(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentAsString(result) must include("Sorry we are experiencing technical problems")
        contentAsString(result) must include("Please try again in few moments")
        contentAsString(result) must not include "What do you want to find out?"
      }

      "return a Bad Request and errors when invalid data is submitted" in {
        enable(OptimisedFlow)

        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", "a" * (CustomisePDFFormProvider.maxFieldLength + 1)))
        val boundForm = form.bind(Map("completedBy" -> "a" * (CustomisePDFFormProvider.maxFieldLength + 1)))

        val validData = Map(ResultPage.toString -> JsString(FakeTimestamp.timestamp()))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(boundForm)
      }

      "redirect to Index Controller for a GET if no existing data is found" in {
        enable(OptimisedFlow)

        val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }

      "redirect to Index Controller for a POST if no existing data is found" in {
        enable(OptimisedFlow)

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", testAnswer))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }

    "If the OptimisedFlow is disabled" should {

      "return OK and the correct view for a GET" in {
        val result = controller().onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {
        val validData = Map(CustomisePDFPage.toString -> Json.toJson(Answers(AdditionalPdfDetails(Some("answer")), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe viewAsString(form.fill(AdditionalPdfDetails(Some(testAnswer))))
      }

      "show the PDF view" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val validData = Map(ResultPage.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val response: PDFGeneratorHttpParser.Response = Right(SuccessfulPDF(ByteString("PDF")))

        mockGeneratePdf(response)

        val result = controller(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe "PDF"
      }

      "show the PDF view with a default timestamp" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val response: PDFGeneratorHttpParser.Response = Right(SuccessfulPDF(ByteString("PDF")))

        mockGeneratePdf(response)

        val result = controller().onSubmit(NormalMode)(postRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe "PDF"
      }

      "show the PDF view when the feature is disabled" in {
        disable(PrintPDF)
        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val validData = Map(ResultPage.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe OK
      }

      "handle error from PDF service" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

        val response: PDFGeneratorHttpParser.Response = Left(BadRequest)

        val validData = Map(ResultPage.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 0)))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        mockGeneratePdf(response)

        val result = controller(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentAsString(result) must include("Sorry we are experiencing technical problems")
        contentAsString(result) must include("Please try again in few moments")
        contentAsString(result) must not include "What do you want to find out?"
      }

      "return a Bad Request and errors when invalid data is submitted" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", "a" * (CustomisePDFFormProvider.maxFieldLength + 1)))
        val boundForm = form.bind(Map("completedBy" -> "a" * (CustomisePDFFormProvider.maxFieldLength + 1)))

        val validData = Map(ResultPage.toString -> JsString(FakeTimestamp.timestamp()))
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onSubmit(NormalMode)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(boundForm)
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
