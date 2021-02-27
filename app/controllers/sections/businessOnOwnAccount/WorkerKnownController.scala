/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.sections.businessOnOwnAccount

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.businessOnOwnAccount.WorkerKnownFormProvider
import javax.inject.Inject
import models.Mode
import navigation.BusinessOnOwnAccountNavigator
import pages.sections.businessOnOwnAccount.WorkerKnownPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CompareAnswerService
import views.html.sections.businessOnOwnAccount.WorkerKnownView

import scala.concurrent.Future

class WorkerKnownController @Inject()(override val dataCacheConnector: DataCacheConnector,
                                      override val navigator: BusinessOnOwnAccountNavigator,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      requireUserType: UserTypeRequiredAction,
                                      formProvider: WorkerKnownFormProvider,
                                      override val controllerComponents: MessagesControllerComponents,
                                      override val compareAnswerService: CompareAnswerService,
                                      view: WorkerKnownView,
                                      implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) { implicit request =>
    Ok(view(fillForm(WorkerKnownPage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode, value, WorkerKnownPage)
    )
  }
}
