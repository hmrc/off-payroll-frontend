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

package controllers.sections.personalService

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.BaseController
import controllers.actions._
import forms.ArrangedSubstituteFormProvider
import javax.inject.Inject
import models.Answers._
import models.{ArrangedSubstitute, Mode}
import navigation.Navigator
import pages.sections.personalService.ArrangedSubstitutePage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CompareAnswerService
import views.html.subOptimised.sections.personalService.ArrangedSubstituteView

import scala.concurrent.Future

class ArrangedSubstituteController @Inject()(dataCacheConnector: DataCacheConnector,
                                             navigator: Navigator,
                                             identify: IdentifierAction,
                                             getData: DataRetrievalAction,
                                             requireData: DataRequiredAction,
                                             formProvider: ArrangedSubstituteFormProvider,
                                             controllerComponents: MessagesControllerComponents,
                                             view: ArrangedSubstituteView,
                                             implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents) {

  val form: Form[ArrangedSubstitute] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(request.userAnswers.get(ArrangedSubstitutePage).fold(form)(answerModel => form.fill(answerModel.answer)), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        val answers = CompareAnswerService.constructAnswers(request,value,ArrangedSubstitutePage)
        dataCacheConnector.save(answers.cacheMap).map(
          _ => Redirect(navigator.nextPage(ArrangedSubstitutePage, mode)(answers))
        )
      }
    )
  }
}
