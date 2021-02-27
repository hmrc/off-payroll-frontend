/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.sections.control


import connectors.mocks.MockDataCacheConnector
import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.control.ScheduleOfWorkingHoursFormProvider
import models.NormalMode
import models.requests.DataRequest
import models.sections.control.ScheduleOfWorkingHours
import navigation.mocks.FakeNavigators.FakeControlNavigator
import pages.sections.control.ScheduleOfWorkingHoursPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.control.ScheduleOfWorkingHoursView

class ScheduleOfWorkingHoursControllerSpec extends ControllerSpecBase with MockDataCacheConnector {

  val formProvider = new ScheduleOfWorkingHoursFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[ScheduleOfWorkingHoursView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction,
                 requireUserType: UserTypeRequiredAction = FakeUserTypeRequiredSuccessAction) = new ScheduleOfWorkingHoursController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    requireUserType = requireUserType,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    appConfig = frontendAppConfig,
    view = view,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    navigator = FakeControlNavigator
  )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(ScheduleOfWorkingHoursPage.toString -> Json.toJson(ScheduleOfWorkingHours.values.head))

  "ScheduleOfWorkingHours Controller" must {

    "return OK and the correct view for a GET for optimised view" in {

      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered for optimised view" in {

      val getRelevantData = FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(ScheduleOfWorkingHours.values.head))
    }

    "redirect to the something went wrong page when no user type is given" in {

      val result = controller(requireUserType = FakeUserTypeRequiredFailureAction).onPageLoad(NormalMode)(fakeRequest)

      redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong().url)
    }

    "redirect to the next page when valid data is submitted" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ScheduleOfWorkingHours.options.head.value))

      val answers = userAnswers.set(ScheduleOfWorkingHoursPage,ScheduleOfWorkingHours.ScheduleDecidedForWorker)
      mockConstructAnswers(DataRequest(postRequest,"id",answers),ScheduleOfWorkingHours)(answers)
      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to the next page when valid data is submitted for optimised view" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ScheduleOfWorkingHours.options.head.value))

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

      val answers = userAnswers.set(ScheduleOfWorkingHoursPage,ScheduleOfWorkingHours.ScheduleDecidedForWorker)
      mockConstructAnswers(DataRequest(postRequest,"id",answers),ScheduleOfWorkingHours)(answers)

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted for optimised view" in {

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

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ScheduleOfWorkingHours.options.head.value))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
