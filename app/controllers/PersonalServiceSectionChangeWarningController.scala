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

package controllers

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.actions._
import javax.inject.Inject
import models.{ArrangedSubstitute, CheckMode}
import navigation.OldNavigator
import pages.{Page, PersonalServiceSectionChangeWarningPage}
import pages.sections.personalService._
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import views.html.PersonalServiceSectionChangeWarningView
import controllers.sections.personalService.{routes => personalServiceRoutes}
import handlers.ErrorHandler

class PersonalServiceSectionChangeWarningController @Inject()(identify: IdentifierAction,
                                                              getData: DataRetrievalAction,
                                                              requireData: DataRequiredAction,
                                                              controllerComponents: MessagesControllerComponents,
                                                              view: PersonalServiceSectionChangeWarningView,
                                                              checkYourAnswersService: CheckYourAnswersService,
                                                              compareAnswerService: CompareAnswerService,
                                                              dataCacheConnector: DataCacheConnector,
                                                              errorHandler: ErrorHandler,
                                                              implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents) with FeatureSwitching {

  def onPageLoad(page: String): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(routes.PersonalServiceSectionChangeWarningController.onSubmit(page)))
  }

  def onSubmit(page: String): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>

    val userAnswers = request.userAnswers.set(PersonalServiceSectionChangeWarningPage, 0, true)

    dataCacheConnector.save(userAnswers.cacheMap).map { _ =>
      Page(page) match {
        case ArrangedSubstitutePage => Redirect(personalServiceRoutes.ArrangedSubstituteController.onPageLoad(CheckMode))
        case DidPaySubstitutePage => Redirect(personalServiceRoutes.DidPaySubstituteController.onPageLoad(CheckMode))
        case NeededToPayHelperPage => Redirect(personalServiceRoutes.NeededToPayHelperController.onPageLoad(CheckMode))
        case RejectSubstitutePage => Redirect(personalServiceRoutes.RejectSubstituteController.onPageLoad(CheckMode))
        case WouldWorkerPaySubstitutePage => Redirect(personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(CheckMode))
        case _ => InternalServerError(errorHandler.internalServerErrorTemplate)
      }
    }
  }
}
