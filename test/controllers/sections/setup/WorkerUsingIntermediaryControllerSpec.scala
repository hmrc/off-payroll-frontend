/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers.sections.setup


import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.setup.WorkerUsingIntermediaryFormProvider
import models._
import navigation.mocks.FakeNavigators.FakeSetupNavigator
import pages.sections.setup.WorkerUsingIntermediaryPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.setup.WorkerUsingIntermediaryView

class WorkerUsingIntermediaryControllerSpec extends ControllerSpecBase {

  val formProvider = new WorkerUsingIntermediaryFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[WorkerUsingIntermediaryView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction,
                 requireUserType: FakeUserTypeRequiredAction = FakeUserTypeRequiredSuccessAction) =
    new WorkerUsingIntermediaryController(
      identify = FakeIdentifierAction,
      getData = dataRetrievalAction,
      requireData = new DataRequiredActionImpl(messagesControllerComponents),
      requireUserType = requireUserType,
      workerUsingIntermediaryFormProvider = formProvider,
      controllerComponents = messagesControllerComponents,
      workerUsingIntermediaryView = view,
      compareAnswerService = mockCompareAnswerService,
      dataCacheConnector = mockDataCacheConnector,
      navigator = FakeSetupNavigator,
      appConfig = frontendAppConfig
    )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(WorkerUsingIntermediaryPage.toString -> Json.toJson(true))

  "WorkerType Controller" must {

    "return OK and the correct view for a GET in the optimised view" in {

      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered for the normal flow" in {

      val getRelevantData = FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "redirect to the something went wrong page when no user type is given" in {

      val result = controller(requireUserType = FakeUserTypeRequiredFailureAction).onPageLoad(NormalMode)(fakeRequest)

      redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong.url)

    }

    "return a Bad Request and errors when invalid data is submitted for the normal flow" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Index Controller for a GET if no existing data is found" in {

      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad.url)

    }

    "redirect to Index Controller for a POST if no existing data is found" in {

      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad.url)

    }
  }
}
