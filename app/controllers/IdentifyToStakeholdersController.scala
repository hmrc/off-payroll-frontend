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
import forms.IdentifyToStakeholdersFormProvider
import javax.inject.Inject
import models.Answers._

import models.{Enumerable, ErrorTemplate, IdentifyToStakeholders, Mode}
import navigation.Navigator
import pages.IdentifyToStakeholdersPage
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.DecisionService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.IdentifyToStakeholdersView
import services.CompareAnswerService

import scala.concurrent.{ExecutionContext, Future}

class IdentifyToStakeholdersController @Inject()(dataCacheConnector: DataCacheConnector,
                                                 navigator: Navigator,
                                                 identify: IdentifierAction,
                                                 getData: DataRetrievalAction,
                                                 requireData: DataRequiredAction,
                                                 formProvider: IdentifyToStakeholdersFormProvider,
                                                 controllerComponents: MessagesControllerComponents,
                                                 view: IdentifyToStakeholdersView,
                                                 decisionService: DecisionService,
                                                 implicit val appConfig: FrontendAppConfig
                                                ) extends FrontendController(controllerComponents) with I18nSupport with Enumerable.Implicits {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  val form: Form[IdentifyToStakeholders] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(appConfig, request.userAnswers.get(IdentifyToStakeholdersPage).fold(form)(answerModel => form.fill(answerModel.answer)), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(appConfig, formWithErrors, mode))),
      value => {
        val answers = CompareAnswerService.constructAnswers(request,value,IdentifyToStakeholdersPage)
        dataCacheConnector.save(answers.cacheMap).flatMap(
          _ => {
            val continue = navigator.nextPage(IdentifyToStakeholdersPage, mode)(answers)
            val exit = continue
            decisionService.decide(answers, continue, ErrorTemplate("identifyToStakeholders.title"))
          }
        )
      }
    )
  }
}
