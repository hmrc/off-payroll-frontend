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
import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitch
import connectors.httpParsers.PDFGeneratorHttpParser
import connectors.{FakeDataCacheConnector, PDFGeneratorConnector}
import connectors.httpParsers.PDFGeneratorHttpParser.{BadRequest, ErrorResponse, SuccessResponse, SuccessfulPDF}
import controllers.actions._
import forms.CustomisePDFFormProvider
import models.{AdditionalPdfDetails, NormalMode}
import navigation.FakeNavigator
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import pages.CustomisePDFPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Call
import play.api.test.Helpers._
import play.twirl.api.Html
import services.{DecisionService, PDFService}
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.CustomisePDFView

import scala.concurrent.Future

class PDFControllerSpec extends ControllerSpecBase {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new CustomisePDFFormProvider()
  val form = formProvider()

  val customisePdfView = injector.instanceOf[CustomisePDFView]
  val pdf = mock[PDFService]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) = new PDFController(
    new FakeDataCacheConnector,
    new FakeNavigator(onwardRoute),
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    customisePdfView,
    injector.instanceOf[DecisionService],
    pdf,
    errorHandler,
    frontendAppConfig
  ){
    override def isEnabled(featureSwitch: FeatureSwitch)(implicit config: FrontendAppConfig): Boolean = true
  }

  def viewAsString(form: Form[_] = form) = customisePdfView(frontendAppConfig, form, NormalMode)(fakeRequest, messages).toString

  val testAnswer = "answer"

  "CustomisePDF Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val validData = Map(CustomisePDFPage.toString -> Json.obj("completedBy" -> testAnswer))
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(AdditionalPdfDetails(Some(testAnswer))))
    }

    "show the PDF view" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

      val response: PDFGeneratorHttpParser.Response = Right(SuccessfulPDF(ByteString("PDF")))

      when(pdf.generatePdf(any())(any(),any())).thenReturn(Future.successful(response))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe "PDF"
    }

    "handle error from PDF service" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", testAnswer))

      val response: PDFGeneratorHttpParser.Response = Left(BadRequest)

      when(pdf.generatePdf(any())(any(),any())).thenReturn(Future.successful(response))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe INTERNAL_SERVER_ERROR
      contentAsString(result) must include("Sorry, we’re experiencing technical difficulties")
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("completedBy", "a" * (CustomisePDFFormProvider.maxFieldLength + 1)))
      val boundForm = form.bind(Map("completedBy" -> "a" * (CustomisePDFFormProvider.maxFieldLength + 1)))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", testAnswer))
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
