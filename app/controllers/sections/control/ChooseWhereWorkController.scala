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

package controllers.sections.control

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.DataCacheConnector
import controllers.BaseController
import controllers.actions._
import forms.ChooseWhereWorkFormProvider
import javax.inject.Inject
import models.Answers._
import models.requests.DataRequest
import models.{ChooseWhereWork, ErrorTemplate, Mode}
import navigation.Navigator
import pages.sections.control.{ChooseWhereWorkPage, ScheduleOfWorkingHoursPage}
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import play.twirl.api.Html
import services.{CompareAnswerService, DecisionService}
import views.html.subOptimised.sections.control.ChooseWhereWorkView

import scala.concurrent.Future

class ChooseWhereWorkController @Inject()(dataCacheConnector: DataCacheConnector,
                                          navigator: Navigator,
                                          identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: ChooseWhereWorkFormProvider,
                                          controllerComponents: MessagesControllerComponents,
                                          view: ChooseWhereWorkView,
                                          optimisedView: views.html.sections.control.ChooseWhereWorkView,
                                          decisionService: DecisionService,
                                          implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents) with FeatureSwitching {

  val form: Form[ChooseWhereWork] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    if(isEnabled(OptimisedFlow)) optimisedSubmit(mode) else submit(mode)
  }

  private[controllers] def view(mode: Mode)(implicit request: DataRequest[_]):Html = if(isEnabled(OptimisedFlow)) {
    optimisedView(request.userAnswers.get(ChooseWhereWorkPage).fold(form)(answerModel => form.fill(answerModel.answer)), mode)
  } else {
    view(request.userAnswers.get(ChooseWhereWorkPage).fold(form)(answerModel => form.fill(answerModel.answer)), mode)
  }

  private[controllers] def submit(mode: Mode)(implicit request: DataRequest[AnyContent]): Future[Result] =
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        val answers = CompareAnswerService.constructAnswers(request,value,ChooseWhereWorkPage)
        dataCacheConnector.save(answers.cacheMap).flatMap(
          _ => {

            val continue = navigator.nextPage(ChooseWhereWorkPage, mode)(answers)
            decisionService.decide(answers, continue, ErrorTemplate("chooseWhereWork.title"))
          }
        )
      }
    )

  private[controllers] def optimisedSubmit(mode: Mode)(implicit request: DataRequest[AnyContent]): Future[Result] =
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(optimisedView(formWithErrors, mode))),
      value => {
        val answers = CompareAnswerService.constructAnswers(request,value,ChooseWhereWorkPage)
        dataCacheConnector.save(answers.cacheMap).flatMap(
          _ => {

            val continue = navigator.nextPage(ChooseWhereWorkPage, mode)(answers)
            decisionService.decide(answers, continue, ErrorTemplate("chooseWhereWork.title"))
          }
        )
      }
    )
}
