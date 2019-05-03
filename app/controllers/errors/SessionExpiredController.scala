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

package controllers.errors

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions.{DataRetrievalAction, IdentifierAction}
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.errors.SessionExpiredView
import views.html.SessionDeletedView

@Singleton
class SessionExpiredController @Inject()(val appConfig: FrontendAppConfig,
                                         identify: IdentifierAction,
                                         controllerComponents: MessagesControllerComponents,
                                         expiredView: SessionExpiredView,
                                         deletedView: SessionDeletedView,
                                         dataCacheConnector: DataCacheConnector
                                        ) extends FrontendController(controllerComponents) with I18nSupport {

  def checkTimeout = identify { implicit request =>
    Redirect(controllers.errors.routes.SessionExpiredController.onPageLoadDeleted()).withSession()
  }

  def onPageLoad: Action[AnyContent] = Action { implicit request =>
    Ok(expiredView(appConfig))
  }

  def onPageLoadDeleted: Action[AnyContent] = Action { implicit request =>
    Ok(deletedView(appConfig))
  }

  def onSubmit: Action[AnyContent] = Action { implicit request =>
    Redirect(controllers.routes.IndexController.onPageLoad())
  }
}
