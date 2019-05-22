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

import config.FrontendAppConfig
import controllers.BaseController
import controllers.actions._
import models.NormalMode
import navigation.Navigator
import pages.sections.setup.AgencyAdvisoryPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.sections.setup.AgencyAdvisoryView

class AgencyAdvisoryController @Inject()(navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         controllerComponents: MessagesControllerComponents,
                                         view: AgencyAdvisoryView,
                                         implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents) {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(
      postAction = routes.AgencyAdvisoryController.onSubmit(),
      finishAction = routes.LeaveController.onPageLoad()
    ))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Redirect(navigator.nextPage(AgencyAdvisoryPage, NormalMode)(request.userAnswers))
  }
}
