/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers.sections.businessOnOwnAccount

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.businessOnOwnAccount.MultipleContractsFormProvider
import javax.inject.Inject
import models.Mode
import navigation.BusinessOnOwnAccountNavigator
import pages.sections.businessOnOwnAccount.MultipleContractsPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CompareAnswerService
import views.html.sections.businessOnOwnAccount.MultipleContractsView

import scala.concurrent.Future

class MultipleContractsController @Inject()(override val dataCacheConnector: DataCacheConnector,
                                            override val navigator: BusinessOnOwnAccountNavigator,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            requireUserType: UserTypeRequiredAction,
                                            formProvider: MultipleContractsFormProvider,
                                            override val controllerComponents: MessagesControllerComponents,
                                            override val compareAnswerService: CompareAnswerService,
                                            view: MultipleContractsView,
                                            implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) { implicit request =>
    Ok(view(fillForm(MultipleContractsPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode, value, MultipleContractsPage)
    )
  }
}
