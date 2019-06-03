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
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.DataCacheConnector
import controllers.BaseController
import controllers.actions._
import forms.WouldWorkerPaySubstituteFormProvider
import javax.inject.Inject
import models.Answers._
import models.{ErrorTemplate, Mode}
import navigation.Navigator
import pages.sections.personalService.WouldWorkerPaySubstitutePage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Request}
import play.twirl.api.HtmlFormat
import services.{CompareAnswerService, DecisionService}
import views.html.subOptimised.sections.personalService.{WouldWorkerPaySubstituteView => SubOptimisedWouldWorkerPaySubstituteView}
import views.html.sections.personalService.WouldWorkerPaySubstituteView

import scala.concurrent.Future

class WouldWorkerPaySubstituteController @Inject()(dataCacheConnector: DataCacheConnector,
                                                   navigator: Navigator,
                                                   identify: IdentifierAction,
                                                   getData: DataRetrievalAction,
                                                   requireData: DataRequiredAction,
                                                   formProvider: WouldWorkerPaySubstituteFormProvider,
                                                   controllerComponents: MessagesControllerComponents,
                                                   optimisedView: WouldWorkerPaySubstituteView,
                                                   subOptimisedView: SubOptimisedWouldWorkerPaySubstituteView,
                                                   decisionService: DecisionService,
                                                   implicit val appConfig: FrontendAppConfig)
  extends BaseController(controllerComponents) with FeatureSwitching {

  val form: Form[Boolean] = formProvider()

  private def view(form: Form[Boolean], mode: Mode)(implicit request: Request[_]): HtmlFormat.Appendable =
    if(isEnabled(OptimisedFlow)) optimisedView(form, mode) else subOptimisedView(form, mode)

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(request.userAnswers.get(WouldWorkerPaySubstitutePage).fold(form)(answerModel => form.fill(answerModel.answer)), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        val answers = CompareAnswerService.constructAnswers(request,value,WouldWorkerPaySubstitutePage)
        dataCacheConnector.save(answers.cacheMap).flatMap(
          _ => {
            val continue = navigator.nextPage(WouldWorkerPaySubstitutePage, mode)(answers)
            val exit = continue
            decisionService.decide(answers, continue, ErrorTemplate("wouldWorkerPaySubstitute.title"))
          }
        )
      }
    )
  }
}
