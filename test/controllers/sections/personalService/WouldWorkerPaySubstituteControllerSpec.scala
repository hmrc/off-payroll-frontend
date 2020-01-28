/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.personalService

import connectors.mocks.MockDataCacheConnector
import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.personalService.WouldWorkerPaySubstituteFormProvider
import models._
import models.requests.DataRequest
import navigation.mocks.FakeNavigators.FakePersonalServiceNavigator
import pages.sections.personalService.WouldWorkerPaySubstitutePage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.personalService.WouldWorkerPaySubstituteView

class WouldWorkerPaySubstituteControllerSpec extends ControllerSpecBase with MockDataCacheConnector {

  val formProvider = new WouldWorkerPaySubstituteFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[WouldWorkerPaySubstituteView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction,
                 requireUserType: UserTypeRequiredAction = FakeUserTypeRequiredSuccessAction) = new WouldWorkerPaySubstituteController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    requireUserType = requireUserType,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    appConfig = frontendAppConfig,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    navigator = FakePersonalServiceNavigator
  )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(WouldWorkerPaySubstitutePage.toString -> Json.toJson(true))

  "WouldWorkerPaySubstitute Controller" must {

    "For the OptimisedJourney" should {

      def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

      "return OK and the correct view for a GET" in {

        val result = controller().onPageLoad(NormalMode)(fakeRequest)
        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {

        val getRelevantData = FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe viewAsString(form.fill(true))
      }

      "redirect to the something went wrong page when no user type is given" in {

        val result = controller(requireUserType = FakeUserTypeRequiredFailureAction).onPageLoad(NormalMode)(fakeRequest)

        redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong().url)
      }

      "redirect to the next page when valid data is submitted" in {

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

        val answers = userAnswers.set(WouldWorkerPaySubstitutePage, true)
        mockConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)

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

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }
  }
}
