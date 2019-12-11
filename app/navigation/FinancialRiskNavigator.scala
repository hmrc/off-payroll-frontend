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
import config.featureSwitch.FeatureSwitching
import controllers.routes._
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import javax.inject.{Inject, Singleton}
import models._
import pages._
import pages.sections.financialRisk._
import play.api.mvc.Call

@Singleton
class FinancialRiskNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private val routeMap:  Map[Page, UserAnswers => Call] = Map(
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
