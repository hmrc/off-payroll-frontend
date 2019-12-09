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
import controllers.sections.businessOnOwnAccount.{routes => booaRoutes}
import javax.inject.{Inject, Singleton}
import models._
import models.sections.setup.WhoAreYou.Hirer
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.setup.{ContractStartedPage, WhoAreYouPage}
import play.api.mvc.Call

@Singleton
class BusinessOnOwnAccountNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  def startPage(userAnswers: UserAnswers): Call =
    (userAnswers.getAnswer(WhoAreYouPage), userAnswers.getAnswer(ContractStartedPage).getOrElse(false)) match {
      case (Some(Hirer), false) => booaRoutes.WorkerKnownController.onPageLoad(NormalMode)
      case _ => booaRoutes.MultipleContractsController.onPageLoad(NormalMode)
    }

  //noinspection ScalaStyle
  private def routeMap(implicit mode: Mode):  Map[Page, UserAnswers => Call] = Map(

    WorkerKnownPage -> (answer =>
      answer.getAnswer(WorkerKnownPage) match {
        case Some(false) => routeToCheckYourAnswers
        case Some(true) => booaRoutes.MultipleContractsController.onPageLoad(mode)
        case None => booaRoutes.WorkerKnownController.onPageLoad(mode)
      }
    ),
    MultipleContractsPage -> (answer =>
      answer.getAnswer(MultipleContractsPage) match {
        case Some(false) => booaRoutes.PermissionToWorkWithOthersController.onPageLoad(mode)
        case _ => booaRoutes.OwnershipRightsController.onPageLoad(mode)
      }
    ),

    PermissionToWorkWithOthersPage -> (_ => booaRoutes.OwnershipRightsController.onPageLoad(mode)),

    OwnershipRightsPage -> (answer =>
      answer.getAnswer(OwnershipRightsPage) match {
        case Some(true) => booaRoutes.RightsOfWorkController.onPageLoad(mode)
        case _ => booaRoutes.PreviousContractController.onPageLoad(mode)
      }
    ),

    RightsOfWorkPage -> (answer =>
      answer.getAnswer(RightsOfWorkPage) match {
        case Some(false) => booaRoutes.TransferOfRightsController.onPageLoad(mode)
        case _ => booaRoutes.PreviousContractController.onPageLoad(mode)
      }
    ),

    TransferOfRightsPage -> (_ => booaRoutes.PreviousContractController.onPageLoad(mode)),

    PreviousContractPage -> (answer =>
      answer.getAnswer(PreviousContractPage) match {
        case Some(true) => booaRoutes.FollowOnContractController.onPageLoad(mode)
        case _ => booaRoutes.FirstContractController.onPageLoad(mode)
      }
    ),

    FollowOnContractPage -> (answer =>
      answer.getAnswer(FollowOnContractPage) match {
        case Some(false) => booaRoutes.FirstContractController.onPageLoad(mode)
        case _ => booaRoutes.MajorityOfWorkingTimeController.onPageLoad(mode)
      }
    ),

    FirstContractPage -> (answer =>
      answer.getAnswer(FirstContractPage) match {
        case Some(false) => booaRoutes.ExtendContractController.onPageLoad(mode)
        case _ => booaRoutes.MajorityOfWorkingTimeController.onPageLoad(mode)
      }
    ),

    ExtendContractPage -> (_ => booaRoutes.MajorityOfWorkingTimeController.onPageLoad(mode)),

    MajorityOfWorkingTimePage -> (_ => booaRoutes.SimilarWorkOtherClientsController.onPageLoad(mode)),

    SimilarWorkOtherClientsPage -> (_ => routeToCheckYourAnswers)
  )

  private def routeToCheckYourAnswers(implicit mode: Mode): Call = mode match {
    case NormalMode => CheckYourAnswersController.onPageLoad(None)
    case CheckMode => CheckYourAnswersController.onPageLoad(Some(Section.businessOnOwnAccount))
  }

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = routeMap(mode).getOrElse(page, _ => IndexController.onPageLoad())
}
