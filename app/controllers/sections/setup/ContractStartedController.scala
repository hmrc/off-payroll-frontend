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
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.setup.ContractStartedFormProvider
import javax.inject.Inject
import models.Mode
import models.requests.DataRequest
import navigation.SetupNavigator
import pages.sections.setup.ContractStartedPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}

import scala.concurrent.Future

class ContractStartedController @Inject()(identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: ContractStartedFormProvider,
                                          override val controllerComponents: MessagesControllerComponents,
                                          view: views.html.sections.setup.ContractStartedView,
                                          checkYourAnswersService: CheckYourAnswersService,
                                          override val compareAnswerService: CompareAnswerService,
                                          override val dataCacheConnector: DataCacheConnector,
                                          override val navigator: SetupNavigator,
                                          implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  def form(implicit request: DataRequest[_]): Form[Boolean] = formProvider()

  def renderView(mode: Mode, oForm: Option[Form[Boolean]] = None)(implicit request: DataRequest[_]) = {
    val formData = oForm.getOrElse(fillForm(ContractStartedPage, form))
    view(formData, mode)
  }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(request.userAnswers.get(ContractStartedPage).fold(form)(answerModel => form.fill(answerModel)), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(renderView(mode, Some(formWithErrors)))),
      value => redirect(mode,value,ContractStartedPage)
    )
  }
}
