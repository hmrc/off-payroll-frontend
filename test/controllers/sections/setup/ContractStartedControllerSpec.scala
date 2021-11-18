/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.sections.setup


import connectors.mocks.MockAuditConnector
import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.setup.ContractStartedFormProvider
import models.{AuditJourneyStart, NormalMode}
import models.requests.DataRequest
import navigation.mocks.FakeNavigators.FakeSetupNavigator
import pages.sections.setup.ContractStartedPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap

class ContractStartedControllerSpec extends ControllerSpecBase with MockAuditConnector {

  val formProvider = new ContractStartedFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[views.html.sections.setup.ContractStartedView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction,
                 requireUserType: UserTypeRequiredAction = FakeUserTypeRequiredSuccessAction) = new ContractStartedController(
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
    navigator = FakeSetupNavigator,
    auditConnector = mockAuditConnector,
    appConfig = frontendAppConfig
  )

  def viewAsStringOptimised(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(ContractStartedPage.toString -> Json.toJson(true))

  "ContractStarted Controller" must {

    "return OK and the correct view for a GET for the normal flow" in {

      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsStringOptimised()
    }

    "populate the view correctly on a GET when the question has previously been answered for normal flow" in {

      val validData = Map(ContractStartedPage.toString -> Json.toJson(true))
      val getRelevantData = FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsStringOptimised(form.fill(true))
    }

    "redirect to the something went wrong page when no user type is given" in {

      val result = controller(requireUserType = FakeUserTypeRequiredFailureAction).onPageLoad(NormalMode)(fakeRequest)

      redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong
.url)
    }

    "redirect to the next page when valid data is submitted" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val answers = userAnswers.set(ContractStartedPage,true)

      mockConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)
      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))
      mockAuditEvent("cestJourneyStart", AuditJourneyStart(userAnswers))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
    }

    "redirect to Index Controller for a GET if no existing data is found" in {

      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad
.url)
    }

    "redirect to Index Controller for a POST if no existing data is found" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad
.url)
    }
  }
}
