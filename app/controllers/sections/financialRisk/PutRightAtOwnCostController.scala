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
import controllers.actions._
import controllers.BaseController
import forms.PutRightAtOwnCostFormProvider
import javax.inject.Inject
import connectors.DataCacheConnector
import models.requests.DataRequest
import models.{Mode, PutRightAtOwnCost}
import navigation.{FinancialRiskNavigator, OldNavigator}
import pages.sections.financialRisk.PutRightAtOwnCostPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import play.twirl.api.HtmlFormat
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import views.html.sections.financialRisk.PutRightAtOwnCostView
import views.html.subOptimised.sections.financialRisk.{PutRightAtOwnCostView => SubOptimisedPutRightAtOwnCostView}

import scala.concurrent.Future

class PutRightAtOwnCostController @Inject()(identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            formProvider: PutRightAtOwnCostFormProvider,
                                            controllerComponents: MessagesControllerComponents,
                                            subOptimisedView: SubOptimisedPutRightAtOwnCostView,
                                            optimisedView: PutRightAtOwnCostView,
                                            checkYourAnswersService: CheckYourAnswersService,
                                            compareAnswerService: CompareAnswerService,
                                            dataCacheConnector: DataCacheConnector,
                                            decisionService: DecisionService,
                                            navigator: FinancialRiskNavigator,
                                            implicit val appConfig: FrontendAppConfig) extends BaseController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) with FeatureSwitching {

  val form: Form[PutRightAtOwnCost] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(PutRightAtOwnCostPage, form), mode))
  }

  private def view(form: Form[PutRightAtOwnCost], mode: Mode)(implicit request: DataRequest[_]): HtmlFormat.Appendable = {
    if(isEnabled(OptimisedFlow)) optimisedView(form, mode) else subOptimisedView(form, mode)
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        redirect(mode,value, PutRightAtOwnCostPage, callDecisionService = true)
      }
    )
  }
}
