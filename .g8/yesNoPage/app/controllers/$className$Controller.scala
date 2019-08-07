package controllers

import javax.inject.Inject

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.$className$FormProvider
import javax.inject.Inject
import models.Mode
import pages.$className$Page
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.$className$View

import scala.concurrent.Future

class $className;format="cap"$Controller @Inject()(dataCacheConnector: DataCacheConnector,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: $className$FormProvider,
                                         controllerComponents: MessagesControllerComponents,
                                         controllerHelper: ControllerHelper,
                                         compareAnswerService: CompareAnswerService,
                                         decisionService: DecisionService,
                                         view: $className$View,
                                         implicit val appConfig: FrontendAppConfig extends BaseController(
controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) {

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
