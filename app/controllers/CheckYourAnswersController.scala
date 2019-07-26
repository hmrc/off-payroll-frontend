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

import javax.inject.Inject
import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import handlers.ErrorHandler
import models._
import navigation.Navigator
import navigation.Section.SectionEnum
import pages.CheckYourAnswersPage
import play.api.mvc._
import services.{CheckYourAnswersService, CheckYourAnswersValidationService, CompareAnswerService, DecisionService}
import views.html.CheckYourAnswersView

class CheckYourAnswersController @Inject()(navigator: Navigator,
                                           identify: IdentifierAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           controllerComponents: MessagesControllerComponents,
                                           view: CheckYourAnswersView,
                                           checkYourAnswersService: CheckYourAnswersService,
                                           checkYourAnswersValidationService: CheckYourAnswersValidationService,
                                           compareAnswerService: CompareAnswerService,
                                           dataCacheConnector: DataCacheConnector,
                                           decisionService: DecisionService,
                                           errorHandler: ErrorHandler,
                                           implicit val appConfig: FrontendAppConfig) extends BaseController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) {

  def onPageLoad(sectionToExpand: Option[SectionEnum] = None): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    checkYourAnswersValidationService.isValid(request.userAnswers) match {
      case Right(_) => Ok(view(checkYourAnswersService.sections))
      case Left(_) => Redirect(controllers.routes.StartAgainController.somethingWentWrong)
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Redirect(navigator.nextPage(CheckYourAnswersPage, NormalMode)(request.userAnswers))
  }
}
