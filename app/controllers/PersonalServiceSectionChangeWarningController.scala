/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.actions._
import controllers.sections.personalService.{routes => personalServiceRoutes}
import handlers.ErrorHandler
import javax.inject.Inject
import models.CheckMode
import pages.sections.personalService._
import pages.{Page, PersonalServiceSectionChangeWarningPage}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CheckYourAnswersService
import views.html.PersonalServiceSectionChangeWarningView

class PersonalServiceSectionChangeWarningController @Inject()(identify: IdentifierAction,
                                                              getData: DataRetrievalAction,
                                                              requireData: DataRequiredAction,
                                                              override val controllerComponents: MessagesControllerComponents,
                                                              view: PersonalServiceSectionChangeWarningView,
                                                              checkYourAnswersService: CheckYourAnswersService,
                                                              dataCacheConnector: DataCacheConnector,
                                                              errorHandler: ErrorHandler,
                                                              implicit val appConfig: FrontendAppConfig)
  extends BaseController with FeatureSwitching {

  def onPageLoad(page: String): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(routes.PersonalServiceSectionChangeWarningController.onSubmit(page)))
  }

  def onSubmit(page: String): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>

    val userAnswers = request.userAnswers.set(PersonalServiceSectionChangeWarningPage, true)

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
