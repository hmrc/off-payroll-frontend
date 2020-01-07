/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.financialRisk


import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.financialRisk.HowWorkerIsPaidFormProvider
import models._
import models.requests.DataRequest
import models.sections.financialRisk.HowWorkerIsPaid
import navigation.mocks.FakeNavigators.FakeFinancialRiskNavigator
import pages.sections.financialRisk.HowWorkerIsPaidPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.financialRisk.HowWorkerIsPaidView

class HowWorkerIsPaidControllerSpec extends ControllerSpecBase {

  val formProvider = new HowWorkerIsPaidFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[HowWorkerIsPaidView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new HowWorkerIsPaidController(
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    navigator = FakeFinancialRiskNavigator,
    frontendAppConfig
  )

  val validData = Map(HowWorkerIsPaidPage.toString -> Json.toJson(HowWorkerIsPaid.values.head))

  "HowWorkerIsPaid Controller" when {

    "the normal flow is enabled" must {

      def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

      "return OK and the correct view for a GET" in {


        val result = controller().onPageLoad(NormalMode)(fakeRequest)
        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {

        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe viewAsString(form.fill(HowWorkerIsPaid.values.head))
      }

      "redirect to the next page when valid data is submitted" in {

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", HowWorkerIsPaid.options.head.value))
        val answers = userAnswers.set(HowWorkerIsPaidPage,HowWorkerIsPaid.HourlyDailyOrWeekly)
        mockConstructAnswers(DataRequest(postRequest,"id",answers),HowWorkerIsPaid)(answers)

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val result = controller().onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }

      "return a Bad Request and errors when invalid data is submitted" in {

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
        val boundForm = form.bind(Map("value" -> "invalid value"))

        val result = controller().onSubmit(NormalMode)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(boundForm)
      }

      "redirect to Index Controller for a GET if no existing data is found" in {

        val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }

      "redirect to Index Controller for a POST if no existing data is found" in {

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", HowWorkerIsPaid.options.head.value))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }
  }
}
