/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package navigation

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.routes._
import controllers.sections.partParcel.{routes => partParcelRoutes}
import javax.inject.{Inject, Singleton}
import models._
import pages._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import play.api.mvc.Call

@Singleton
class PartAndParcelNavigator @Inject()(businessOnOwnAccountNavigator: BusinessOnOwnAccountNavigator,
                                        implicit val appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private val routeMap:  Map[Page, UserAnswers => Call] = Map(
    BenefitsPage -> (_ => partParcelRoutes.LineManagerDutiesController.onPageLoad(NormalMode)),
    LineManagerDutiesPage -> (_ => partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode)),
    IdentifyToStakeholdersPage -> (answers => nextSection(answers))
  )

  private def nextSection(userAnswers: UserAnswers) = businessOnOwnAccountNavigator.startPage(userAnswers)

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode => routeMap.getOrElse(page, _ => IndexController.onPageLoad())
    case CheckMode => _ => CheckYourAnswersController.onPageLoad(Some(Section.partAndParcel))
  }
}
