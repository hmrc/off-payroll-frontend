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

//noinspection ScalaStyle
@Singleton
class SetupNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private def isSmallBusiness(answers: UserAnswers): Boolean =
    (answers.getAnswer(TurnoverOverPage), answers.getAnswer(EmployeesOverPage), answers.getAnswer(BalanceSheetOverPage)) match {
      case (Some(false), Some(false), _) => true
      case (_, Some(false), Some(false)) => true
      case (Some(false), _, Some(false)) => true
      case _ => false
    }

  private def businessSizeNextPage(answers: UserAnswers): Call = (isSmallBusiness(answers), isWorker(answers)) match {
    case (true, false) => setupRoutes.ToolNotNeededController.onPageLoad()
    case (_, false) => setupRoutes.HirerAdvisoryController.onPageLoad()
    case (_, true) => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
  }

  private val optimisedSetupRouteMap: Map[Page, UserAnswers => Call] = Map(
    //Initialisation Section
    IndexPage -> (_ => setupRoutes.AboutYourResultController.onPageLoad()),

    //Setup Section
    AboutYourResultPage -> (_ => setupRoutes.AboutYouController.onPageLoad(NormalMode)),
    WhichDescribesYouPage -> (answers => answers.getAnswer(WhichDescribesYouPage) match {
      case Some(Agency) => setupRoutes.AgencyAdvisoryController.onPageLoad()
      case _ => setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
    }),
    AgencyAdvisoryPage -> (_ => setupRoutes.WorkerTypeController.onPageLoad(NormalMode)),
    WorkerUsingIntermediaryPage -> (answers => answers.getAnswer(WorkerUsingIntermediaryPage) match {
      case Some(true) => setupRoutes.IsWorkForPrivateSectorController.onPageLoad(NormalMode)
      case _ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
    }),
    IsWorkForPrivateSectorPage -> (answers => {
      (answers.getAnswer(IsWorkForPrivateSectorPage), isWorker(answers)) match {
        case (Some(true), _) => setupRoutes.TurnoverOverController.onPageLoad(NormalMode)
        case (Some(false), true) => setupRoutes.WorkerAdvisoryController.onPageLoad()
        case (_, _) => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      }
    }),
    TurnoverOverPage -> (_ => setupRoutes.EmployeesOverController.onPageLoad(NormalMode)),
    EmployeesOverPage -> (answers => {
      (answers.getAnswer(TurnoverOverPage), answers.getAnswer(EmployeesOverPage)) match {
        case (Some(a), Some(b)) if a == b => businessSizeNextPage(answers)
        case _ => setupRoutes.BalanceSheetOverController.onPageLoad(NormalMode)
      }
    }),
    BalanceSheetOverPage -> businessSizeNextPage,
    WorkerAdvisoryPage -> (_ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)),
    HirerAdvisoryPage -> (_ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)),
    ContractStartedPage -> (_ => exitRoutes.OfficeHolderController.onPageLoad(NormalMode))
  )

  private val setupRouteMap: Map[Page, UserAnswers => Call] = Map(

    //Initialisation Section
    IndexPage -> (_ => setupRoutes.AboutYouController.onPageLoad(NormalMode)),

    //Setup Section
    AboutYouPage -> (_ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)),
    ContractStartedPage -> (_ => setupRoutes.WorkerTypeController.onPageLoad(NormalMode)),
    WorkerTypePage -> (_ => exitRoutes.OfficeHolderController.onPageLoad(NormalMode))
  )

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = {
    val routing = if (isEnabled(OptimisedFlow)) optimisedSetupRouteMap else setupRouteMap
    routing.getOrElse(page, _ => IndexController.onPageLoad())
  }
}
