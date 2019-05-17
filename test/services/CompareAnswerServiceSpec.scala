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
import connectors.DecisionConnector
import forms.DeclarationFormProvider
import handlers.ErrorHandler
import models.AboutYouAnswer.{Agency, Worker}
import models.ArrangedSubstitute.{No, YesClientAgreed}
import models.CannotClaimAsExpense.{WorkerHadOtherExpenses, WorkerUsedVehicle}
import models.ChooseWhereWork.WorkerAgreeWithOthers
import models.HowWorkIsDone.WorkerFollowStrictEmployeeProcedures
import models.HowWorkerIsPaid.Commission
import models.IdentifyToStakeholders.WorkAsIndependent
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.CannotBeCorrected
import models.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.WorkerType.{LimitedCompany, SoleTrader}
import models._
import models.requests.DataRequest
import org.mockito.Matchers._
import org.mockito.Mockito.when
import pages._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{AboutYouPage, ContractStartedPage, WorkerTypePage}
import play.api.mvc.{AnyContent, Call}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, redirectLocation, _}
import play.twirl.api.Html
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.AnswerSection
import views.html.results._

import scala.concurrent.Future

class CompareAnswerServiceSpec extends SpecBase {

  val service = CompareAnswerService

  "compare answer service (consecutive answer)" should {

    "add an About You Answer" in {

      val userAnswers: UserAnswers = UserAnswers("id")

      val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

      val answers = service.constructAnswers(DataRequest(request,"id",userAnswers),AboutYouAnswer.Worker,AboutYouPage)

      val result = answers.get(AboutYouPage).get

      result.answer mustBe Worker
      result.answerNumber mustBe 0
    }

    "add a Contract Started Answer" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)

      val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

      val answers = service.constructAnswers(DataRequest(request,"id",userAnswers),true,ContractStartedPage)

      val result = answers.get(ContractStartedPage).get

      result.answer mustBe true
      result.answerNumber mustBe 1
    }

    "add a Worker Type Answer" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(ContractStartedPage,1, true)

      val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

      val answers = service.constructAnswers(DataRequest(request,"id",userAnswers),WorkerType.SoleTrader,WorkerTypePage)

      val result = answers.get(WorkerTypePage).get

      result.answer mustBe WorkerType.SoleTrader
      result.answerNumber mustBe 2
    }
  }

  "compare answer service (change new answer)" should {
    "change an About You Answer if it's a new value" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)

      val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

      val answers = service.constructAnswers(DataRequest(request,"id",userAnswers),AboutYouAnswer.Agency,AboutYouPage)

      val result = answers.get(AboutYouPage).get

      result.answer mustBe Agency
      result.answerNumber mustBe 0
    }

    "change a Contract Started Answer if it's a new value" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(ContractStartedPage,1, true)

      val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

      val answers = service.constructAnswers(DataRequest(request,"id",userAnswers),false,ContractStartedPage)

      val result = answers.get(ContractStartedPage).get

      result.answer mustBe false
      result.answerNumber mustBe 1
    }

    "change all answers after current answer if it's changed to a new value" in {

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

      val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

      val answers = service.constructAnswers(DataRequest(request,"id",userAnswers),false,ContractStartedPage)

      val result = answers.get(ContractStartedPage).get

      result.answer mustBe false
      result.answerNumber mustBe 1
      answers.size mustBe 2
      answers.get(OfficeHolderPage) mustBe None
      answers.get(IdentifyToStakeholdersPage) mustBe None
    }
  }

  "compare answer service (change same answer)" should {
    "not change an About You Answer if it's the same value" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)

      val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

      val answers = service.constructAnswers(DataRequest(request,"id",userAnswers),AboutYouAnswer.Worker,AboutYouPage)

      val result = answers.get(AboutYouPage).get

      result.answer mustBe Worker
      result.answerNumber mustBe 0
    }

    "not change a Contract Started Answer if it's the same value" in {

      val userAnswers: UserAnswers = UserAnswers("id")
        .set(AboutYouPage,0, Worker)
        .set(ContractStartedPage,1, true)

      val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

      val answers = service.constructAnswers(DataRequest(request,"id",userAnswers),true,ContractStartedPage)

      val result = answers.get(ContractStartedPage).get

      result.answer mustBe true
      result.answerNumber mustBe 1
    }
  }

  }
