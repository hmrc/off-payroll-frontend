/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers.sections.personalService

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.personalService.ArrangedSubstituteFormProvider
import javax.inject.Inject
import models.Mode
import navigation.PersonalServiceNavigator
import pages.sections.personalService.ArrangedSubstitutePage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.personalService.ArrangedSubstituteView

import scala.concurrent.Future

class ArrangedSubstituteController @Inject()(identify: IdentifierAction,
                                             getData: DataRetrievalAction,
                                             requireData: DataRequiredAction,
                                             requireUserType: UserTypeRequiredAction,
                                             formProvider: ArrangedSubstituteFormProvider,
                                             override val controllerComponents: MessagesControllerComponents,
                                             view: ArrangedSubstituteView,
                                             checkYourAnswersService: CheckYourAnswersService,
                                             override val compareAnswerService: CompareAnswerService,
                                             override val dataCacheConnector: DataCacheConnector,
                                             override val navigator: PersonalServiceNavigator,
                                             implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {


  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData andThen requireUserType) { implicit request =>
    Ok(view(fillForm(ArrangedSubstitutePage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode,value,ArrangedSubstitutePage)
    )
  }
}
