/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers.sections.financialRisk

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.financialRisk.PutRightAtOwnCostFormProvider
import javax.inject.Inject
import models.Mode
import navigation.FinancialRiskNavigator
import pages.sections.financialRisk.PutRightAtOwnCostPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.financialRisk.PutRightAtOwnCostView

import scala.concurrent.Future

class PutRightAtOwnCostController @Inject()(identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            requireUserType: UserTypeRequiredAction,
                                            formProvider: PutRightAtOwnCostFormProvider,
                                            override val controllerComponents: MessagesControllerComponents,
                                            view: PutRightAtOwnCostView,
                                            checkYourAnswersService: CheckYourAnswersService,
                                            override val compareAnswerService: CompareAnswerService,
                                            override val dataCacheConnector: DataCacheConnector,
                                            override val navigator: FinancialRiskNavigator,
                                            implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) { implicit request =>
    Ok(view(fillForm(PutRightAtOwnCostPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        redirect(mode,value, PutRightAtOwnCostPage)
      }
    )
  }
}
