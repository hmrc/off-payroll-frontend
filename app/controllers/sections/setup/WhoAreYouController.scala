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

import javax.inject.Inject

import config.featureSwitch.FeatureSwitching
import config.{FrontendAppConfig, SessionKeys}
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.WhoAreYouFormProvider
import models.WhatDoYouWantToFindOut.IR35
import models.requests.DataRequest
import models.{Mode, UserType, _}
import navigation.SetupNavigator
import pages.sections.setup.{WhatDoYouWantToFindOutPage, WhoAreYouPage}
import play.api.data.Form
import play.api.mvc._
import services.{CompareAnswerService, DecisionService}
import utils.SessionUtils._
import views.html.sections.setup.WhoAreYouView

import scala.concurrent.Future

class WhoAreYouController @Inject()(identify: IdentifierAction,
                                    getData: DataRetrievalAction,
                                    requireData: DataRequiredAction,
                                    whoAreYouFormProvider: WhoAreYouFormProvider,
                                    controllerComponents: MessagesControllerComponents,
                                    view: WhoAreYouView,
                                    compareAnswerService: CompareAnswerService,
                                    dataCacheConnector: DataCacheConnector,
                                    decisionService: DecisionService,
                                    navigator: SetupNavigator,
                                    implicit val appConfig: FrontendAppConfig) extends BaseNavigationController(controllerComponents,
  compareAnswerService, dataCacheConnector, navigator, decisionService) with FeatureSwitching {

  private def renderedView(mode: Mode, form: Form[WhoAreYou])(implicit request: DataRequest[_]) = {
    val showAgency = request.userAnswers.getAnswer(WhatDoYouWantToFindOutPage) match {
      case Some(IR35) => true
      case _ => false
    }
    view(routes.WhoAreYouController.onSubmit(mode), fillForm(WhoAreYouPage, form), mode, showAgency)
  }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(renderedView(mode, whoAreYouFormProvider()))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    whoAreYouFormProvider().bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(renderedView(mode, formWithErrors))),
      value => {
        println("HHHHHHHHHHHHHHHHHH")
        redirect(mode, value, WhoAreYouPage).map(result => result.addingToSession(SessionKeys.userType -> UserType(value)))
      }
        )
  }
}
