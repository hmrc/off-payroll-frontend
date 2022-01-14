/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import handlers.ErrorHandler
import javax.inject.Inject
import models.Section.SectionEnum
import models._
import navigation.CYANavigator
import pages.CheckYourAnswersPage
import play.api.mvc._
import services.{CheckYourAnswersService, CheckYourAnswersValidationService, CompareAnswerService}
import views.html.CheckYourAnswersView

class CheckYourAnswersController @Inject()(override val navigator: CYANavigator,
                                           identify: IdentifierAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           requireUserType: UserTypeRequiredAction,
                                           override val controllerComponents: MessagesControllerComponents,
                                           view: CheckYourAnswersView,
                                           checkYourAnswersService: CheckYourAnswersService,
                                           checkYourAnswersValidationService: CheckYourAnswersValidationService,
                                           override val compareAnswerService: CompareAnswerService,
                                           override val dataCacheConnector: DataCacheConnector,
                                           errorHandler: ErrorHandler,
                                           implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController {

  def onPageLoad(sectionToExpand: Option[SectionEnum] = None): Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) {
    implicit request =>
      checkYourAnswersValidationService.isValid(request.userAnswers) match {
        case Right(_) => Ok(view(checkYourAnswersService.sections, sectionToExpand))
        case Left(_) => Redirect(controllers.routes.StartAgainController.somethingWentWrong)
      }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) { implicit request =>
    Redirect(navigator.nextPage(CheckYourAnswersPage, NormalMode)(request.userAnswers))
  }
}
