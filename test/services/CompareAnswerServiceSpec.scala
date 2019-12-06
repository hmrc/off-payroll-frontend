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

import base.GuiceAppSpecBase
import connectors.mocks.MockDataCacheConnector
import models._
import models.requests.DataRequest
import models.sections.control.ChooseWhereWork.WorkerAgreeWithOthers
import models.sections.control.HowWorkIsDone.WorkerFollowStrictEmployeeProcedures
import models.sections.control.MoveWorker.CanMoveWorkerWithPermission
import models.sections.control.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.sections.financialRisk.CannotClaimAsExpense.{WorkerHadOtherExpenses, WorkerUsedVehicle}
import models.sections.financialRisk.HowWorkerIsPaid.Commission
import models.sections.financialRisk.PutRightAtOwnCost.CannotBeCorrected
import models.sections.partAndParcel.IdentifyToStakeholders.WorkAsIndependent
import models.sections.personalService.ArrangedSubstitute.{No, YesClientAgreed}
import models.sections.setup.AboutYouAnswer.Worker
import models.sections.setup.{AboutYouAnswer, WhatDoYouWantToDo, WhoAreYou, WorkerType}
import navigation.mocks.MockQuestionDeletionLookup
import org.scalamock.scalatest.MockFactory
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.mvc.AnyContent
import play.api.test.FakeRequest

class CompareAnswerServiceSpec extends GuiceAppSpecBase with MockFactory with MockDataCacheConnector with MockQuestionDeletionLookup {

  val service = new CompareAnswerService(mockQuestionDeletionLookup)

  "optimised" should {

    "optimised compare answer service" should {

      "remove answers if an answer is added" in {

        val userAnswers: UserAnswers = UserAnswers("id")
          .set(WouldWorkerPaySubstitutePage, true)
          .set(RejectSubstitutePage, true)
          .set(NeededToPayHelperPage, true)

        mockGetPagesToRemove(ArrangedSubstitutePage)(List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, NeededToPayHelperPage))

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), YesClientAgreed, ArrangedSubstitutePage)

        val resultFirst = answers.get(WouldWorkerPaySubstitutePage)
        val resultSecond = answers.get(RejectSubstitutePage)
        val resultThird = answers.get(NeededToPayHelperPage)

        resultFirst mustBe None
        resultSecond mustBe None
        resultThird mustBe None

        val newPage = answers.get(ArrangedSubstitutePage)
        newPage mustBe Some(YesClientAgreed)
      }

      "remove answers if an answer is changed" in {
        val userAnswers: UserAnswers = UserAnswers("id")
          .set(ArrangedSubstitutePage, No)
          .set(WouldWorkerPaySubstitutePage, true)
          .set(RejectSubstitutePage, true)
          .set(NeededToPayHelperPage, true)


        mockGetPagesToRemove(ArrangedSubstitutePage)(List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, NeededToPayHelperPage))

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), YesClientAgreed, ArrangedSubstitutePage)

        val resultFirst = answers.get(WouldWorkerPaySubstitutePage)
        val resultSecond = answers.get(RejectSubstitutePage)
        val resultThird = answers.get(NeededToPayHelperPage)

        resultFirst mustBe None
        resultSecond mustBe None
        resultThird mustBe None

        val newPage = answers.get(ArrangedSubstitutePage)
        newPage mustBe Some(YesClientAgreed)
      }

      "return same answers if an answer is not changed" in {
        val userAnswers: UserAnswers = UserAnswers("id")
          .set(ArrangedSubstitutePage, No)
          .set(WouldWorkerPaySubstitutePage, true)
          .set(RejectSubstitutePage, true)
          .set(NeededToPayHelperPage, true)

        val request: FakeRequest[AnyContent] = fakeRequest.withFormUrlEncodedBody()

        val answers = service.constructAnswers(DataRequest(request, "id", userAnswers), No, ArrangedSubstitutePage)

        val resultFirst = answers.get(WouldWorkerPaySubstitutePage)
        val resultSecond = answers.get(RejectSubstitutePage)
        val resultThird = answers.get(NeededToPayHelperPage)

        resultFirst mustBe Some(true)
        resultSecond mustBe Some(true)
        resultThird mustBe Some(true)

        val newPage = answers.get(ArrangedSubstitutePage)
        newPage mustBe Some(No)
      }
    }
  }
}
