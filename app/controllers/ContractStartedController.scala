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

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.ContractStartedFormProvider
import javax.inject.Inject
import services.CompareAnswerService

import models.Mode
import navigation.Navigator
import pages.ContractStartedPage
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CompareAnswerService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.ContractStartedView

import scala.concurrent.{ExecutionContext, Future}

class ContractStartedController @Inject()(dataCacheConnector: DataCacheConnector,
                                          navigator: Navigator,
                                          identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: ContractStartedFormProvider,
                                          controllerComponents: MessagesControllerComponents,
                                          view: ContractStartedView,
                                          implicit val appConfig: FrontendAppConfig
                                         ) extends FrontendController(controllerComponents) with I18nSupport with CompareAnswerService[Boolean]{

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  val form: Form[Boolean] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(appConfig, request.userAnswers.get(ContractStartedPage).fold(form)(form.fill), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(appConfig, formWithErrors, mode))),
      value => {
        val answers = constructAnswers(request,value,ContractStartedPage)
        dataCacheConnector.save(answers.cacheMap).map(
            _ => Redirect(navigator.nextPage(ContractStartedPage, mode)(answers))
          )
        }
    )
  }

}
