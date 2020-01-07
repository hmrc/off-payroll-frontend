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
import forms.sections.setup.ContractStartedFormProvider
import javax.inject.Inject
import models.{AuditJourneyStart, Mode}
import navigation.SetupNavigator
import pages.sections.setup.ContractStartedPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import views.html.sections.setup.ContractStartedView

import scala.concurrent.Future

class ContractStartedController @Inject()(identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: ContractStartedFormProvider,
                                          override val controllerComponents: MessagesControllerComponents,
                                          view: ContractStartedView,
                                          checkYourAnswersService: CheckYourAnswersService,
                                          override val compareAnswerService: CompareAnswerService,
                                          override val dataCacheConnector: DataCacheConnector,
                                          override val navigator: SetupNavigator,
                                          val auditConnector: AuditConnector,
                                          implicit val appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(ContractStartedPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        auditConnector.sendExplicitAudit("cestJourneyStart", AuditJourneyStart(request.userAnswers))
        redirect(mode, value, ContractStartedPage)
      }
    )
  }
}
