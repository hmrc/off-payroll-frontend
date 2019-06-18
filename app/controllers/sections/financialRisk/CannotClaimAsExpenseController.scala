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
import controllers.actions._
import controllers.{BaseController, ControllerHelper}
import forms.CannotClaimAsExpenseFormProvider
import javax.inject.Inject
import models.Mode
import pages.sections.financialRisk.CannotClaimAsExpensePage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.subOptimised.sections.financialRisk.CannotClaimAsExpenseView

import scala.concurrent.Future

class CannotClaimAsExpenseController @Inject()(identify: IdentifierAction,
                                               getData: DataRetrievalAction,
                                               requireData: DataRequiredAction,
                                               formProvider: CannotClaimAsExpenseFormProvider,
                                               controllerComponents: MessagesControllerComponents,
                                               view: CannotClaimAsExpenseView,
                                               controllerHelper: ControllerHelper,
                                               implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents) {

  val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(CannotClaimAsExpensePage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      values => {
        controllerHelper.redirect(mode,values,CannotClaimAsExpensePage,callDecisionService = true)
      }
    )
  }
}
