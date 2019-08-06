package controllers

import javax.inject.Inject

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.TransferOfRightsFormProvider
import javax.inject.Inject
import models.Mode
import navigation.Navigator
import pages.TransferOfRightsPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.TransferOfRightsView

import scala.concurrent.Future

class TransferOfRightsController @Inject()(dataCacheConnector: DataCacheConnector,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: TransferOfRightsFormProvider,
                                         controllerComponents: MessagesControllerComponents,
                                         controllerHelper: ControllerHelper,
                                         view: TransferOfRightsView,
                                         implicit val appConfig: FrontendAppConfig
                                         ) extends BaseController(controllerComponents) {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(TransferOfRightsPage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      value => controllerHelper.redirect(mode, value, TransferOfRightsPage)
    )
  }
}
