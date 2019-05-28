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
import config.SessionKeys
import connectors.{DataCacheConnector, DecisionConnector}
import forms.mappings.Mappings
import forms.{DeclarationFormProvider, InteractWithStakeholdersFormProvider}
import handlers.ErrorHandler
import models.AboutYouAnswer.Worker
import models.ArrangedSubstitute.{No, YesClientAgreed}
import models.CannotClaimAsExpense.{WorkerHadOtherExpenses, WorkerUsedVehicle}
import models.ChooseWhereWork.WorkerAgreeWithOthers
import models.HowWorkIsDone.WorkerFollowStrictEmployeeProcedures
import models.HowWorkerIsPaid.Commission
import models.IdentifyToStakeholders.WorkAsIndependent
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.CannotBeCorrected
import models.ResultEnum._
import models.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.WeightedAnswerEnum.{HIGH, LOW}
import models.WorkerType.{LimitedCompany, SoleTrader}
import models._
import models.requests.DataRequest
import org.mockito.Matchers
import org.mockito.Matchers._
import org.mockito.Mockito.when
import pages._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{AboutYouPage, ContractStartedPage, WorkerTypePage}
import play.api.data.Form
import play.api.data.Forms.of
import play.api.mvc.{Call, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, redirectLocation, _}
import play.twirl.api.Html
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.UserAnswersUtils
import viewmodels.AnswerSection
import views.html.results._

import scala.concurrent.Future

class DecisionServiceSpec extends SpecBase {

  implicit val headerCarrier = new HeaderCarrier()
  implicit val request = FakeRequest("", "")

  val formProvider = new DeclarationFormProvider()

  val connector = mock[DecisionConnector]
  val dataConnector = mock[DataCacheConnector]
  override val errorHandler: ErrorHandler = mock[ErrorHandler]

  when(errorHandler.standardErrorTemplate(any(), any(), any())(any())).thenReturn(Html("Error page"))
  when(errorHandler.internalServerErrorTemplate(any())).thenReturn(Html("Error page"))

