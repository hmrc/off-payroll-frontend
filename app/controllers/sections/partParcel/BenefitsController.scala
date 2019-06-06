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

package controllers.sections.partParcel

import javax.inject.Inject

import config.FrontendAppConfig
import controllers.actions._
import controllers.{BaseController, ControllerHelper}
import forms.BenefitsFormProvider
import models.{ErrorTemplate, Mode}
import pages.sections.partParcel.BenefitsPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.subOptimised.sections.partParcel.BenefitsView

import scala.concurrent.Future

class BenefitsController @Inject()(identify: IdentifierAction,
                                   getData: DataRetrievalAction,
                                   requireData: DataRequiredAction,
                                   formProvider: BenefitsFormProvider,
                                   controllerComponents: MessagesControllerComponents,
                                   view: BenefitsView,
                                   controllerHelper: ControllerHelper,
                                   implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents) {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(BenefitsPage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        controllerHelper.redirect(mode,value,BenefitsPage,callDecisionService = true)
      }
    )
  }
}
