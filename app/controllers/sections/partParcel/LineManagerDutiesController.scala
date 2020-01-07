/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.partParcel

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.partAndParcel.LineManagerDutiesFormProvider
import javax.inject.Inject
import models.Mode
import navigation.PartAndParcelNavigator
import pages.sections.partParcel.LineManagerDutiesPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.partParcel.LineManagerDutiesView

import scala.concurrent.Future

class LineManagerDutiesController @Inject()(identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            formProvider: LineManagerDutiesFormProvider,
                                            override val controllerComponents: MessagesControllerComponents,
                                            view: LineManagerDutiesView,
                                            checkYourAnswersService: CheckYourAnswersService,
                                            override val compareAnswerService: CompareAnswerService,
                                            override val dataCacheConnector: DataCacheConnector,
                                            override val navigator: PartAndParcelNavigator,
                                            implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(LineManagerDutiesPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        redirect(mode,value, LineManagerDutiesPage)
      }
    )
  }
}
