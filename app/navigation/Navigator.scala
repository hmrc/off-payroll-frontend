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

package navigation

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import controllers.routes
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import controllers.sections.personalService.{routes => personalServiceRoutes}
import controllers.sections.setup.{routes => setupRoutes}
import javax.inject.{Inject, Singleton}
import models.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import pages.{CustomisePDFPage, _}
import models.WhichDescribesYouAnswer.{Agency, _}
import models._
import pages._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.mvc.Call

@Singleton
class Navigator @Inject()(implicit appConfig: FrontendAppConfig) extends FeatureSwitching {

  private val isWorker: UserAnswers => Boolean = _.get(WhichDescribesYouPage) match {
    case Some(Answers(WorkerPAYE, _)) => true
    case Some(Answers(WorkerIR35, _)) => true
    case Some(Answers(ClientPAYE, _)) => false
    case Some(Answers(ClientIR35, _)) => false
    case _ => true
  }

  private def isSmallBusiness(answers: UserAnswers): Boolean =
    (answers.get(TurnoverOverPage), answers.get(EmployeesOverPage), answers.get(BalanceSheetOverPage)) match {
      case (Some(Answers(false, _)), Some(Answers(false, _)), _) => true
      case (_, Some(Answers(false, _)), Some(Answers(false, _))) => true
      case (Some(Answers(false, _)),_ , Some(Answers(false, _))) => true
      case _ => false
    }

  private def businessSizeNextPage(answers: UserAnswers)(implicit mode: Mode): Call = (isSmallBusiness(answers), isWorker(answers)) match {
    case (true, false) => setupRoutes.ToolNotNeededController.onPageLoad()
    case (_, false) => setupRoutes.HirerAdvisoryController.onPageLoad()
    case (_, true) => setupRoutes.ContractStartedController.onPageLoad(mode)
  }

  private def personalServiceNextPage(implicit mode: Mode): Call = mode match {
    case NormalMode => controlRoutes.MoveWorkerController.onPageLoad(NormalMode)
    case CheckMode => routes.CheckYourAnswersController.onPageLoad()
  }

  private def optimisedIndexToWorkerUsingIntermediary(implicit mode: Mode): Map[Page, UserAnswers => Call] = Map(
    //Initialisation Section
    IndexPage -> (_ => setupRoutes.AboutYourResultController.onPageLoad()),

    //Setup Section
    AboutYourResultPage -> (_ => setupRoutes.AboutYouController.onPageLoad(mode)),
    WhichDescribesYouPage -> (answers => answers.get(WhichDescribesYouPage) match {
      case Some(Answers(Agency, _)) => setupRoutes.AgencyAdvisoryController.onPageLoad()
      case _ => setupRoutes.WorkerTypeController.onPageLoad(mode)
    }),
    AgencyAdvisoryPage -> (_ => setupRoutes.WorkerTypeController.onPageLoad(mode)),
    WorkerUsingIntermediaryPage -> (answers => answers.get(WorkerUsingIntermediaryPage) match {
      case Some(Answers(true, _)) => setupRoutes.IsWorkForPrivateSectorController.onPageLoad(mode)
      case _ => setupRoutes.ContractStartedController.onPageLoad(mode)
    })
  )

  private def optimisedPrivateSectorToContractStarted(implicit mode: Mode): Map[Page, UserAnswers => Call] = Map(
    IsWorkForPrivateSectorPage -> (answers => {
      (answers.get(IsWorkForPrivateSectorPage), isWorker(answers)) match {
        case (Some(Answers(true, _)), _) => setupRoutes.TurnoverOverController.onPageLoad(mode)
        case (Some(Answers(false, _)), true) => setupRoutes.WorkerAdvisoryController.onPageLoad()
        case (Some(Answers(false, _)), false) => setupRoutes.ContractStartedController.onPageLoad(mode)
        case (_, _) => setupRoutes.IsWorkForPrivateSectorController.onPageLoad(mode)
      }
    }),
    TurnoverOverPage -> (_ => setupRoutes.EmployeesOverController.onPageLoad(mode)),
    EmployeesOverPage -> (answers => {
      (answers.get(TurnoverOverPage), answers.get(EmployeesOverPage)) match {
        case (Some(Answers(true, _)), Some(Answers(true, _))) => businessSizeNextPage(answers)
        case (Some(Answers(false, _)), Some(Answers(false, _))) => businessSizeNextPage(answers)
        case _ => setupRoutes.BalanceSheetOverController.onPageLoad(mode)
      }
    }),
    BalanceSheetOverPage -> businessSizeNextPage,
    WorkerAdvisoryPage -> (_ => setupRoutes.ContractStartedController.onPageLoad(mode)),
    HirerAdvisoryPage -> (_ => setupRoutes.ContractStartedController.onPageLoad(mode)),
    ContractStartedPage -> (_ => exitRoutes.OfficeHolderController.onPageLoad(mode))
  )

