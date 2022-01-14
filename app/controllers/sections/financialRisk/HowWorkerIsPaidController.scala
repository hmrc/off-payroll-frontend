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
import forms.sections.financialRisk.HowWorkerIsPaidFormProvider
import javax.inject.Inject
import models.Mode
import navigation.FinancialRiskNavigator
import pages.sections.financialRisk.HowWorkerIsPaidPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.financialRisk.HowWorkerIsPaidView

import scala.concurrent.Future

class HowWorkerIsPaidController @Inject()(identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          requireUserType: UserTypeRequiredAction,
                                          formProvider: HowWorkerIsPaidFormProvider,
                                          override val controllerComponents: MessagesControllerComponents,
                                          view: HowWorkerIsPaidView,
                                          checkYourAnswersService: CheckYourAnswersService,
                                          override val compareAnswerService: CompareAnswerService,
                                          override val dataCacheConnector: DataCacheConnector,
                                          override val navigator: FinancialRiskNavigator,
                                          implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) { implicit request =>
    Ok(view(fillForm(HowWorkerIsPaidPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode,value,HowWorkerIsPaidPage)
    )
  }
}