  val service: DecisionService = new DecisionServiceImpl(connector, dataConnector, errorHandler, formProvider,
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
    .set(AboutYouPage,0, Worker)
    .set(ContractStartedPage,1, true)
    .set(WorkerTypePage,2, SoleTrader)
    .set(OfficeHolderPage,3, false)
    .set(ArrangedSubstitutePage,4, YesClientAgreed)
    .set(DidPaySubstitutePage, 5,false)
    .set(WouldWorkerPaySubstitutePage,6, true)
    .set(RejectSubstitutePage,7, false)
    .set(NeededToPayHelperPage,8, false)
    .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
    .set(HowWorkIsDonePage, 10,WorkerFollowStrictEmployeeProcedures)
    .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
    .set(ChooseWhereWorkPage,12,WorkerAgreeWithOthers)
    .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
    .set(HowWorkerIsPaidPage,14,Commission)
    .set(PutRightAtOwnCostPage,15,CannotBeCorrected)
    .set(BenefitsPage,16,false)
    .set(LineManagerDutiesPage,17, false)
    .set(InteractWithStakeholdersPage,18, false)
    .set(IdentifyToStakeholdersPage, 19,WorkAsIndependent)

  val exitResponse = DecisionResponse("1.0.0-beta", "12345",
    Score(None, None, Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
    OUTSIDE_IR35
  )

  val response = DecisionResponse("1.0.0-beta", "12345",
    Score(Some(SetupEnum.CONTINUE), Some(ExitEnum.CONTINUE), Some(HIGH), Some(LOW), Some(LOW), Some(LOW)),
    NOT_MATCHED
  )

  val riskResponse = DecisionResponse("1.0.0-beta", "12345",
    Score(Some(SetupEnum.CONTINUE), Some(ExitEnum.CONTINUE), Some(HIGH), None, Some(LOW), Some(LOW)),
    OUTSIDE_IR35
  )

  val controlResponse = DecisionResponse("1.0.0-beta", "12345",
    Score(Some(SetupEnum.CONTINUE), Some(ExitEnum.CONTINUE), Some(HIGH), Some(LOW), None, Some(LOW)),
    OUTSIDE_IR35
  )

  val error = ErrorTemplate("error.title")

  def onwardRoute = Call("GET", "/continue")

  def exitRoute = Call("GET", "/result")

  object Section extends UserAnswersUtils

  "Calling the decide service" should {

    "determine the view when outside and soletrader" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(ContractStartedPage,1, true)
        .set(WorkerTypePage,2, SoleTrader)
        .set(OfficeHolderPage, 3,false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage, 5,false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8,false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage, 10,WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage, 13,Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14,Commission)
        .set(PutRightAtOwnCostPage,15,CannotBeCorrected)
        .set(BenefitsPage,16,false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage, 19,WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.OUTSIDE_IR35.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("This engagement should be classed as self-employed for tax purposes")
      result.toString() must include(messagesApi("result.selfEmployed.whyResult.p1"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
      result.toString() must include(messagesApi("officeHolder.exclamation"))
      result.toString() must include(messagesApi("rejectSubstitute.exclamation"))
    }

    "determine the view when outside and financial risk" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(ContractStartedPage,1, true)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.OUTSIDE_IR35.toString,
        SessionKeys.financialRiskResult -> WeightedAnswerEnum.OUTSIDE_IR35.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("The intermediaries legislation does not apply to this engagement")
      result.toString() must include(messagesApi("result.financialRisk.whyResult.p1"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }

    "determine the view when outside and control" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(ContractStartedPage,1, true)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.OUTSIDE_IR35.toString,
        SessionKeys.controlResult -> WeightedAnswerEnum.OUTSIDE_IR35.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("The intermediaries legislation does not apply to this engagement")
      result.toString() must include(messagesApi("result.control.whyResult.p1"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }

    "determine the view when outside and current substitution" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(ContractStartedPage,1, true)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.OUTSIDE_IR35.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("The intermediaries legislation does not apply to this engagement")
      result.toString() must include(messagesApi("result.currentSubstitution.whyResult.p1"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }
    "determine the view when outside and future substitution due to not yet arranged" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(ContractStartedPage,1, true)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, No)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.OUTSIDE_IR35.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("The intermediaries legislation does not apply to this engagement")
      result.toString() must include(messagesApi("result.futureSubstitution.whyResult.p1"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }

    "determine the view when outside and future substitution" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(ContractStartedPage,1, false)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.OUTSIDE_IR35.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("The intermediaries legislation does not apply to this engagement")
      result.toString() must include(messagesApi("result.futureSubstitution.whyResult.p1"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }

    "route to the error page if cannot route to a result page based on the information provided" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.OUTSIDE_IR35.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() mustBe "Error page"

    }

    "return a 500 if by some magical means, the decision string vanishes" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ""), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() mustBe "Error page"

    }

    "determine the view when inside and route to employed view" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(WorkerTypePage,2, SoleTrader)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.INSIDE_IR35.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("This engagement should be classed as employed for tax purposes")
      result.toString() must include(messagesApi("result.employed.whyResult.p1"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }

    "determine the view when inside and route to office holder inside view" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, true)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.INSIDE_IR35.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("The intermediaries legislation applies to this engagement")
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }

    "determine the view when inside and route to inside view" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.INSIDE_IR35.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("The intermediaries legislation applies to this engagement")
      result.toString() must include(messagesApi("result.insideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }

    "determine the view when self employed and route to self employed view" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(WorkerTypePage,2, SoleTrader)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.SELF_EMPLOYED.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("This engagement should be classed as self-employed for tax purposes")
      result.toString() must include(messagesApi("result.selfEmployed.shouldNowDo.p1.beforeLink"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }

    "determine the view when unknown and route to indeterminate view" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(WorkerTypePage,2, SoleTrader)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.UNKNOWN.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("We are unable to determine the tax status of this engagement")
      result.toString() must include(messagesApi("result.indeterminate.whyResult.p1"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }
    "determine the view when employed and route to office holder view" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, true)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.EMPLOYED.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("This engagement should be classed as employed for tax purposes")
      result.toString() must include(messagesApi("result.officeHolderEmployed.whyResult.p1"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }
    "determine the view when employed and route to employed view" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.EMPLOYED.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Section.answers

      val result = service.determineResultView(answers, None, false, None)

      result.toString() must include("This engagement should be classed as employed for tax purposes")
      result.toString() must include(messagesApi("result.employed.whyResult.p1"))
      result.toString() must include(messagesApi("result.officeHolderInsideIR35.whyResult.p1"))
      result.toString() must include(messagesApi("result.substitutesAndHelpers.summary"))
      result.toString() must include(messagesApi("result.workArrangements.summary"))
      result.toString() must include(messagesApi("result.financialRisk.summary"))
      result.toString() must include(messagesApi("result.partParcel.summary"))
    }
    "determine the view when employed and route to employed view with an error form" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(WorkerTypePage,2, LimitedCompany)
        .set(OfficeHolderPage,3, false)
        .set(ArrangedSubstitutePage,4, YesClientAgreed)
        .set(DidPaySubstitutePage,5, false)
        .set(WouldWorkerPaySubstitutePage,6, true)
        .set(RejectSubstitutePage,7, false)
        .set(NeededToPayHelperPage,8, false)
        .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
        .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
        .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
        .set(ChooseWhereWorkPage,12, WorkerAgreeWithOthers)
        .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
        .set(HowWorkerIsPaidPage,14, Commission)
        .set(PutRightAtOwnCostPage,15, CannotBeCorrected)
        .set(BenefitsPage,16, false)
        .set(LineManagerDutiesPage,17, false)
        .set(InteractWithStakeholdersPage,18, false)
        .set(IdentifyToStakeholdersPage,19, WorkAsIndependent)

      implicit val dataRequest = DataRequest(request.withSession(SessionKeys.result -> ResultEnum.EMPLOYED.toString), "", userAnswers)

      val answers: Seq[AnswerSection] = Seq()

      import forms.mappings.Mappings
      import play.api.data.Form

      object errorForm extends Mappings {
        val form: Form[Boolean] = Form("value" -> boolean("interactWithStakeholders.error.required")).withError("value", "Required")
      }

      val result = service.determineResultView(answers, Some(errorForm.form), false, None)

      result.toString() must include("This engagement should be classed as employed for tax purposes")
      result.toString() must include(messagesApi("result.employed.whyResult.p1"))
    }

    "determine the view when no answers or sessionkeys are populated" in {

      implicit val dataRequest = DataRequest(request, "", userAnswers)

      val answers: Seq[AnswerSection] = Seq()

      val result = service.determineResultView(answers, None, false, None)

      result.toString() mustBe "Error page"
    }
  }

  "Calling the decide service" should {

    when(dataConnector.save(any())).thenReturn(Future.successful(CacheMap("id", Map())))

    "return a continue decision based on the interview" in {

      implicit val dataRequest = DataRequest(request, "", userAnswers)

      when(connector.decide(Interview(userAnswers))).thenReturn(Future.successful(Right(response)))

      val result = service.decide(userAnswers, onwardRoute, error)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)

    }
    "return a continue decision based on the interview when control is empty (and save decision in db)" in {

      implicit val dataRequest = DataRequest(request, "", userAnswers)

      when(connector.decide(Interview(userAnswers))).thenReturn(Future.successful(Right(riskResponse)))
      when(connector.log(Interview(userAnswers),riskResponse)).thenReturn(Future.successful(Right(true)))
      when(dataConnector.addDecision(Matchers.any(),Matchers.any())).thenReturn(Future.successful(true))

      val result = service.decide(userAnswers, onwardRoute, error)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.ResultController.onPageLoad().url)

    }
    "return a continue decision based on the interview when risk is empty" in {

      implicit val dataRequest = DataRequest(request, "", userAnswers)

      when(connector.decide(Interview(userAnswers))).thenReturn(Future.successful(Right(controlResponse)))
      when(connector.log(Interview(userAnswers),controlResponse)).thenReturn(Future.successful(Right(true)))
      when(dataConnector.addDecision(Matchers.any(),Matchers.any())).thenReturn(Future.successful(true))

      val result = service.decide(userAnswers, onwardRoute, error)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.ResultController.onPageLoad().url)

    }
    "return a decision based on the interview" in {

      implicit val dataRequest = DataRequest(request, "", userAnswers)

      when(connector.decide(Interview(userAnswers))).thenReturn(Future.successful(Right(exitResponse)))
      when(connector.log(Interview(userAnswers),exitResponse)).thenReturn(Future.successful(Right(true)))
      when(dataConnector.addDecision(Matchers.any(),Matchers.any())).thenReturn(Future.successful(true))

      val result = service.decide(userAnswers, onwardRoute, error)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.ResultController.onPageLoad().url)
    }

    "handle 400 errors" in {

      implicit val dataRequest = DataRequest(request, "", userAnswers)

      when(connector.decide(Interview(userAnswers))).thenReturn(Future.successful(Left(ErrorResponse(400, "Bad"))))

      val result = service.decide(userAnswers, onwardRoute, error)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe "Error page"
    }

    "handle 500 errors" in {

      implicit val dataRequest = DataRequest(request, "", userAnswers)

      when(connector.decide(Interview(userAnswers))).thenReturn(Future.successful(Left(ErrorResponse(500, "Internal error"))))

      val result = service.decide(userAnswers, onwardRoute, error)

      status(result) mustBe INTERNAL_SERVER_ERROR
      contentAsString(result) mustBe "Error page"
    }
  }

}
