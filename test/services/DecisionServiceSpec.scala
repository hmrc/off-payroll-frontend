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

package services

import base.SpecBase
import connectors.DecisionConnector
import forms.DeclarationFormProvider
import handlers.ErrorHandler
import models.AboutYouAnswer.Worker
import models.ArrangedSubstitue.YesClientAgreed
import models.CannotClaimAsExpense.{WorkerHadOtherExpenses, WorkerUsedVehicle}
import models.ChooseWhereWork.Workeragreewithothers
import models.HowWorkIsDone.WorkerFollowStrictEmployeeProcedures
import models.HowWorkerIsPaid.Commission
import models.IdentifyToStakeholders.WorkAsIndependent
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.CannotBeCorrected
import models.ResultEnum._
import models.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.WeightedAnswerEnum.{HIGH, LOW}
import models.WorkerType.SoleTrader
import models._
import org.mockito.Matchers._
import org.mockito.Mockito.when
import pages._
import play.api.mvc.{Call, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, redirectLocation, _}
import play.twirl.api.Html
import uk.gov.hmrc.http.HeaderCarrier
import views.html.results._

import scala.concurrent.Future

class DecisionServiceSpec extends SpecBase {

  implicit val headerCarrier = new HeaderCarrier()
  implicit val request = FakeRequest("", "")

  val formProvider = new DeclarationFormProvider()

  val connector = mock[DecisionConnector]
  override val errorHandler: ErrorHandler = mock[ErrorHandler]

  when(errorHandler.standardErrorTemplate(any(), any(), any())(any())).thenReturn(Html("Error page"))

  val service: DecisionService = new DecisionServiceImpl(connector, errorHandler, formProvider,
    injector.instanceOf[OfficeHolderInsideIR35View],
    injector.instanceOf[OfficeHolderEmployedView],
    injector.instanceOf[CurrentSubstitutionView],
    injector.instanceOf[FutureSubstitutionView],
    injector.instanceOf[SelfEmployedView],
    injector.instanceOf[EmployedView],
    injector.instanceOf[ControlView],
    injector.instanceOf[FinancialRiskView],
    injector.instanceOf[IndeterminateView],
    injector.instanceOf[InsideIR35View],
    frontendAppConfig
  )

  val userAnswers: UserAnswers = UserAnswers("id")
    .set(AboutYouPage, Worker)
    .set(ContractStartedPage, true)
    .set(WorkerTypePage, SoleTrader)
    .set(OfficeHolderPage, false)
    .set(ArrangedSubstituePage, YesClientAgreed)
    .set(DidPaySubstitutePage, false)
    .set(WouldWorkerPaySubstitutePage, true)
    .set(RejectSubstitutePage, false)
    .set(NeededToPayHelperPage, false)
    .set(MoveWorkerPage, CanMoveWorkerWithPermission)
    .set(HowWorkIsDonePage, WorkerFollowStrictEmployeeProcedures)
    .set(ScheduleOfWorkingHoursPage, WorkerAgreeSchedule)
    .set(ChooseWhereWorkPage, Workeragreewithothers)
    .set(CannotClaimAsExpensePage, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
    .set(HowWorkerIsPaidPage, Commission)
    .set(PutRightAtOwnCostPage, CannotBeCorrected)
    .set(BenefitsPage, false)
    .set(LineManagerDutiesPage, false)
    .set(InteractWithStakeholdersPage, false)
    .set(IdentifyToStakeholdersPage, WorkAsIndependent)

  val exitResponse = DecisionResponse("1.0.0-beta", "12345",
<<<<<<< HEAD
    Score(None, None, Some(HIGH), Some(LOW), Some(LOW), Some(LOW)),
    UNKNOWN
=======
    Score(None, None, Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
    OUTSIDE_IR35
>>>>>>> 8100babb898e239b0dfb157ec7f5490db44b96d4
  )

  val response = DecisionResponse("1.0.0-beta", "12345",
    Score(Some(SetupEnum.CONTINUE), Some(ExitEnum.CONTINUE), Some(HIGH), Some(LOW), Some(LOW), Some(LOW)),
    NOT_MATCHED
  )

  val error = ErrorTemplate("error.title")

  def onwardRoute = Call("GET", "/continue")

  def exitRoute = Call("GET", "/result")


  "Calling the decide service" should {
    "return a continue decision based on the interview" in {

      when(connector.decide(Interview(userAnswers))).thenReturn(Future.successful(Right(response)))

      val result = service.decide(userAnswers, onwardRoute, error)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)

    }
    "return a decision based on the interview" in {

      when(connector.decide(Interview(userAnswers))).thenReturn(Future.successful(Right(exitResponse)))

      val result = service.decide(userAnswers, onwardRoute, error)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.ResultController.onPageLoad().url)
    }

    "handle 400 errors" in {

      when(connector.decide(Interview(userAnswers))).thenReturn(Future.successful(Left(ErrorResponse(400, "Bad"))))

      val result = service.decide(userAnswers, onwardRoute, error)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe "Error page"
    }

    "handle 500 errors" in {

      when(connector.decide(Interview(userAnswers))).thenReturn(Future.successful(Left(ErrorResponse(500, "Internal error"))))

      val result = service.decide(userAnswers, onwardRoute, error)

      status(result) mustBe INTERNAL_SERVER_ERROR
      contentAsString(result) mustBe "Error page"
    }
  }

}
