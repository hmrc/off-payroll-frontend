/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.actions._
import forms.AddReferenceDetailsFormProvider
import javax.inject.Inject
import models.NormalMode
import navigation.CYANavigator
import pages.AddReferenceDetailsPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CompareAnswerService
import views.html.AddReferenceDetailsView

import scala.concurrent.Future

class AddReferenceDetailsController @Inject()(identify: IdentifierAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              requireUserType: UserTypeRequiredAction,
                                              formProvider: AddReferenceDetailsFormProvider,
                                              override val controllerComponents: MessagesControllerComponents,
                                              addReferenceDetails: AddReferenceDetailsView,
                                              override val navigator: CYANavigator,
                                              override val dataCacheConnector: DataCacheConnector,
                                              override val compareAnswerService: CompareAnswerService,
                                              implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) { implicit request =>
    Ok(addReferenceDetails(fillForm(AddReferenceDetailsPage, form)))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(addReferenceDetails(formWithErrors))),
      value => {
        redirect[Boolean](NormalMode,value, AddReferenceDetailsPage)
      }
    )
  }
}
