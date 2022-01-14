/*
 * Copyright 2022 HM Revenue & Customs
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
import pages.sections.setup.AgencyAdvisoryPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.setup.AgencyAdvisoryView

class AgencyAdvisoryController @Inject()(override val navigator: SetupNavigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         requireUserType: UserTypeRequiredAction,
                                         override val controllerComponents: MessagesControllerComponents,
                                         view: AgencyAdvisoryView,
                                         checkYourAnswersService: CheckYourAnswersService,
                                         override val compareAnswerService: CompareAnswerService,
                                         override val dataCacheConnector: DataCacheConnector,
                                         implicit val appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) { implicit request =>
    Ok(view(
      postAction = routes.AgencyAdvisoryController.onSubmit()
    ))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Redirect(navigator.nextPage(AgencyAdvisoryPage, NormalMode)(request.userAnswers))
  }
}
