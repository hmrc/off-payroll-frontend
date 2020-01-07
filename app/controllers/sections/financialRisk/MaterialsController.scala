/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.financialRisk

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.financialRisk.MaterialsFormProvider
import javax.inject.Inject
import models.Mode
import navigation.FinancialRiskNavigator
import pages.sections.financialRisk.MaterialsPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.financialRisk.MaterialsView

import scala.concurrent.Future

class MaterialsController @Inject()(override val dataCacheConnector: DataCacheConnector,
                                    override val navigator: FinancialRiskNavigator,
                                    identify: IdentifierAction,
                                    getData: DataRetrievalAction,
                                    requireData: DataRequiredAction,
                                    formProvider: MaterialsFormProvider,
                                    override val controllerComponents: MessagesControllerComponents,
                                    view: MaterialsView,
                                    checkYourAnswersService: CheckYourAnswersService,
                                    override val compareAnswerService: CompareAnswerService,
                                    implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(MaterialsPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode, value, MaterialsPage)
    )
  }
}
