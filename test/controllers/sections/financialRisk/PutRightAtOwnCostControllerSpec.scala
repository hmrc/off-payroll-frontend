/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.sections.financialRisk


import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.financialRisk.PutRightAtOwnCostFormProvider
import models._
import models.requests.DataRequest
import models.sections.financialRisk.PutRightAtOwnCost
import models.sections.financialRisk.PutRightAtOwnCost.OutsideOfHoursNoCharge
import navigation.mocks.FakeNavigators.FakeFinancialRiskNavigator
import pages.sections.financialRisk.PutRightAtOwnCostPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.financialRisk.PutRightAtOwnCostView

class PutRightAtOwnCostControllerSpec extends ControllerSpecBase {

  val formProvider = new PutRightAtOwnCostFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[PutRightAtOwnCostView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction,
                 requireUserType: UserTypeRequiredAction = FakeUserTypeRequiredSuccessAction) = new PutRightAtOwnCostController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    requireUserType = requireUserType,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    navigator = FakeFinancialRiskNavigator,
    appConfig = frontendAppConfig
  )

  val validData = Map(PutRightAtOwnCostPage.toString -> Json.toJson(PutRightAtOwnCost.values.head))

  "PutRightAtOwnCost Controller" when {

    "optimal flow is enabled" must {

      def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

      "return OK and the correct view for a GET" in {

        val result = controller().onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {

        val getRelevantData = FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe viewAsString(form.fill(PutRightAtOwnCost.values.head))
      }

      "redirect to the next page when valid data is submitted" in {

        val userAnswers = UserAnswers("id").set(PutRightAtOwnCostPage, OutsideOfHoursNoCharge)

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", PutRightAtOwnCost.options.head.value))

        mockConstructAnswers(DataRequest(postRequest,"id",userAnswers),PutRightAtOwnCost)(userAnswers)
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
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad.url)

      }

      "redirect to Index Controller for a POST if no existing data is found" in {

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", PutRightAtOwnCost.options.head.value))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad.url)

      }
    }
  }
}
