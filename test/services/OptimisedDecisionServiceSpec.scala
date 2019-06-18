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
import forms.DeclarationFormProvider
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
import views.html.results.inside._
import views.html.results.inside.officeHolder.{OfficeHolderAgentView, OfficeHolderIR35View, OfficeHolderPAYEView}
import views.html.results.outside.IR35OutsideView
import views.html.results.undetermined._
import views.html.subOptimised.results.{ControlView, SelfEmployedView}

class OptimisedDecisionServiceSpec extends SpecBase with MockDecisionConnector
  with MockDataCacheConnector with MockErrorHandler with FeatureSwitching with ScalaFutures {

  val formProvider = new DeclarationFormProvider()

  val service: OptimisedDecisionService = new OptimisedDecisionService(mockDecisionConnector, mockErrorHandler, formProvider,
    officeAgency = injector.instanceOf[OfficeHolderAgentView],
    officeIR35 = injector.instanceOf[OfficeHolderIR35View],
    officePAYE = injector.instanceOf[OfficeHolderPAYEView],
    undeterminedAgency = injector.instanceOf[AgentUndeterminedView],
    undeterminedIR35 = injector.instanceOf[IR35UndeterminedView],
    undeterminedPAYE = injector.instanceOf[PAYEUndeterminedView],
    insideAgent = injector.instanceOf[AgentInsideView],
    insideIR35 = injector.instanceOf[IR35InsideView],
    insidePAYE = injector.instanceOf[PAYEInsideView],
    outsideAgent = injector.instanceOf[IR35OutsideView], //TODO: Update with AgentOutsideView
    outsideIR35 = injector.instanceOf[IR35OutsideView],
    outsidePAYE = injector.instanceOf[IR35OutsideView], //TODO: Update with PAYEOutsideView
    frontendAppConfig)

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

    "give a valid result" when {

      "every decision call is successful" in {

        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview.apply(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))
        mockLog(Interview(userAnswers), DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35))(Right(true))


        whenReady(service.collateDecisions) { res =>
          res.right.get.result mustBe ResultEnum.INSIDE_IR35
        }
      }

      "decision log returns a Left" in {
        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview.apply(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers), Interview.writesControl)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers))(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
        mockLog(Interview(userAnswers),DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))(Left(ErrorResponse(500, "ma name Jeff")))

        whenReady(service.collateDecisions) { res =>
          res.right.get.result mustBe ResultEnum.INSIDE_IR35
        }
      }
    }


    "return an error" when {

      "personal service decision call returns a Left" in {

        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview.apply(userAnswers), Interview.writesPersonalService)(Left(ErrorResponse(500, "ma name Jeff")))

        whenReady(service.collateDecisions) { res =>
          res.left.get mustBe an[ErrorResponse]
        }
      }

      "control decision call returns a Left" in {
        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview.apply(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers), Interview.writesControl)(Left(ErrorResponse(500, "ma name Jeff")))

        whenReady(service.collateDecisions) { res =>
          res.left.get mustBe an[ErrorResponse]
        }
      }

      "financial risk decision call returns a Left" in {
        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview.apply(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers), Interview.writesControl)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers), Interview.writesFinancialRisk)(Left(ErrorResponse(500, "ma name Jeff")))

        whenReady(service.collateDecisions) { res =>
          res.left.get mustBe an[ErrorResponse]
        }
      }


      "whole decision call returns a Left" in {
        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview.apply(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers), Interview.writesControl)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
        mockDecide(Interview.apply(userAnswers))(Left(ErrorResponse(500, "ma name Jeff")))

        whenReady(service.collateDecisions) { res =>
          res.left.get mustBe an[ErrorResponse]
        }
      }
    }
  }

}
