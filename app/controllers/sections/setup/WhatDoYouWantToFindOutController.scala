/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.setup

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.setup.WhatDoYouWantToFindOutFormProvider
import javax.inject.Inject
import models._
import models.sections.setup.WhatDoYouWantToFindOut
import navigation.SetupNavigator
import pages.sections.setup.WhatDoYouWantToFindOutPage
import play.api.data.Form
import play.api.mvc._
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.setup.WhatDoYouWantToFindOutView

import scala.concurrent.Future

class WhatDoYouWantToFindOutController @Inject()(identify: IdentifierAction,
                                                 getData: DataRetrievalAction,
                                                 requireData: DataRequiredAction,
                                                 whatDoYouWantToFindOutFormProvider: WhatDoYouWantToFindOutFormProvider,
                                                 override val controllerComponents: MessagesControllerComponents,
                                                 whatDoYouWantToFindOutView: WhatDoYouWantToFindOutView,
                                                 checkYourAnswersService: CheckYourAnswersService,
                                                 override val compareAnswerService: CompareAnswerService,
                                                 override val dataCacheConnector: DataCacheConnector,
                                                 override val navigator: SetupNavigator,
                                                 implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  val form: Form[WhatDoYouWantToFindOut] = whatDoYouWantToFindOutFormProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(whatDoYouWantToFindOutView(fillForm(WhatDoYouWantToFindOutPage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(whatDoYouWantToFindOutView(formWithErrors, mode))),
      value => redirect(mode, value, WhatDoYouWantToFindOutPage)
    )
  }
}
