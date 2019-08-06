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
import controllers.BaseNavigationController
import controllers.actions._
import forms.ScheduleOfWorkingHoursFormProvider
import javax.inject.Inject
import models.{Mode, ScheduleOfWorkingHours}
import navigation.ControlNavigator
import pages.sections.control.ScheduleOfWorkingHoursPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, _}
import play.twirl.api.HtmlFormat
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import views.html.sections.control.ScheduleOfWorkingHoursView
import views.html.subOptimised.sections.control.{ScheduleOfWorkingHoursView => SubOptimisedScheduleOfWorkingHoursView}

import scala.concurrent.Future

class ScheduleOfWorkingHoursController @Inject()(identify: IdentifierAction,
                                                 getData: DataRetrievalAction,
                                                 requireData: DataRequiredAction,
                                                 formProvider: ScheduleOfWorkingHoursFormProvider,
                                                 controllerComponents: MessagesControllerComponents,
                                                 optimisedView: ScheduleOfWorkingHoursView,
                                                 subOptimisedView: SubOptimisedScheduleOfWorkingHoursView,
                                                 checkYourAnswersService: CheckYourAnswersService,
                                                 compareAnswerService: CompareAnswerService,
                                                 dataCacheConnector: DataCacheConnector,
                                                 decisionService: DecisionService,
                                                 navigator: ControlNavigator,
                                                 implicit val appConfig: FrontendAppConfig) extends BaseNavigationController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) with FeatureSwitching {

  val form: Form[ScheduleOfWorkingHours] = formProvider()

  private def view(form: Form[ScheduleOfWorkingHours], mode: Mode)(implicit request: Request[_]): HtmlFormat.Appendable =
    if (isEnabled(OptimisedFlow)) optimisedView(form, mode) else subOptimisedView(form, mode)

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(ScheduleOfWorkingHoursPage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode,value,ScheduleOfWorkingHoursPage)
    )
  }
}
