package controllers

import javax.inject.Inject

import play.api.i18n.I18nSupport
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import controllers.actions._
import config.FrontendAppConfig
import views.html.$className$View
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}


import scala.concurrent.{Future, ExecutionContext}

class $className;format="cap"$Controller @Inject()(appConfig: FrontendAppConfig,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         controllerComponents: MessagesControllerComponents,
                                         view: $className$View
                                         ) extends FrontendController(controllerComponents) with I18nSupport {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(appConfig))
  }
}
