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
import connectors.DataCacheConnector
import connectors.mocks.MockDataCacheConnector
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
import models.WorkerType.SoleTrader
import models._
import models.requests.DataRequest
import navigation.QuestionDeletionLookup
<<<<<<< HEAD
=======
import navigation.mocks.MockQuestionDeletionLookup
>>>>>>> 4249b723f1b45cea24b91ba865c413192a0b8500
import org.mockito.Matchers
import org.mockito.Mockito.when
import org.scalamock.scalatest.MockFactory
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{AboutYouPage, ContractStartedPage, WorkerTypePage}
import play.api.mvc.AnyContent
import play.api.test.FakeRequest
import services.mocks.MockCompareAnswerService

import scala.concurrent.Future

class CompareAnswerServiceSpec extends SpecBase with MockFactory with MockDataCacheConnector with MockQuestionDeletionLookup {

  val service = new CompareAnswerService(mockQuestionDeletionLookup)

  "sub-optimised" should {

    "compare answer service (consecutive answer)" should {

      "add an About You Answer" in {

        val userAnswers: UserAnswers = UserAnswers("id")

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), AboutYouAnswer.Worker, AboutYouPage)

        val result = answers.get(AboutYouPage).get

        result.answer mustBe Worker
        result.answerNumber mustBe 0
      }

      "add a Contract Started Answer" in {

        val userAnswers: UserAnswers = UserAnswers("id")
          .set(AboutYouPage, 0, Worker)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), true, ContractStartedPage)

        val result = answers.get(ContractStartedPage).get

        result.answer mustBe true
        result.answerNumber mustBe 1
      }

      "add a Worker Type Answer" in {

        val userAnswers: UserAnswers = UserAnswers("id")
          .set(AboutYouPage, 0, Worker)
          .set(ContractStartedPage, 1, true)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), WorkerType.SoleTrader, WorkerTypePage)

        val result = answers.get(WorkerTypePage).get

        result.answer mustBe WorkerType.SoleTrader
        result.answerNumber mustBe 2
      }
    }

    "compare answer service (change new answer)" should {
      "change an About You Answer if it's a new value" in {


        val userAnswers: UserAnswers = UserAnswers("id")
          .set(AboutYouPage, 0, Worker)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), AboutYouAnswer.Agency, AboutYouPage)

        val result = answers.get(AboutYouPage).get
