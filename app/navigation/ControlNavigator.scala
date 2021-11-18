/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package navigation

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.routes._
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import javax.inject.{Inject, Singleton}
import models._
import pages._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import play.api.mvc.Call

@Singleton
class ControlNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private val routeMap:  Map[Page, UserAnswers => Call] = Map(
    MoveWorkerPage -> (_ => controlRoutes.HowWorkIsDoneController.onPageLoad(NormalMode)),
    HowWorkIsDonePage -> (_ => controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(NormalMode)),
    ScheduleOfWorkingHoursPage -> (_ => controlRoutes.ChooseWhereWorkController.onPageLoad(NormalMode)),
    ChooseWhereWorkPage -> (_ => financialRiskRoutes.EquipmentExpensesController.onPageLoad(NormalMode))
  )

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode => routeMap.getOrElse(page, _ => IndexController.onPageLoad)
    case CheckMode => _ => CheckYourAnswersController.onPageLoad(Some(Section.control))


  }
}
