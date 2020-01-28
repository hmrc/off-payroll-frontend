/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.financialRisk

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.financialRisk.EquipmentExpensesFormProvider
import javax.inject.Inject
import models.Mode
import navigation.FinancialRiskNavigator
import pages.sections.financialRisk.EquipmentExpensesPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CompareAnswerService
import views.html.sections.financialRisk.EquipmentExpensesView

import scala.concurrent.Future

class EquipmentExpensesController @Inject()(override val dataCacheConnector: DataCacheConnector,
                                            override val navigator: FinancialRiskNavigator,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            requireUserType: UserTypeRequiredAction,
                                            formProvider: EquipmentExpensesFormProvider,
                                            override val controllerComponents: MessagesControllerComponents,
                                            view: EquipmentExpensesView,
                                            override val compareAnswerService: CompareAnswerService,
                                            implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) { implicit request =>
    Ok(view(fillForm(EquipmentExpensesPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode, value, EquipmentExpensesPage)
    )
  }
}
