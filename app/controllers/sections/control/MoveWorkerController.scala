/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.control

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.control.MoveWorkerFormProvider
import javax.inject.Inject
import models.Mode
import navigation.ControlNavigator
import pages.sections.control.MoveWorkerPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.control.MoveWorkerView

import scala.concurrent.Future

class MoveWorkerController @Inject()(identify: IdentifierAction,
                                     getData: DataRetrievalAction,
                                     requireData: DataRequiredAction,
                                     formProvider: MoveWorkerFormProvider,
                                     override val controllerComponents: MessagesControllerComponents,
                                     view: MoveWorkerView,
                                     checkYourAnswersService: CheckYourAnswersService,
                                     override val compareAnswerService: CompareAnswerService,
                                     override val dataCacheConnector: DataCacheConnector,
                                     override val navigator: ControlNavigator,
                                     implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(MoveWorkerPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode,value,MoveWorkerPage)
    )
  }
}
