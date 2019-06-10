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
import config.featureSwitch.FeatureSwitching
import connectors.mocks.{MockDataCacheConnector, MockDecisionConnector}
import handlers.mocks.MockErrorHandler
import models.AboutYouAnswer.Worker
import models.ArrangedSubstitute.YesClientAgreed
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
import org.scalatest.concurrent.ScalaFutures
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.mvc.AnyContent
import MultiDecision.Result
import cats.data.EitherT

import scala.concurrent.Future

class OptimisedDecisionServiceSpec extends SpecBase with MockDecisionConnector
  with MockDataCacheConnector with MockErrorHandler with FeatureSwitching with ScalaFutures {

  val service: OptimisedDecisionService = new OptimisedDecisionService(mockDecisionConnector, mockErrorHandler,frontendAppConfig)

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

  "multipleDecisionCall" should {
    "call decision once if a decision is reached" in {
      implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

      mockDecideSection(Interview.apply(userAnswers))(createLeftType(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))))
      mockLog(Interview(userAnswers),DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))(Right(true))


      whenReady(service.multipleDecisionCall()) { res =>
       res.right.get.result mustBe ResultEnum.INSIDE_IR35
      }
    }

    "call decision twice if the first call is unmatched" in {
      implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createLeftType(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))))
      mockLog(Interview(userAnswers),DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))(Right(true))


      whenReady(service.multipleDecisionCall()) { res =>
        res.right.get.result mustBe ResultEnum.INSIDE_IR35
      }
    }

    "call decision thrice if the first 2 calls are unmatched" in {
      implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createLeftType(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))))

      mockLog(Interview(userAnswers),DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))(Right(true))


      whenReady(service.multipleDecisionCall()) { res =>
        res.right.get.result mustBe ResultEnum.INSIDE_IR35
      }
    }

    "call decision fourice if the first 3 calls are unmatched" in {
      implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createLeftType(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))))

      mockLog(Interview(userAnswers),DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))(Right(true))


      whenReady(service.multipleDecisionCall()) { res =>
        res.right.get.result mustBe ResultEnum.INSIDE_IR35
      }
    }

    "do the big decision if no matches" in {
      implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createLeftType(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))))

      mockLog(Interview(userAnswers),DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))(Right(true))


      whenReady(service.multipleDecisionCall()) { res =>
        res.right.get.result mustBe ResultEnum.INSIDE_IR35
      }
    }

    "return an error given no decision" in {
      implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))
      mockDecideSection(Interview.apply(userAnswers))(createRightType(true))

      whenReady(service.multipleDecisionCall()) { res =>
        res.left.get mustBe an[ErrorResponse]
      }
    }

    "return an error given an exception" in {
      implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

      mockDecideSection(Interview.apply(userAnswers))(createLeftType(Left(ErrorResponse(500,"ma name Jeff"))))

      whenReady(service.multipleDecisionCall()) { res =>
        res.left.get mustBe an[ErrorResponse]
      }
    }

  }

}
