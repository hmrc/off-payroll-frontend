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
import controllers.routes._
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import controllers.sections.personalService.{routes => personalServiceRoutes}
import controllers.sections.setup.{routes => setupRoutes}
import javax.inject.{Inject, Singleton}
import models.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import models.WhichDescribesYouAnswer.{Agency, _}
import models._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import pages.{CustomisePDFPage, _}
import play.api.mvc.Call

@Singleton
class SetupNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

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

  private def optimisedSetupRouteMap(implicit mode: Mode): Map[Page, UserAnswers => Call] = Map(
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
    }),
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

  private val setupRouteMap: Map[Page, UserAnswers => Call] = Map(

    //Initialisation Section
    IndexPage -> (_ => setupRoutes.AboutYouController.onPageLoad(NormalMode)),

    //Setup Section
    AboutYouPage -> (_ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)),
    ContractStartedPage -> (_ => setupRoutes.WorkerTypeController.onPageLoad(NormalMode)),
    WorkerTypePage -> (_ => exitRoutes.OfficeHolderController.onPageLoad(NormalMode))
  )

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      val routing = if (isEnabled(OptimisedFlow)) optimisedSetupRouteMap(NormalMode) else setupRouteMap
      routing.getOrElse(page, _ => IndexController.onPageLoad())
    case CheckMode =>
      val changeRouting = optimisedSetupRouteMap(CheckMode)
      changeRouting.getOrElse(page, _ => IndexController.onPageLoad())
  }
}
