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

import base.{GuiceAppSpecBase, SpecBase}
import config.SessionKeys
import config.featureSwitch.{CallNewDecisionService, FeatureSwitching, OptimisedFlow}
import connectors.mocks.{MockDataCacheConnector, MockDecisionConnector}
import forms.DeclarationFormProvider
import handlers.mocks.MockErrorHandler
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
import models.WhichDescribesYouAnswer.{ClientPAYE, WorkerPAYE}
import models.WorkerType.{LimitedCompany, SoleTrader}
import models._
import models.requests.DataRequest
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, redirectLocation, _}
import play.twirl.api.Html
import uk.gov.hmrc.http.HeaderCarrier
import utils.UserAnswersUtils
import viewmodels.AnswerSection
import views.html.subOptimised.results._

class DecisionServiceSpec extends GuiceAppSpecBase with MockDecisionConnector with MockDataCacheConnector with MockErrorHandler with FeatureSwitching {

  implicit val headerCarrier = new HeaderCarrier()
  implicit val request = FakeRequest("", "")

  val formProvider = new DeclarationFormProvider()

  val service: DecisionService = new DecisionServiceImpl(mockDecisionConnector, mockDataCacheConnector, mockErrorHandler, formProvider,
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
  
  def onwardRoute = Call("GET", "/continue")

  def exitRoute = Call("GET", "/result")

  implicit val dataRequest = DataRequest(request, "", userAnswers)

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

      mockInternalServerError(Html("Error page"))

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

    "determine the view when inside and route to office holder inside view when using the optimised view" in {

      enable(OptimisedFlow)

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(WhichDescribesYouPage,0, ClientPAYE)
        .set(IsWorkForPrivateSectorPage,1, false)
        .set(WorkerUsingIntermediaryPage,2, true)
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

      val answers: Seq[AnswerSection] = Seq()

      mockInternalServerError(Html("Error page"))

      val result = service.determineResultView(answers, None, false, None)

      result.toString() mustBe "Error page"
    }
  }

  "Calling the decide service" should {

    "return a continue decision from the new decision service based on the interview" in {

      enable(CallNewDecisionService)
      enable(OptimisedFlow)

      mockDecideNew(Interview(userAnswers))(Right(response))

      val result = service.decide(userAnswers, onwardRoute)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)

    }
    "return a continue decision based on the interview" in {

      disable(CallNewDecisionService)
      disable(OptimisedFlow)

      mockDecide(Interview(userAnswers))(Right(response))

      val result = service.decide(userAnswers, onwardRoute)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)

    }
    "return a continue decision based on the interview when control is empty" in {
      enable(OptimisedFlow)

      mockDecide(Interview(userAnswers))(Right(riskResponse))
      mockLog(Interview(userAnswers), riskResponse)(Right(true))

      val result = service.decide(userAnswers, onwardRoute)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/continue")
    }
    "return a continue decision based on the interview when risk is empty optimised" in {
      enable(OptimisedFlow)

      mockDecide(Interview(userAnswers))(Right(controlResponse))
      mockLog(Interview(userAnswers), controlResponse)(Right(true))

      val result = service.decide(userAnswers, onwardRoute)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/continue")

    }

    "return a continue decision based on the interview when risk is empty" in {

      mockDecide(Interview(userAnswers))(Right(controlResponse))
      mockLog(Interview(userAnswers), controlResponse)(Right(true))

      val result = service.decide(userAnswers, onwardRoute)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.ResultController.onPageLoad().url)

    }
    "return a decision based on the interview" in {

      mockDecide(Interview(userAnswers))(Right(exitResponse))
      mockLog(Interview(userAnswers), exitResponse)(Right(true))

      val result = service.decide(userAnswers, onwardRoute)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.ResultController.onPageLoad().url)
    }

    "handle 400 errors" in {

      mockDecide(Interview(userAnswers))(Left(ErrorResponse(400, "Bad")))
      mockInternalServerError(Html("Error page"))

      val result = service.decide(userAnswers, onwardRoute)

      status(result) mustBe INTERNAL_SERVER_ERROR
      contentAsString(result) mustBe "Error page"
    }

    "handle 500 errors" in {

      
      mockDecide(Interview(userAnswers))(Left(ErrorResponse(500, "Internal error")))
      mockInternalServerError(Html("Error page"))

      val result = service.decide(userAnswers, onwardRoute)

      status(result) mustBe INTERNAL_SERVER_ERROR
      contentAsString(result) mustBe "Error page"
    }
  }
}
