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

package controllers

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.actions._
import controllers.sections.businessOnOwnAccount.{routes => businessOnOwnAccountRoutes}
import handlers.ErrorHandler
import javax.inject.Inject
import models.CheckMode
import pages._
import pages.sections.businessOnOwnAccount._
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CheckYourAnswersService
import views.html.BusinessOnOwnAccountSectionChangeWarningView

class BusinessOnOwnAccountSectionChangeWarningController @Inject()(identify: IdentifierAction,
                                                                   getData: DataRetrievalAction,
                                                                   requireData: DataRequiredAction,
                                                                   override val controllerComponents: MessagesControllerComponents,
                                                                   view: BusinessOnOwnAccountSectionChangeWarningView,
                                                                   checkYourAnswersService: CheckYourAnswersService,
                                                                   dataCacheConnector: DataCacheConnector,
                                                                   errorHandler: ErrorHandler,
                                                                   implicit val appConfig: FrontendAppConfig)
  extends BaseController with FeatureSwitching {

  def onPageLoad(page: String): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(routes.BusinessOnOwnAccountSectionChangeWarningController.onSubmit(page)))
  }

  def onSubmit(page: String): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>

    val userAnswers = request.userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)

    dataCacheConnector.save(userAnswers.cacheMap).map { _ =>
      Page(page) match {
        case ExtendContractPage => Redirect(businessOnOwnAccountRoutes.ExtendContractController.onPageLoad(CheckMode))
        case FirstContractPage => Redirect(businessOnOwnAccountRoutes.FirstContractController.onPageLoad(CheckMode))
        case FollowOnContractPage => Redirect(businessOnOwnAccountRoutes.FollowOnContractController.onPageLoad(CheckMode))
        case MajorityOfWorkingTimePage => Redirect(businessOnOwnAccountRoutes.MajorityOfWorkingTimeController.onPageLoad(CheckMode))
        case MultipleContractsPage => Redirect(businessOnOwnAccountRoutes.MultipleContractsController.onPageLoad(CheckMode))
        case OwnershipRightsPage => Redirect(businessOnOwnAccountRoutes.OwnershipRightsController.onPageLoad(CheckMode))
        case PermissionToWorkWithOthersPage => Redirect(businessOnOwnAccountRoutes.PermissionToWorkWithOthersController.onPageLoad(CheckMode))
        case PreviousContractPage => Redirect(businessOnOwnAccountRoutes.PreviousContractController.onPageLoad(CheckMode))
        case RightsOfWorkPage => Redirect(businessOnOwnAccountRoutes.RightsOfWorkController.onPageLoad(CheckMode))
        case SimilarWorkOtherClientsPage => Redirect(businessOnOwnAccountRoutes.SimilarWorkOtherClientsController.onPageLoad(CheckMode))
        case TransferOfRightsPage => Redirect(businessOnOwnAccountRoutes.TransferOfRightsController.onPageLoad(CheckMode))
        case WorkerKnownPage => Redirect(businessOnOwnAccountRoutes.WorkerKnownController.onPageLoad(CheckMode))
        case _ => InternalServerError(errorHandler.internalServerErrorTemplate)
      }
    }
  }
}
