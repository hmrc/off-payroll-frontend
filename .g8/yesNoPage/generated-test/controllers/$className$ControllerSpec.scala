package controllers

import play.api.data.Form
import play.api.libs.json.{JsBoolean, Json}
import uk.gov.hmrc.http.cache.client.CacheMap
import navigation.FakeNavigator
import connectors.FakeDataCacheConnector
import controllers.actions.{FakeDontGetDataDataRetrievalAction, FakeGeneralDataRetrievalAction, _}
import play.api.test.Helpers._
import forms.$className$FormProvider
import models.requests.DataRequest
import models.{Answers, NormalMode}
import pages.{$className$Page, $className$Page}
import play.api.mvc.Call
import views.html.$className$View

class $className$ControllerSpec extends ControllerSpecBase {

  val formProvider = new $className$FormProvider()
  val form = formProvider()

  val view = injector.instanceOf[$className$View]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new $className$Controller(
    appConfig = frontendAppConfig,
    dataCacheConnector = new FakeDataCacheConnector,
    navigator = new FakeNavigator(onwardRoute),
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    controllerHelper = mockControllerHelper
  )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  "$className$Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val validData = Map($className$Page.toString -> Json.toJson(Answers(true,0)))
      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val validData = Map($className$Page.toString -> Json.toJson(Answers(true,0)))

      val answers = userAnswers.set($className$Page,0,true)
      mockConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)

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

    "redirect to Index for a GET if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
