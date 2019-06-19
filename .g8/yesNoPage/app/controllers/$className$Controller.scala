package controllers

import javax.inject.Inject

import play.api.i18n.I18nSupport
import play.api.data.Form
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.DataCacheConnector
import controllers.actions._
import controllers.{BaseController, ControllerHelper}
import config.FrontendAppConfig
import forms.$className$FormProvider
import models.Mode
import pages.$className$Page
import navigation.Navigator
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.$className$View

import scala.concurrent.{Future, ExecutionContext}

class $className;format="cap"$Controller @Inject()(dataCacheConnector: DataCacheConnector,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: $className$FormProvider,
                                         controllerComponents: MessagesControllerComponents,
                                         controllerHelper: ControllerHelper,
                                         view: $className$View,
                                         implicit val appConfig: FrontendAppConfig
                                         ) extends BaseController(controllerComponents) {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm($className$Page, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      value => controllerHelper.redirect(mode, value, $className$Page)
    )
  }
}
