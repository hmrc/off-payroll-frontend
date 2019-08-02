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
import pages.{CustomisePDFPage, _}
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.mvc.Call

@Singleton
class FinancialRiskNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private val routeMap:  Map[Page, UserAnswers => Call] = Map(
    CannotClaimAsExpensePage -> (_ => financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(NormalMode)),
    EquipmentExpensesPage -> (_ => financialRiskRoutes.VehicleController.onPageLoad(NormalMode)),
    VehiclePage -> (_ => financialRiskRoutes.MaterialsController.onPageLoad(NormalMode)),
    MaterialsPage -> (_ => financialRiskRoutes.OtherExpensesController.onPageLoad(NormalMode)),
    OtherExpensesPage -> (_ => financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(NormalMode)),
    HowWorkerIsPaidPage -> (_ => financialRiskRoutes.PutRightAtOwnCostController.onPageLoad(NormalMode)),
    PutRightAtOwnCostPage -> (_ => partParcelRoutes.BenefitsController.onPageLoad(NormalMode))
  )

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode => routeMap.getOrElse(page, _ => IndexController.onPageLoad())
    case CheckMode => _ => CheckYourAnswersController.onPageLoad(Some(Section.financialRisk))
  }
}
