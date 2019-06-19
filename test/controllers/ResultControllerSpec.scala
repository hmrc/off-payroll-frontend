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

import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import connectors.DataCacheConnector
import connectors.mocks.MockDataCacheConnector
import controllers.actions._
import forms.DeclarationFormProvider
import models.ChooseWhereWork.WorkerChooses
import models._
import models.requests.DataRequest
import navigation.FakeNavigator
import pages.ResultPage
import pages.sections.control.ChooseWhereWorkPage
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.mvc.Call
import play.api.test.Helpers._
import play.twirl.api.Html
import services.{DecisionService, OptimisedDecisionService}
import services.mocks.{MockCompareAnswerService, MockDecisionService, MockOptimisedDecisionService}
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.FakeTimestamp
import viewmodels.AnswerSection
import views.html.subOptimised.results._

class ResultControllerSpec extends ControllerSpecBase with MockOptimisedDecisionService {

  val formProvider = new DeclarationFormProvider()
  val form = formProvider()

  val controllerHelper = injector.instanceOf[ControllerHelper]

  val officeHolderInsideIR35View = injector.instanceOf[OfficeHolderInsideIR35View]
  val officeHolderEmployedView = injector.instanceOf[OfficeHolderEmployedView]
  val currentSubstitutionView = injector.instanceOf[CurrentSubstitutionView]
  val futureSubstitutionView = injector.instanceOf[FutureSubstitutionView]
  val selfEmployedView = injector.instanceOf[SelfEmployedView]
  val employedView = injector.instanceOf[EmployedView]
  val controlView = injector.instanceOf[ControlView]
  val financialRiskView = injector.instanceOf[FinancialRiskView]
  val indeterminateView = injector.instanceOf[IndeterminateView]
  val insideIR35 = injector.instanceOf[InsideIR35View]

  val postAction = routes.ResultController.onSubmit()

  val answers = Seq(
    AnswerSection(Some(Messages("result.peopleInvolved.h2")), None, Seq()),
    AnswerSection(Some(Messages("result.workersDuties.h2")), whyResult = Some(Html(messages("result.officeHolderInsideIR35.whyResult.p1"))), Seq()),
    AnswerSection(Some(Messages("result.substitutesHelpers.h2")), whyResult = Some(Html(messages("result.substitutesAndHelpers.summary"))), Seq()),
    AnswerSection(Some(Messages("result.workArrangements.h2")), whyResult = Some(Html(messages("result.workArrangements.summary"))), Seq()),
    AnswerSection(Some(Messages("result.financialRisk.h2")), whyResult = Some(Html(messages("result.financialRisk.summary"))), Seq()),
    AnswerSection(Some(Messages("result.partAndParcel.h2")), whyResult = Some(Html(messages("result.partParcel.summary"))), Seq())
  )

  val version = "1.5.0-final"

  object TestResultController extends ResultController(
    FakeIdentifierAction,
    FakeEmptyCacheMapDataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    injector.instanceOf[DecisionService],
    formProvider,
    new FakeNavigator(onwardRoute),
    mockDataCacheConnector,
    FakeTimestamp,
    mockCompareAnswerService,
    mockOptimisedDecisionService,
    frontendAppConfig
  )

  def viewAsString() = employedView(answers, version, form, postAction)(fakeRequest, messages, frontendAppConfig).toString

  "ResultPage Controller" must {

    "If the Optimised Flow is enabled" should {

      "If a success response is returned from the Optimised Decision Service" should {

        "return OK and the HTML returned from the Decision Service" in {

          enable(OptimisedFlow)

          val validData = Map(ResultPage.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 0)))
          val answers = userAnswers.set(ResultPage, 0, FakeTimestamp.timestamp())
          val dataRequest = DataRequest(fakeRequest, "id", answers)

          mockConstructAnswers(dataRequest, FakeTimestamp.timestamp())(answers)
          mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))
          mockDetermineResultView(onwardRoute)(Right(Html("Success")))

          val result = TestResultController.onPageLoad(dataRequest)

          status(result) mustBe OK
          contentAsString(result) mustBe "Success"
        }
      }

      "If an error response is returned from the Optimised Decision Service" should {

        "return ISE and the HTML returned from the Decision Service" in {

          enable(OptimisedFlow)

          val validData = Map(ResultPage.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 0)))
          val answers = userAnswers.set(ResultPage, 0, FakeTimestamp.timestamp())
          val dataRequest = DataRequest(fakeRequest, "id", answers)

          mockConstructAnswers(dataRequest, FakeTimestamp.timestamp())(answers)
          mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))
          mockDetermineResultView(onwardRoute)(Left(Html("Error")))

          val result = TestResultController.onPageLoad(dataRequest)

          status(result) mustBe INTERNAL_SERVER_ERROR
          contentAsString(result) mustBe "Error"
        }
      }

      "redirect to next page" in {

        enable(OptimisedFlow)

        val result = TestResultController.onSubmit(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }

    "If the Optimised Flow is disabled" should {

      "return OK and the correct view for a GET" in {

        val validData = Map(ResultPage.toString -> Json.toJson(Answers(FakeTimestamp.timestamp(), 0)))
        val postRequest = fakeRequest
        val answers = userAnswers.set(ResultPage, 0, FakeTimestamp.timestamp())

        mockConstructAnswers(DataRequest(postRequest, "id", answers), FakeTimestamp.timestamp())(answers)

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))


        val result = TestResultController.onPageLoad(fakeRequest.withSession(SessionKeys.result -> ResultEnum.EMPLOYED.toString))

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }

      "redirect to next page" in {

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

        val result = TestResultController.onSubmit(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }

      "handle errors" in {

        val postRequest = fakeRequest

        val result = TestResultController.onSubmit(postRequest)

        status(result) mustBe BAD_REQUEST
      }
    }
  }
}




