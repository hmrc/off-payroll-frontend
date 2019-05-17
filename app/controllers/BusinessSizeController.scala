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

package controllers

import javax.inject.Inject
import play.api.i18n.I18nSupport
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.DataCacheConnector
import controllers.actions._
import config.{FrontendAppConfig, SessionKeys}
import forms.BusinessSizeFormProvider
import models.{Enumerable, Mode, UserType}
import pages.BusinessSizePage
import navigation.Navigator
import pages.sections.setup.WorkerTypePage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CompareAnswerService
import views.html.BusinessSizeView

import scala.concurrent.{ExecutionContext, Future}

class BusinessSizeController @Inject()(
                                      dataCacheConnector: DataCacheConnector,
                                      navigator: Navigator,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      formProvider: BusinessSizeFormProvider,
                                      controllerComponents: MessagesControllerComponents,
                                      view: BusinessSizeView,
                                      implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents) {

  val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val userType = UserType(request.session.get(SessionKeys.userType))
    Ok(view(request.userAnswers.get(BusinessSizePage).fold(form)(answerModel => form.fill(answerModel.answer)), mode, userType))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      values => {
        val answers = CompareAnswerService.constructAnswers(request, values, BusinessSizePage)
        dataCacheConnector.save(answers.cacheMap).map(
          _ => Redirect(navigator.nextPage(BusinessSizePage, mode)(answers))
        )
      }
    )
  }
}
