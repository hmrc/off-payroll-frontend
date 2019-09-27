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

package controllers.sections.exit

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.exit.OfficeHolderFormProvider
import javax.inject.Inject
import models.requests.DataRequest
import models.{CheckMode, Mode, NormalMode}
import navigation.ExitNavigator
import pages.sections.exit.OfficeHolderPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Request}
import play.twirl.api.HtmlFormat
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import views.html.sections.exit.OfficeHolderView
import views.html.subOptimised.sections.exit.{OfficeHolderView => SubOptimisedOfficeHolderView}

import scala.concurrent.Future

class OfficeHolderController @Inject()(identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: OfficeHolderFormProvider,
                                       controllerComponents: MessagesControllerComponents,
                                       optimisedView: OfficeHolderView,
                                       subOptimisedView: SubOptimisedOfficeHolderView,
                                       checkYourAnswersService: CheckYourAnswersService,
                                       compareAnswerService: CompareAnswerService,
                                       dataCacheConnector: DataCacheConnector,
                                       decisionService: DecisionService,
                                       navigator: ExitNavigator,
                                       implicit val appConfig: FrontendAppConfig) extends BaseNavigationController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) with FeatureSwitching {

  private def view(form: Form[Boolean], mode: Mode)(implicit request: DataRequest[_]): HtmlFormat.Appendable =
    if(isEnabled(OptimisedFlow)) optimisedView(form, mode) else subOptimisedView(form, mode)

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(OfficeHolderPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        val currentAnswer = request.userAnswers.getAnswer(OfficeHolderPage)
        val overrideMode = if(mode == CheckMode && !value && currentAnswer.contains(true)) NormalMode else mode
        redirect[Boolean](overrideMode, value, OfficeHolderPage, callDecisionService = true)
      }
    )
  }
}
