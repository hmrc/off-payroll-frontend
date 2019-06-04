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
import forms.DeclarationFormProvider
import javax.inject.Inject

import models.{NormalMode, Timestamp}
import navigation.Navigator
import pages.ResultPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CompareAnswerService, DecisionService}
import utils.UserAnswersUtils

import scala.concurrent.Future

class ResultController @Inject()(identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 controllerComponents: MessagesControllerComponents,
                                 decisionService: DecisionService,
                                 formProvider: DeclarationFormProvider,
                                 navigator: Navigator,
                                 dataCacheConnector: DataCacheConnector,
                                 time: Timestamp,
                                 compareAnswerService: CompareAnswerService,
                                 implicit val conf: FrontendAppConfig) extends BaseController(controllerComponents) with UserAnswersUtils {

  val resultForm: Form[Boolean] = formProvider()

  private val version = conf.decisionVersion

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val timestamp = compareAnswerService.constructAnswers(request,time.timestamp(),ResultPage)
    dataCacheConnector.save(timestamp.cacheMap).map { _ =>
      val result = decisionService.determineResultView(answers)
      Ok(result)
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    resultForm.bindFromRequest().fold(
      formWithErrors => {
        val result = decisionService.determineResultView(answers, Some(formWithErrors))
        BadRequest(result)
      },
      _ => {
        Redirect(navigator.nextPage(ResultPage, NormalMode)(request.userAnswers))
      }
    )
  }

}
