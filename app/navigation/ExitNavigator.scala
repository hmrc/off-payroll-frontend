/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package navigation

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.routes._
import controllers.sections.personalService.{routes => personalServiceRoutes}
import controllers.sections.setup.{routes => setupRoutes}
import javax.inject.{Inject, Singleton}
import models._
import pages._
import pages.sections.exit.OfficeHolderPage
import pages.sections.setup._
import play.api.mvc.Call

@Singleton
class ExitNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  def officeHolderRouteMap(mode: Mode): Map[Page, UserAnswers => Call] = {
    Map(OfficeHolderPage -> (answers =>
      if(answers.get(OfficeHolderPage).contains(true)) {
        ResultController.onPageLoad()
      } else {
        mode match {
          case CheckMode => CheckYourAnswersController.onPageLoad(Some(Section.earlyExit))
          case NormalMode => answers.get(ContractStartedPage) match {
                case Some(true) => personalServiceRoutes.ArrangedSubstituteController.onPageLoad(NormalMode)
                case Some(_) => personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode)
                case _ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
              }
        }
      }))
  }

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = {
    officeHolderRouteMap(mode).getOrElse(page, _ => IndexController.onPageLoad)
  }
}
