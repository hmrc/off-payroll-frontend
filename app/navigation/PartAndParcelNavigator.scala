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

import javax.inject.{Inject, Singleton}

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.routes._
import controllers.sections.partParcel.{routes => partParcelRoutes}
import models._
import pages._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import play.api.mvc.Call

@Singleton
class PartAndParcelNavigator @Inject()(businessOnOwnAccountNavigator: BusinessOnOwnAccountNavigator,
                                        implicit val appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private val routeMap:  Map[Page, UserAnswers => Call] = Map(
    BenefitsPage -> (_ => partParcelRoutes.LineManagerDutiesController.onPageLoad(NormalMode)),
    LineManagerDutiesPage -> (_ => partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode)),
    InteractWithStakeholdersPage -> { answer =>
      answer.getAnswer(InteractWithStakeholdersPage) match {
        case Some(true) => partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode)
        case _ => nextSection(answer)
      }},
    IdentifyToStakeholdersPage -> (answers => nextSection(answers))
  )

  private def nextSection(userAnswers: UserAnswers) = businessOnOwnAccountNavigator.startPage(userAnswers)

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode => routeMap.getOrElse(page, _ => IndexController.onPageLoad())
    case CheckMode => _ => CheckYourAnswersController.onPageLoad(Some(Section.partAndParcel))
  }
}
