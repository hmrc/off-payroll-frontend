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
import javax.inject.Inject
import models.NormalMode
import navigation.SetupNavigator
import pages.sections.setup.AboutYourResultPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.setup.AboutYourResultView

class AboutYourResultController @Inject()(override val navigator: SetupNavigator,
                                          identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          override val controllerComponents: MessagesControllerComponents,
                                          view: AboutYourResultView,
                                          checkYourAnswersService: CheckYourAnswersService,
                                          override val compareAnswerService: CompareAnswerService,
                                          override val dataCacheConnector: DataCacheConnector,
                                          implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(routes.AboutYourResultController.onSubmit()))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Redirect(navigator.nextPage(AboutYourResultPage, NormalMode)(request.userAnswers))
  }
}
