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
import navigation.SetupNavigator
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CompareAnswerService
import views.html.sections.setup.NoIntermediaryView

class NoIntermediaryController @Inject()(override val navigator: SetupNavigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         override val controllerComponents: MessagesControllerComponents,
                                         view: NoIntermediaryView,
                                         override val compareAnswerService: CompareAnswerService,
                                         override val dataCacheConnector: DataCacheConnector,
                                         implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(controllers.routes.StartAgainController.redirectToDisclaimer()))
  }
}
