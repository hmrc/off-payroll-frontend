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
import forms.sections.setup.WhatDoYouWantToDoFormProvider
import javax.inject.Inject
import models._
import models.sections.setup.WhatDoYouWantToDo
import navigation.SetupNavigator
import pages.sections.setup.WhatDoYouWantToDoPage
import play.api.data.Form
import play.api.mvc._
import services.{CheckYourAnswersService, CompareAnswerService}
import views.html.sections.setup.WhatDoYouWantToDoView

import scala.concurrent.Future

class WhatDoYouWantToDoController @Inject()(identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            WhatDoYouWantToDoFormProvider: WhatDoYouWantToDoFormProvider,
                                            override val controllerComponents: MessagesControllerComponents,
                                            whatDoYouWantToDoView: WhatDoYouWantToDoView,
                                            checkYourAnswersService: CheckYourAnswersService,
                                            override val compareAnswerService: CompareAnswerService,
                                            override val dataCacheConnector: DataCacheConnector,
                                            override val navigator: SetupNavigator,
                                            implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  val form: Form[WhatDoYouWantToDo] = WhatDoYouWantToDoFormProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(whatDoYouWantToDoView(fillForm(WhatDoYouWantToDoPage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(whatDoYouWantToDoView(formWithErrors, mode))),
      value => redirect(mode, value, WhatDoYouWantToDoPage)
    )
  }
}
