/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.sections.exit


import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.exit.OfficeHolderFormProvider
import models.requests.DataRequest
import models.{CheckMode, NormalMode}
import navigation.mocks.FakeNavigators.FakeExitNavigator
import pages.sections.exit.OfficeHolderPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.exit.OfficeHolderView

class OfficeHolderControllerSpec extends ControllerSpecBase {

  val formProvider = new OfficeHolderFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[OfficeHolderView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction,
                 requireUserType: UserTypeRequiredAction = FakeUserTypeRequiredSuccessAction) = new OfficeHolderController(
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
    navigator = FakeExitNavigator
  )

  val validData = Map(OfficeHolderPage.toString -> Json.toJson(true))

  "OfficeHolder Controller" must {

    "override the mode if office holder set to false in check mode" in {

      val answers = userAnswers.set(OfficeHolderPage, false)

      val validData = Map(OfficeHolderPage.toString -> Json.toJson(false))

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "false"))

      val getRelevantData = FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      mockConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)

      val result = controller(getRelevantData).onSubmit(CheckMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "If the OptimisedFlow is enabled" should {

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

        val answers = userAnswers.set(OfficeHolderPage, true)

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

        mockConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)

        val result = controller().onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }

      "override the mode if changing from Yes to No, so that Normal flow is continued" in {

        val answers = userAnswers.set(OfficeHolderPage, true)

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "false"))

        mockConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)

        val result = controller().onSubmit(CheckMode)(postRequest)

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
