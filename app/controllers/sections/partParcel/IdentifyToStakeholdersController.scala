/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers.sections.partParcel

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.partAndParcel.IdentifyToStakeholdersFormProvider
import javax.inject.Inject
import models.Mode
import navigation.PartAndParcelNavigator
import pages.sections.partParcel.IdentifyToStakeholdersPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.partParcel.IdentifyToStakeholdersView

import scala.concurrent.Future

class IdentifyToStakeholdersController @Inject()(identify: IdentifierAction,
                                                 getData: DataRetrievalAction,
                                                 requireData: DataRequiredAction,
                                                 requireUserType: UserTypeRequiredAction,
                                                 formProvider: IdentifyToStakeholdersFormProvider,
                                                 override val controllerComponents: MessagesControllerComponents,
                                                 view: IdentifyToStakeholdersView,
                                                 checkYourAnswersService: CheckYourAnswersService,
                                                 override val compareAnswerService: CompareAnswerService,
                                                 override val dataCacheConnector: DataCacheConnector,
                                                 override val navigator: PartAndParcelNavigator,
                                                 implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) { implicit request =>
    Ok(view(fillForm(IdentifyToStakeholdersPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        redirect(mode,value, IdentifyToStakeholdersPage)
      }
    )
  }
}