<<<<<<< HEAD

        result.answer mustBe Agency
        result.answerNumber mustBe 0
      }

      "change a Contract Started Answer if it's a new value" in {


        val userAnswers: UserAnswers = UserAnswers("id")
          .set(AboutYouPage, 0, Worker)
          .set(ContractStartedPage, 1, true)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), false, ContractStartedPage)

        val result = answers.get(ContractStartedPage).get

        result.answer mustBe false
        result.answerNumber mustBe 1
      }

      "change all answers after current answer if it's changed to a new value" in {


        val userAnswers: UserAnswers = UserAnswers("id")
          .set(AboutYouPage, 0, Worker)
          .set(ContractStartedPage, 1, true)
          .set(WorkerTypePage, 2, SoleTrader)
          .set(OfficeHolderPage, 3, false)
          .set(ArrangedSubstitutePage, 4, YesClientAgreed)
          .set(DidPaySubstitutePage, 5, false)
          .set(WouldWorkerPaySubstitutePage, 6, true)
          .set(RejectSubstitutePage, 7, false)
          .set(NeededToPayHelperPage, 8, false)
          .set(MoveWorkerPage, 9, CanMoveWorkerWithPermission)
          .set(HowWorkIsDonePage, 10, WorkerFollowStrictEmployeeProcedures)
          .set(ScheduleOfWorkingHoursPage, 11, WorkerAgreeSchedule)
          .set(ChooseWhereWorkPage, 12, WorkerAgreeWithOthers)
          .set(CannotClaimAsExpensePage, 13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
          .set(HowWorkerIsPaidPage, 14, Commission)
          .set(PutRightAtOwnCostPage, 15, CannotBeCorrected)
          .set(BenefitsPage, 16, false)
          .set(LineManagerDutiesPage, 17, false)
          .set(InteractWithStakeholdersPage, 18, false)
          .set(IdentifyToStakeholdersPage, 19, WorkAsIndependent)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), false, ContractStartedPage)

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
          .set(AboutYouPage, 0, Worker)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), AboutYouAnswer.Worker, AboutYouPage)

        val result = answers.get(AboutYouPage).get

        result.answer mustBe Worker
        result.answerNumber mustBe 0
      }

      "not change a Contract Started Answer if it's the same value" in {


        val userAnswers: UserAnswers = UserAnswers("id")
          .set(AboutYouPage, 0, Worker)
          .set(ContractStartedPage, 1, true)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), true, ContractStartedPage)

        val result = answers.get(ContractStartedPage).get

        result.answer mustBe true
        result.answerNumber mustBe 1
      }
    }
  }

  "optimised" should {

    "optimised compare answer service" should {

      "remove answers if an answer is added" in {

        val userAnswers: UserAnswers = UserAnswers("id")
          .set(WouldWorkerPaySubstitutePage, 0, true)
          .set(RejectSubstitutePage, 1, true)
          .set(NeededToPayHelperPage, 2, true)

        (questionLookupMock.getPagesToRemove _).expects(ArrangedSubstitutePage)
          .returning(_ => List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, NeededToPayHelperPage))

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.optimisedConstructAnswers(DataRequest(request, "id", userAnswers), YesClientAgreed, ArrangedSubstitutePage)

        val resultFirst = answers.get(WouldWorkerPaySubstitutePage)
        val resultSecond = answers.get(RejectSubstitutePage)
        val resultThird = answers.get(NeededToPayHelperPage)

        resultFirst mustBe None
        resultSecond mustBe None
        resultThird mustBe None

        val newPage = answers.get(ArrangedSubstitutePage)
        newPage mustBe Some(Answers(YesClientAgreed, 0))
      }

      "remove answers if an answer is changed" in {
        val userAnswers: UserAnswers = UserAnswers("id")
          .set(ArrangedSubstitutePage, 0, No)
          .set(WouldWorkerPaySubstitutePage, 1, true)
          .set(RejectSubstitutePage, 2, true)
          .set(NeededToPayHelperPage, 3, true)

        (questionLookupMock.getPagesToRemove _).expects(ArrangedSubstitutePage)
          .returning(_ => List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, NeededToPayHelperPage))

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.optimisedConstructAnswers(DataRequest(request, "id", userAnswers), YesClientAgreed, ArrangedSubstitutePage)

        val resultFirst = answers.get(WouldWorkerPaySubstitutePage)
        val resultSecond = answers.get(RejectSubstitutePage)
        val resultThird = answers.get(NeededToPayHelperPage)

        resultFirst mustBe None
        resultSecond mustBe None
        resultThird mustBe None

        val newPage = answers.get(ArrangedSubstitutePage)
        newPage mustBe Some(Answers(YesClientAgreed, 0))
      }

      "return same answers if an answer is not changed" in {
        val userAnswers: UserAnswers = UserAnswers("id")
          .set(ArrangedSubstitutePage, 0, No)
          .set(WouldWorkerPaySubstitutePage, 0, true)
          .set(RejectSubstitutePage, 0, true)
          .set(NeededToPayHelperPage, 0, true)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.optimisedConstructAnswers(DataRequest(request, "id", userAnswers), No, ArrangedSubstitutePage)

        val resultFirst = answers.get(WouldWorkerPaySubstitutePage)
        val resultSecond = answers.get(RejectSubstitutePage)
        val resultThird = answers.get(NeededToPayHelperPage)

        resultFirst mustBe Some(Answers(true, 0))
        resultSecond mustBe Some(Answers(true, 0))
        resultThird mustBe Some(Answers(true, 0))

        val newPage = answers.get(ArrangedSubstitutePage)
        newPage mustBe Some(Answers(No, 0))
=======

        result.answer mustBe Agency
        result.answerNumber mustBe 0
      }

      "change a Contract Started Answer if it's a new value" in {


        val userAnswers: UserAnswers = UserAnswers("id")
          .set(AboutYouPage, 0, Worker)
          .set(ContractStartedPage, 1, true)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), false, ContractStartedPage)

        val result = answers.get(ContractStartedPage).get

        result.answer mustBe false
        result.answerNumber mustBe 1
      }

      "change all answers after current answer if it's changed to a new value" in {


        val userAnswers: UserAnswers = UserAnswers("id")
          .set(AboutYouPage, 0, Worker)
          .set(ContractStartedPage, 1, true)
          .set(WorkerTypePage, 2, SoleTrader)
          .set(OfficeHolderPage, 3, false)
          .set(ArrangedSubstitutePage, 4, YesClientAgreed)
          .set(DidPaySubstitutePage, 5, false)
          .set(WouldWorkerPaySubstitutePage, 6, true)
          .set(RejectSubstitutePage, 7, false)
          .set(NeededToPayHelperPage, 8, false)
          .set(MoveWorkerPage, 9, CanMoveWorkerWithPermission)
          .set(HowWorkIsDonePage, 10, WorkerFollowStrictEmployeeProcedures)
          .set(ScheduleOfWorkingHoursPage, 11, WorkerAgreeSchedule)
          .set(ChooseWhereWorkPage, 12, WorkerAgreeWithOthers)
          .set(CannotClaimAsExpensePage, 13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
          .set(HowWorkerIsPaidPage, 14, Commission)
          .set(PutRightAtOwnCostPage, 15, CannotBeCorrected)
          .set(BenefitsPage, 16, false)
          .set(LineManagerDutiesPage, 17, false)
          .set(InteractWithStakeholdersPage, 18, false)
          .set(IdentifyToStakeholdersPage, 19, WorkAsIndependent)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), false, ContractStartedPage)

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
          .set(AboutYouPage, 0, Worker)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), AboutYouAnswer.Worker, AboutYouPage)

        val result = answers.get(AboutYouPage).get

        result.answer mustBe Worker
        result.answerNumber mustBe 0
      }

      "not change a Contract Started Answer if it's the same value" in {


        val userAnswers: UserAnswers = UserAnswers("id")
          .set(AboutYouPage, 0, Worker)
          .set(ContractStartedPage, 1, true)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), true, ContractStartedPage)

        val result = answers.get(ContractStartedPage).get

        result.answer mustBe true
        result.answerNumber mustBe 1
