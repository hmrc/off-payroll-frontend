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

package controllers.sections.financialRisk

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.DataCacheConnector
import controllers.BaseController
import controllers.actions._
import forms.HowWorkerIsPaidFormProvider
import javax.inject.Inject
import models.requests.DataRequest
import models.{HowWorkerIsPaid, Mode}
import navigation.FinancialRiskNavigator
import pages.sections.financialRisk.HowWorkerIsPaidPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import play.twirl.api.HtmlFormat
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import views.html.sections.financialRisk.HowWorkerIsPaidView
import views.html.subOptimised.sections.financialRisk.{HowWorkerIsPaidView => SubOptimisedHowWorkerIsPaidView}

import scala.concurrent.Future

class HowWorkerIsPaidController @Inject()(identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: HowWorkerIsPaidFormProvider,
                                          controllerComponents: MessagesControllerComponents,
                                          subOptimisedView: SubOptimisedHowWorkerIsPaidView,
                                          optimisedView: HowWorkerIsPaidView,
                                          checkYourAnswersService: CheckYourAnswersService,
                                          compareAnswerService: CompareAnswerService,
                                          dataCacheConnector: DataCacheConnector,
                                          decisionService: DecisionService,
                                          navigator: FinancialRiskNavigator,
                                          implicit val appConfig: FrontendAppConfig) extends BaseController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) with FeatureSwitching {

  val form: Form[HowWorkerIsPaid] = formProvider()

  private def view(form: Form[HowWorkerIsPaid], mode: Mode)(implicit request: DataRequest[_]): HtmlFormat.Appendable = {
    if(isEnabled(OptimisedFlow)) optimisedView(form, mode) else subOptimisedView(form, mode)
  }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(HowWorkerIsPaidPage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode,value,HowWorkerIsPaidPage)
    )
  }
}
