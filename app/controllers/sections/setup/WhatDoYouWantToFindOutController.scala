/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import views.html.sections.setup.WhatDoYouWantToFindOutView

import scala.concurrent.Future

class WhatDoYouWantToFindOutController @Inject()(identify: IdentifierAction,
                                        getData: DataRetrievalAction,
                                        requireData: DataRequiredAction,
                                        whatDoYouWantToFindOutFormProvider: WhatDoYouWantToFindOutFormProvider,
                                        controllerComponents: MessagesControllerComponents,
                                        whatDoYouWantToFindOutView: WhatDoYouWantToFindOutView,
                                        checkYourAnswersService: CheckYourAnswersService,
                                        compareAnswerService: CompareAnswerService,
                                        dataCacheConnector: DataCacheConnector,
                                        decisionService: DecisionService,
                                        navigator: SetupNavigator,
                                        implicit val appConfig: FrontendAppConfig) extends BaseNavigationController(controllerComponents,
  compareAnswerService, dataCacheConnector, navigator, decisionService) with FeatureSwitching {

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
