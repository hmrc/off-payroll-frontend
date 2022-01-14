/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers

import controllers.actions._
import forms.AddReferenceDetailsFormProvider
import models.requests.DataRequest
import navigation.mocks.FakeNavigators.FakeCYANavigator
import pages.AddReferenceDetailsPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.AddReferenceDetailsView

class AddReferenceDetailsControllerSpec extends ControllerSpecBase {

  val formProvider = new AddReferenceDetailsFormProvider()
  val form = formProvider()

  val view = injector.instanceOf[AddReferenceDetailsView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction,
                 requireUserType: FakeUserTypeRequiredAction = FakeUserTypeRequiredSuccessAction) = new AddReferenceDetailsController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    requireUserType,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view,
    navigator = FakeCYANavigator,
    dataCacheConnector = mockDataCacheConnector,
    compareAnswerService = mockCompareAnswerService,
    appConfig = frontendAppConfig
  )

  def viewAsString(form: Form[_] = form) = view(form)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(AddReferenceDetailsPage.toString -> Json.toJson(true))

  "AddReferenceDetails Controller" must {

    "return OK and the correct view for a GET" in {

      val result = controller().onPageLoad()(fakeRequest)
      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }


    "populate the view correctly on a GET when the question has previously been answered" in {

      val getRelevantData = FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad()(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "redirect to the something went wrong page when no user type is given" in {

      val result = controller(requireUserType = FakeUserTypeRequiredFailureAction).onPageLoad()(fakeRequest)
      redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong.url)

    }

    "redirect to the next page when valid data is submitted" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val answers = userAnswers.set(AddReferenceDetailsPage,true)
      mockConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

      val result = controller().onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit()(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Index Controller for a GET if no existing data is found" in {

      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad()(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad.url)

    }

    "redirect to Index Controller for a POST if no existing data is found" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad.url)

    }
  }
}