>>>>>>> 4249b723f1b45cea24b91ba865c413192a0b8500
      }
    }
  }

  "optimised" should {

    "optimised compare answer service" should {

      "remove answers if an answer is added" in {

        val userAnswers: UserAnswers = UserAnswers("id")
          .set(WouldWorkerPaySubstitutePage, 0, true)
          .set(RejectSubstitutePage, 1, true)
          .set(NeededToPayHelperPage, 2, true)

        mockGetPagesToRemove(ArrangedSubstitutePage)(List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, NeededToPayHelperPage))

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.optimisedConstructAnswers(DataRequest(request, "id", userAnswers), YesClientAgreed, ArrangedSubstitutePage)

        val resultFirst = answers.get(WouldWorkerPaySubstitutePage)
        val resultSecond = answers.get(RejectSubstitutePage)
        val resultThird = answers.get(NeededToPayHelperPage)

        resultFirst mustBe None
        resultSecond mustBe None
        resultThird mustBe None

        val newPage = answers.get(ArrangedSubstitutePage)
        newPage mustBe Some(Answers(YesClientAgreed, 0))
      }

      "remove answers if an answer is changed" in {
        val userAnswers: UserAnswers = UserAnswers("id")
          .set(ArrangedSubstitutePage, 0, No)
          .set(WouldWorkerPaySubstitutePage, 1, true)
          .set(RejectSubstitutePage, 2, true)
          .set(NeededToPayHelperPage, 3, true)


        mockGetPagesToRemove(ArrangedSubstitutePage)(List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, NeededToPayHelperPage))

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.optimisedConstructAnswers(DataRequest(request, "id", userAnswers), YesClientAgreed, ArrangedSubstitutePage)

        val resultFirst = answers.get(WouldWorkerPaySubstitutePage)
        val resultSecond = answers.get(RejectSubstitutePage)
        val resultThird = answers.get(NeededToPayHelperPage)

        resultFirst mustBe None
        resultSecond mustBe None
        resultThird mustBe None

        val newPage = answers.get(ArrangedSubstitutePage)
        newPage mustBe Some(Answers(YesClientAgreed, 0))
      }

      "return same answers if an answer is not changed" in {
        val userAnswers: UserAnswers = UserAnswers("id")
          .set(ArrangedSubstitutePage, 0, No)
          .set(WouldWorkerPaySubstitutePage, 0, true)
          .set(RejectSubstitutePage, 0, true)
          .set(NeededToPayHelperPage, 0, true)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.optimisedConstructAnswers(DataRequest(request, "id", userAnswers), No, ArrangedSubstitutePage)

        val resultFirst = answers.get(WouldWorkerPaySubstitutePage)
        val resultSecond = answers.get(RejectSubstitutePage)
        val resultThird = answers.get(NeededToPayHelperPage)

        resultFirst mustBe Some(Answers(true, 0))
        resultSecond mustBe Some(Answers(true, 0))
        resultThird mustBe Some(Answers(true, 0))

        val newPage = answers.get(ArrangedSubstitutePage)
        newPage mustBe Some(Answers(No, 0))
      }
    }
  }
}
