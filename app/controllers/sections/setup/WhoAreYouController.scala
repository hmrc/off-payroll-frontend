/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.sections.setup

import config.featureSwitch.FeatureSwitching
import config.{FrontendAppConfig, SessionKeys}
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.setup.WhoAreYouFormProvider
import javax.inject.Inject
import models.Mode
import models.requests.DataRequest
import models.sections.setup.WhatDoYouWantToFindOut.IR35
import models.sections.setup.WhoAreYou
import navigation.SetupNavigator
import pages.sections.setup.{WhatDoYouWantToFindOutPage, WhoAreYouPage}
import play.api.data.Form
import play.api.mvc._
import services.CompareAnswerService
import views.html.sections.setup.WhoAreYouView
import utils.SessionUtils._

import scala.concurrent.Future

class WhoAreYouController @Inject()(identify: IdentifierAction,
                                    getData: DataRetrievalAction,
                                    requireData: DataRequiredAction,
                                    whoAreYouFormProvider: WhoAreYouFormProvider,
                                    override val controllerComponents: MessagesControllerComponents,
                                    view: WhoAreYouView,
                                    override val compareAnswerService: CompareAnswerService,
                                    override val dataCacheConnector: DataCacheConnector,
                                    override val navigator: SetupNavigator,
                                    implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  private def renderedView(mode: Mode, form: Form[WhoAreYou])(implicit request: DataRequest[_]) = {
    val showAgency = request.userAnswers.get(WhatDoYouWantToFindOutPage).contains(IR35)
    view(routes.WhoAreYouController.onSubmit(mode), fillForm(WhoAreYouPage, form), mode, showAgency)
  }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(renderedView(mode, whoAreYouFormProvider()))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    whoAreYouFormProvider().bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(renderedView(mode, formWithErrors))),
      value => {
        redirect(mode, value, WhoAreYouPage).map(result => result.addingToSession(SessionKeys.userType -> value))
      }
    )
  }
}
