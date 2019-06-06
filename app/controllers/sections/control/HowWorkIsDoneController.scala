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

import javax.inject.Inject

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.DataCacheConnector
import controllers.BaseController
import controllers.actions._
import controllers.{BaseController, ControllerHelper}
import forms.HowWorkIsDoneFormProvider
import javax.inject.Inject
import models.{HowWorkIsDone, Mode}
import pages.sections.control.HowWorkIsDonePage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.subOptimised.sections.control.HowWorkIsDoneView
import play.api.mvc._
import play.twirl.api.HtmlFormat
import services.CompareAnswerService
import views.html.subOptimised.sections.control.{HowWorkIsDoneView => SubOptimisedHowWorkIsDoneView}

import scala.concurrent.Future

class HowWorkIsDoneController @Inject()(identify: IdentifierAction,
                                        getData: DataRetrievalAction,
                                        requireData: DataRequiredAction,
                                        formProvider: HowWorkIsDoneFormProvider,
                                        controllerComponents: MessagesControllerComponents,
                                        controllerHelper: ControllerHelper,
                                        optimisedView: HowWorkIsDoneView,
                                        subOptimisedView: SubOptimisedHowWorkIsDoneView,
                                        implicit val appConfig: FrontendAppConfig
                                       ) extends BaseController(controllerComponents) with FeatureSwitching {

  val form: Form[HowWorkIsDone] = formProvider()

  private def view(form: Form[HowWorkIsDone], mode: Mode)(implicit request: Request[_]): HtmlFormat.Appendable =
    if(isEnabled(OptimisedFlow)) optimisedView(form, mode) else subOptimisedView(form, mode)

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(HowWorkIsDonePage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => controllerHelper.redirect(mode,value,HowWorkIsDonePage)
    )
  }
}
