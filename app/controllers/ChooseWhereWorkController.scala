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

package controllers

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.ChooseWhereWorkFormProvider
import javax.inject.Inject
import models.{Enumerable, Mode}
import navigation.Navigator
import pages.ChooseWhereWorkPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.DecisionService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.ChooseWhereWorkView

import scala.concurrent.{ExecutionContext, Future}

class ChooseWhereWorkController @Inject()(dataCacheConnector: DataCacheConnector,
                                          navigator: Navigator,
                                          identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: ChooseWhereWorkFormProvider,
                                          controllerComponents: MessagesControllerComponents,
                                          view: ChooseWhereWorkView,
                                          decisionService: DecisionService,
                                          implicit val appConfig: FrontendAppConfig
                                         ) extends FrontendController(controllerComponents) with I18nSupport with Enumerable.Implicits {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(appConfig, request.userAnswers.get(ChooseWhereWorkPage).fold(form)(form.fill), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(appConfig, formWithErrors, mode))),
      value => {
        val updatedAnswers = request.userAnswers.set(ChooseWhereWorkPage, value)
        dataCacheConnector.save(updatedAnswers.cacheMap).flatMap(
          _ => {

            val continue = navigator.nextPage(ChooseWhereWorkPage, mode)(updatedAnswers)
            val exit = continue
            decisionService.decide(updatedAnswers, continue, exit)
          }
        )
      }
    )
  }
}
