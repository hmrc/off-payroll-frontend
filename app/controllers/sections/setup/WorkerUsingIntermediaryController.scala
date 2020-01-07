/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.setup

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.setup.WorkerUsingIntermediaryFormProvider
import javax.inject.Inject
import models.Mode
import navigation.SetupNavigator
import pages.sections.setup.WorkerUsingIntermediaryPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CompareAnswerService
import views.html.sections.setup.WorkerUsingIntermediaryView

import scala.concurrent.Future

class WorkerUsingIntermediaryController @Inject()(identify: IdentifierAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  workerUsingIntermediaryFormProvider: WorkerUsingIntermediaryFormProvider,
                                                  override val controllerComponents: MessagesControllerComponents,
                                                  workerUsingIntermediaryView: WorkerUsingIntermediaryView,
                                                  override val compareAnswerService: CompareAnswerService,
                                                  override val dataCacheConnector: DataCacheConnector,
                                                  override val navigator: SetupNavigator,
                                                  implicit val appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(workerUsingIntermediaryView(fillForm(WorkerUsingIntermediaryPage, workerUsingIntermediaryFormProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    workerUsingIntermediaryFormProvider().bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(workerUsingIntermediaryView(formWithErrors, mode))),
      value => redirect(mode,value,WorkerUsingIntermediaryPage)
    )
  }
}
