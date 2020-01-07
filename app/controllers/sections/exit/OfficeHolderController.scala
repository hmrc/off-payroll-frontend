/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.exit

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.exit.OfficeHolderFormProvider
import javax.inject.Inject
import models.{CheckMode, Mode, NormalMode}
import navigation.ExitNavigator
import pages.sections.exit.OfficeHolderPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.exit.OfficeHolderView

import scala.concurrent.Future

class OfficeHolderController @Inject()(identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: OfficeHolderFormProvider,
                                       override val controllerComponents: MessagesControllerComponents,
                                       view: OfficeHolderView,
                                       checkYourAnswersService: CheckYourAnswersService,
                                       override val compareAnswerService: CompareAnswerService,
                                       override val dataCacheConnector: DataCacheConnector,
                                       override val navigator: ExitNavigator,
                                       implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(OfficeHolderPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        val currentAnswer = request.userAnswers.get(OfficeHolderPage)
        val overrideMode = if(mode == CheckMode && !value && currentAnswer.contains(true)) NormalMode else mode
        redirect[Boolean](overrideMode, value, OfficeHolderPage)
      }
    )
  }
}