  private def optimisedSetupRouteMap(implicit mode: Mode): Map[Page, UserAnswers => Call] = {
    optimisedIndexToWorkerUsingIntermediary ++ optimisedPrivateSectorToContractStarted
  }

  private val setupRouteMap: Map[Page, UserAnswers => Call] = Map(

    //Initialisation Section
    IndexPage -> (_ => setupRoutes.AboutYouController.onPageLoad(NormalMode)),

    //Setup Section
    AboutYouPage -> (_ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)),
    ContractStartedPage -> (_ => setupRoutes.WorkerTypeController.onPageLoad(NormalMode)),
    WorkerTypePage -> (_ => exitRoutes.OfficeHolderController.onPageLoad(NormalMode))
  )

  private def officeHolderRouteMap(mode: Mode):  Map[Page, UserAnswers => Call] = Map(
    //Early Exit Section
    OfficeHolderPage -> (answers => answers.get(ContractStartedPage) match {
      case Some(Answers(true,_)) => personalServiceRoutes.ArrangedSubstituteController.onPageLoad(mode)
      case Some(_) => personalServiceRoutes.RejectSubstituteController.onPageLoad(mode)
      case _ => setupRoutes.ContractStartedController.onPageLoad(mode)
    })
  )

  private def arrangedSubstituteToDidPaySubstitute(implicit mode: Mode):  Map[Page, UserAnswers => Call] = Map(
    ArrangedSubstitutePage -> (answers =>
      answers.get(ArrangedSubstitutePage) match {
        case Some(Answers(YesClientAgreed,_)) => personalServiceRoutes.DidPaySubstituteController.onPageLoad(mode)
        case Some(Answers(YesClientNotAgreed,_)) => personalServiceRoutes.NeededToPayHelperController.onPageLoad(mode)
        case Some(Answers(No,_)) => personalServiceRoutes.RejectSubstituteController.onPageLoad(mode)
        case _ => personalServiceRoutes.ArrangedSubstituteController.onPageLoad(mode)
      }),
    DidPaySubstitutePage -> (answers =>
      answers.get(DidPaySubstitutePage) match {
        case Some(Answers(true,_)) => personalServiceNextPage
        case _ => personalServiceRoutes.NeededToPayHelperController.onPageLoad(mode)
      })
  )

  private def rejectSubstituteRouting(implicit mode: Mode):  Map[Page, UserAnswers => Call] = Map(
    RejectSubstitutePage -> (answers =>
      (answers.get(ContractStartedPage), answers.get(RejectSubstitutePage)) match {
        case (Some(Answers(true,_)), Some(Answers(true,_))) => personalServiceRoutes.NeededToPayHelperController.onPageLoad(mode)
        case (_, Some(Answers(false,_))) => personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(mode)
        case (_, Some(Answers(true,_))) => personalServiceNextPage
        case (None, _) => setupRoutes.ContractStartedController.onPageLoad(mode)
        case (_, None) => personalServiceRoutes.RejectSubstituteController.onPageLoad(mode)
      })
  )

  private def wouldWorkerPaySubstituteRouting(implicit mode: Mode):  Map[Page, UserAnswers => Call] = Map(
    WouldWorkerPaySubstitutePage -> (answers =>
      (answers.get(ContractStartedPage), answers.get(WouldWorkerPaySubstitutePage)) match {
        case (Some(Answers(true, _)), _) if !isEnabled(OptimisedFlow) =>
          personalServiceRoutes.NeededToPayHelperController.onPageLoad(mode)
        case (Some(Answers(false, _)), _) => personalServiceNextPage
        case (Some(Answers(true, _)), Some(Answers(false, _))) => personalServiceRoutes.NeededToPayHelperController.onPageLoad(mode)
        case (Some(Answers(true, _)), Some(Answers(true, _))) => personalServiceNextPage
        case _ => setupRoutes.ContractStartedController.onPageLoad(mode)
      }),
    NeededToPayHelperPage -> (_ => personalServiceNextPage)
  )

  private def personalServiceRouteMap(implicit mode: Mode): Map[Page, UserAnswers => Call] = {
    arrangedSubstituteToDidPaySubstitute ++
      rejectSubstituteRouting ++
      wouldWorkerPaySubstituteRouting
  }

  private val routeMap:  Map[Page, UserAnswers => Call] = Map(

    //Control Section
    MoveWorkerPage -> (_ => controlRoutes.HowWorkIsDoneController.onPageLoad(NormalMode)),
    HowWorkIsDonePage -> (_ => controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(NormalMode)),
    ScheduleOfWorkingHoursPage -> (_ => controlRoutes.ChooseWhereWorkController.onPageLoad(NormalMode)),
    ChooseWhereWorkPage -> (_ => {
      if(isEnabled(OptimisedFlow)) {
        financialRiskRoutes.EquipmentExpensesController.onPageLoad(NormalMode)
      } else {
        financialRiskRoutes.CannotClaimAsExpenseController.onPageLoad(NormalMode)
      }
    }),

    //Financial Risk Section
    CannotClaimAsExpensePage -> (_ => financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(NormalMode)),
    EquipmentExpensesPage -> (_ => financialRiskRoutes.VehicleController.onPageLoad(NormalMode)),
    VehiclePage -> (_ => financialRiskRoutes.MaterialsController.onPageLoad(NormalMode)),
    MaterialsPage -> (_ => financialRiskRoutes.OtherExpensesController.onPageLoad(NormalMode)),
    OtherExpensesPage -> (_ => financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(NormalMode)),
    HowWorkerIsPaidPage -> (_ => financialRiskRoutes.PutRightAtOwnCostController.onPageLoad(NormalMode)),
    PutRightAtOwnCostPage -> (_ => partParcelRoutes.BenefitsController.onPageLoad(NormalMode)),

    //Part and Parcel Section
    BenefitsPage -> (_ => partParcelRoutes.LineManagerDutiesController.onPageLoad(NormalMode)),
    LineManagerDutiesPage -> (_ =>
      if (isEnabled(OptimisedFlow)) {
        partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode)
      } else {
        partParcelRoutes.InteractWithStakeholdersController.onPageLoad(NormalMode)
      }),
    InteractWithStakeholdersPage -> { answer =>
      answer.get(InteractWithStakeholdersPage) match {
        case Some(Answers(true,_)) => partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode)
        case Some(ans) if !ans.answer && isEnabled(OptimisedFlow) => routes.CheckYourAnswersController.onPageLoad()
        case _ => routes.ResultController.onPageLoad()
      }},
    IdentifyToStakeholdersPage -> (_ =>
      if (isEnabled(OptimisedFlow)) {
        routes.CheckYourAnswersController.onPageLoad()
      } else {
        routes.ResultController.onPageLoad()
      }),

    //CYA/Results Page
    CheckYourAnswersPage -> (_ => routes.ResultController.onPageLoad()),
    ResultPage -> { answer =>

      if (isEnabled(OptimisedFlow)) {
        answer.get(ResultPage) match {
          case Some(Answers(true, _)) => routes.AddReferenceDetailsController.onPageLoad()
          case _ => routes.FinishedCheckingController.onPageLoad()
        }
      } else {
        routes.PDFController.onPageLoad(NormalMode)
      }
    },

    AddReferenceDetailsPage -> {
      answer =>
        answer.get(AddReferenceDetailsPage) match {
          case Some(Answers(true, _)) => routes.PDFController.onPageLoad(NormalMode)
          case _ => routes.FinishedCheckingController.onPageLoad()
        }
    },

    CustomisePDFPage -> (_ => routes.FinishedCheckingController.onPageLoad())

  )

  def nextPage(page: Page, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      val routing =
        (if (isEnabled(OptimisedFlow)) optimisedSetupRouteMap(NormalMode) else setupRouteMap) ++
          officeHolderRouteMap(NormalMode) ++
          personalServiceRouteMap(NormalMode) ++
          routeMap
      routing.getOrElse(page, _ => routes.IndexController.onPageLoad())
    case CheckMode =>
      val changeRouting =
        optimisedSetupRouteMap(CheckMode) ++
          officeHolderRouteMap(CheckMode) ++
          personalServiceRouteMap(CheckMode)
      changeRouting.getOrElse(page, _ => routes.CheckYourAnswersController.onPageLoad())
  }
}
