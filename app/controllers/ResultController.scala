/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import config.featureSwitch.FeatureSwitching
import config.{FrontendAppConfig, SessionKeys}
import connectors.DataCacheConnector
import controllers.actions._
import forms.{DeclarationFormProvider, DownloadPDFCopyFormProvider}
import handlers.ErrorHandler
import javax.inject.Inject
import models.{NormalMode, Timestamp}
import navigation.CYANavigator
import pages.{ResultPage, Timestamp}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import utils.SessionUtils._

class ResultController @Inject()(identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 override val controllerComponents: MessagesControllerComponents,
                                 requireUserType: UserTypeRequiredAction,
                                 formProvider: DeclarationFormProvider,
                                 formProviderPDF: DownloadPDFCopyFormProvider,
                                 override val navigator: CYANavigator,
                                 override val dataCacheConnector: DataCacheConnector,
                                 time: Timestamp,
                                 override val compareAnswerService: CompareAnswerService,
                                 decisionService: DecisionService,
                                 checkYourAnswersService: CheckYourAnswersService,
                                 errorHandler: ErrorHandler,
                                 implicit val appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType).async { implicit request =>
    val timestamp = compareAnswerService.constructAnswers(request,time.timestamp(),Timestamp)

    dataCacheConnector.save(timestamp.cacheMap).flatMap { _ =>
      decisionService.decide.map {
        case Right(decision) =>
          decisionService.determineResultView(decision) match {
            case Right(result) => Ok(result).addingToSession(SessionKeys.decisionResponse -> decision)
            case Left(_) => Redirect(controllers.routes.StartAgainController.somethingWentWrong())
          }
        case Left(_) => Redirect(controllers.routes.StartAgainController.somethingWentWrong())
      }
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    redirect[Boolean](NormalMode, true, ResultPage)
  }
}
