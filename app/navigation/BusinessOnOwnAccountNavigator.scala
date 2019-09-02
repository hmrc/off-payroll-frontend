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
import controllers.routes
import controllers.routes._
import controllers.sections.businessOnOwnAccount.{routes => booaRoutes}
import javax.inject.{Inject, Singleton}
import models._
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.setup.ContractStartedPage
import play.api.mvc.Call


@Singleton
class BusinessOnOwnAccountNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  def startPage(userAnswers: UserAnswers): Call =
    (isWorker(userAnswers), userAnswers.getAnswer(ContractStartedPage)) match {
      case (false, Some(false)) => booaRoutes.WorkerKnownController.onPageLoad(NormalMode)
      case _ => booaRoutes.MultipleContractsController.onPageLoad(NormalMode)
    }

  private val routeMap:  Map[Page, UserAnswers => Call] = Map(

    WorkerKnownPage -> (_ => booaRoutes.MultipleContractsController.onPageLoad(NormalMode)),

    MultipleContractsPage -> (answer =>
      answer.getAnswer(MultipleContractsPage) match {
        case Some(false) => booaRoutes.PermissionToWorkWithOthersController.onPageLoad(NormalMode)
        case _ => booaRoutes.OwnershipRightsController.onPageLoad(NormalMode)
      }
    ),

    PermissionToWorkWithOthersPage -> (_ => booaRoutes.OwnershipRightsController.onPageLoad(NormalMode)),

    OwnershipRightsPage -> (answer =>
      (answer.getAnswer(OwnershipRightsPage), workerKnown(answer)) match {
        case (Some(true), _) => booaRoutes.RightsOfWorkController.onPageLoad(NormalMode)
        case (_, true) => booaRoutes.PreviousContractController.onPageLoad(NormalMode)
        case _ => booaRoutes.FirstContractController.onPageLoad(NormalMode)
      }
    ),

    RightsOfWorkPage -> (answer =>
      (answer.getAnswer(RightsOfWorkPage), workerKnown(answer)) match {
        case (Some(false), _) => booaRoutes.TransferOfRightsController.onPageLoad(NormalMode)
        case (_, true) => booaRoutes.PreviousContractController.onPageLoad(NormalMode)
        case _ => booaRoutes.FirstContractController.onPageLoad(NormalMode)
      }
    ),

    TransferOfRightsPage -> (_ => booaRoutes.PreviousContractController.onPageLoad(NormalMode)),

    PreviousContractPage -> (answer =>
      answer.getAnswer(PreviousContractPage) match {
        case Some(true) => booaRoutes.FollowOnContractController.onPageLoad(NormalMode)
        case _ => booaRoutes.FirstContractController.onPageLoad(NormalMode)
      }
    ),

    FollowOnContractPage -> (answer =>
      answer.getAnswer(FollowOnContractPage) match {
        case Some(false) => booaRoutes.ExtendContractController.onPageLoad(NormalMode)
        case _ => booaRoutes.MajorityOfWorkingTimeController.onPageLoad(NormalMode)
      }
    ),

    FirstContractPage -> (answer =>
      answer.getAnswer(FirstContractPage) match {
        case Some(false) => booaRoutes.ExtendContractController.onPageLoad(NormalMode)
        case _ => booaRoutes.MajorityOfWorkingTimeController.onPageLoad(NormalMode)
      }
    ),

    ExtendContractPage -> (_ => booaRoutes.MajorityOfWorkingTimeController.onPageLoad(NormalMode)),

    MajorityOfWorkingTimePage -> (_ => booaRoutes.FinanciallyDependentController.onPageLoad(NormalMode)),

    FinanciallyDependentPage -> (answer =>
      workerKnown(answer) match {
        case true => booaRoutes.SimilarWorkOtherClientsController.onPageLoad(NormalMode)
        case _ => routes.CheckYourAnswersController.onPageLoad()
      }
    ),

    SimilarWorkOtherClientsPage -> (_ => routes.CheckYourAnswersController.onPageLoad())
  )

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = routeMap.getOrElse(page, _ => IndexController.onPageLoad())
}
