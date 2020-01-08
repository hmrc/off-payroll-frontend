/*
 * Copyright 2020 HM Revenue & Customs
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
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.personalService.{routes => personalServiceRoutes}
import javax.inject.{Inject, Singleton}
import models._
import models.sections.personalService.ArrangedSubstitute.{YesClientAgreed, YesClientNotAgreed}
import pages._
import pages.sections.personalService._
import pages.sections.setup._
import play.api.mvc.Call

//noinspection ScalaStyle
@Singleton
class PersonalServiceNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private def routeMap(implicit mode: Mode): Map[Page, UserAnswers => Call] = Map(
    ArrangedSubstitutePage -> (answers =>
      answers.get(ArrangedSubstitutePage) match {
        case Some(YesClientAgreed) => personalServiceRoutes.DidPaySubstituteController.onPageLoad(mode)
        case Some(YesClientNotAgreed) => personalServiceRoutes.NeededToPayHelperController.onPageLoad(mode)
        case _ => personalServiceRoutes.RejectSubstituteController.onPageLoad(mode)
      }),
    DidPaySubstitutePage -> (answers =>
      answers.get(DidPaySubstitutePage) match {
        case Some(true) => routeToNextSection
        case _ => personalServiceRoutes.NeededToPayHelperController.onPageLoad(mode)
      }),
    RejectSubstitutePage -> (answers =>
      (answers.get(ContractStartedPage), answers.get(RejectSubstitutePage)) match {
        case (Some(true), Some(true)) => personalServiceRoutes.NeededToPayHelperController.onPageLoad(mode)
        case (_, Some(false)) => personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(mode)
        case _ => routeToNextSection
      }),
    WouldWorkerPaySubstitutePage -> (answers =>
      (answers.get(ContractStartedPage), answers.get(WouldWorkerPaySubstitutePage)) match {
        case (Some(true), x) if x.contains(false) => personalServiceRoutes.NeededToPayHelperController.onPageLoad(mode)
        case _ => routeToNextSection
      }),
    NeededToPayHelperPage -> (_ => routeToNextSection)
  )

  private def routeToNextSection(implicit mode: Mode): Call = mode match {
    case NormalMode => controlRoutes.MoveWorkerController.onPageLoad(NormalMode)
    case CheckMode => CheckYourAnswersController.onPageLoad(Some(Section.personalService))
  }

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call =
    routeMap(mode).getOrElse(page, _ => IndexController.onPageLoad())
}
