/*
 * Copyright 2021 HM Revenue & Customs
 *
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
