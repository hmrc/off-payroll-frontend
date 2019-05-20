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

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.DataCacheConnector
import controllers.BaseController
import controllers.actions._
import forms.{WorkerTypeFormProvider, WorkerUsingIntermediaryFormProvider}
import javax.inject.Inject
import models.requests.DataRequest
import models.{Mode, WorkerType}
import navigation.Navigator
import pages.sections.setup.{WorkerTypePage, WorkerUsingIntermediaryPage}
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import play.twirl.api.Html
import services.{CompareAnswerService, DecisionService}
import views.html.subOptimised.sections.setup.WorkerTypeView
import views.html.sections.setup.WorkerUsingIntermediaryView

import scala.concurrent.Future

class WorkerTypeController @Inject()(dataCacheConnector: DataCacheConnector,
                                     navigator: Navigator,
                                     identify: IdentifierAction,
                                     getData: DataRetrievalAction,
                                     requireData: DataRequiredAction,
                                     workerTypeFormProvider: WorkerTypeFormProvider,
                                     newFormProvider: WorkerUsingIntermediaryFormProvider,
                                     controllerComponents: MessagesControllerComponents,
                                     workerTypeView: WorkerTypeView,
                                     workerUsingIntermediaryView: WorkerUsingIntermediaryView,
                                     decisionService: DecisionService,
                                     implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents) with FeatureSwitching{

  val workerTypeForm: Form[WorkerType] = workerTypeFormProvider()
  val workerUsingIntermediaryForm: Form[Boolean] = newFormProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    if(isEnabled(OptimisedFlow)) submitWorkerUsingIntermediary(mode) else submitWorkerType(mode)
  }

  private[controllers] def view(mode: Mode)(implicit request: DataRequest[_]):Html = if(isEnabled(OptimisedFlow)) {
    workerUsingIntermediaryView(request.userAnswers.get(WorkerUsingIntermediaryPage).fold(workerUsingIntermediaryForm)
    (answerModel => workerUsingIntermediaryForm.fill(answerModel.answer)), mode)
  } else {
    workerTypeView(request.userAnswers.get(WorkerTypePage).fold(workerTypeForm)(answerModel => workerTypeForm.fill(answerModel.answer)), mode)
  }

  private[controllers] def submitWorkerType(mode: Mode)(implicit request: DataRequest[AnyContent]): Future[Result] =
    workerTypeForm.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(workerTypeView(formWithErrors, mode))),
      value => {
        val answers = CompareAnswerService.constructAnswers(request,value, WorkerTypePage)
        dataCacheConnector.save(answers.cacheMap).map(
          _ => Redirect(navigator.nextPage(WorkerTypePage, mode)(answers))
        )
      }
    )

  private[controllers] def submitWorkerUsingIntermediary(mode: Mode)(implicit request: DataRequest[AnyContent]): Future[Result] =
    workerUsingIntermediaryForm.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(workerUsingIntermediaryView(formWithErrors, mode))),
      value => {
        val answers = CompareAnswerService.constructAnswers(request,value, WorkerUsingIntermediaryPage)
        dataCacheConnector.save(answers.cacheMap).map(
          _ => Redirect(navigator.nextPage(WorkerUsingIntermediaryPage, mode)(answers))
        )
      }
    )
}
