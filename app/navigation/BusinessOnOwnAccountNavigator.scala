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
import javax.inject.{Inject, Singleton}
import models._
import pages._
import pages.sections.businessOnOwnAccount.FirstContractPage
import play.api.mvc.Call
import controllers.sections.businessOnOwnAccount.{routes => booaRoutes}


@Singleton
class BusinessOnOwnAccountNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private val routeMap:  Map[Page, UserAnswers => Call] = Map(

    FirstContractPage -> (_ => booaRoutes.MultipleContractsController.onPageLoad(NormalMode)), //todo one of these are the wrong page

    MultipleContractsPage -> (answer =>
      answer.getAnswer(MultipleContractsPage) match {
        case Some(true) => booaRoutes.PermissionToWorkWithOthersController.onPageLoad(NormalMode)
        case _ => booaRoutes.RightsOfWorkController.onPageLoad(NormalMode)
      }
    ),

    PermissionToWorkWithOthersPage -> (_ => booaRoutes.RightsOfWorkController.onPageLoad(NormalMode)),

    RightsOfWorkPage -> (answer =>
      (answer.getAnswer(RightsOfWorkPage), answer.getAnswer(FirstContractPage)) match {
        case (Some(RightsOfWork.No), _) => booaRoutes.TransferOfRightsController.onPageLoad(NormalMode)
        case (_, Some(false)) => booaRoutes.PreviousContractController.onPageLoad(NormalMode)
        case (_, Some(true)) => booaRoutes.FirstContractController.onPageLoad(NormalMode)  //todo one of these are the wrong page
      }
    )
  )

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode => routeMap.getOrElse(page, _ => IndexController.onPageLoad())
    case CheckMode => _ => CheckYourAnswersController.onPageLoad(Some(Section.businessOnOwnAccount))
  }
}
