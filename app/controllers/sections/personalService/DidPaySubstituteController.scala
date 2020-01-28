/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.personalService

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.personalService.DidPaySubstituteFormProvider
import javax.inject.Inject
import models.Mode
import navigation.PersonalServiceNavigator
import pages.sections.personalService.DidPaySubstitutePage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.personalService.DidPaySubstituteView

import scala.concurrent.Future

class DidPaySubstituteController @Inject()(identify: IdentifierAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           requireUserType: UserTypeRequiredAction,
                                           formProvider: DidPaySubstituteFormProvider,
                                           override val controllerComponents: MessagesControllerComponents,
                                           view: DidPaySubstituteView,
                                           checkYourAnswersService: CheckYourAnswersService,
                                           override val compareAnswerService: CompareAnswerService,
                                           override val dataCacheConnector: DataCacheConnector,
                                           override val navigator: PersonalServiceNavigator,
                                           implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) { implicit request =>
    Ok(view(fillForm(DidPaySubstitutePage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        redirect(mode,value,DidPaySubstitutePage)
      }
    )
  }
}
