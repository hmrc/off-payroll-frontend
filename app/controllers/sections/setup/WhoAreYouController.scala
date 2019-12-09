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

import config.featureSwitch.FeatureSwitching
import config.{FrontendAppConfig, SessionKeys}
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.setup.WhoAreYouFormProvider
import javax.inject.Inject
import models.requests.DataRequest
import models.sections.setup.WhatDoYouWantToFindOut.IR35
import models.sections.setup.WhoAreYou
import models.{Mode, UserType}
import navigation.SetupNavigator
import pages.sections.setup.{WhatDoYouWantToFindOutPage, WhoAreYouPage}
import play.api.data.Form
import play.api.mvc._
import services.CompareAnswerService
import utils.SessionUtils._
import views.html.sections.setup.WhoAreYouView

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
    val showAgency = request.userAnswers.getAnswer(WhatDoYouWantToFindOutPage).contains(IR35)
    view(routes.WhoAreYouController.onSubmit(mode), fillForm(WhoAreYouPage, form), mode, showAgency)
  }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(renderedView(mode, whoAreYouFormProvider()))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    whoAreYouFormProvider().bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(renderedView(mode, formWithErrors))),
      value => {
        redirect(mode, value, WhoAreYouPage).map(result => result.addingToSession(SessionKeys.userType -> UserType(value)))
      }
    )
  }
}
