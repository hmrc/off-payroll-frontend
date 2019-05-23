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

import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import config.{FrontendAppConfig, SessionKeys}
import connectors.DataCacheConnector
import controllers.BaseController
import controllers.actions._
import forms.{AboutYouFormProvider, WhichDescribesYouFormProvider}
import javax.inject.Inject
import models.Answers._
import models.requests.DataRequest
import models.{AboutYouAnswer, Mode, UserType, WhichDescribesYouAnswer}
import navigation.Navigator
import pages.sections.setup.{AboutYouPage, WhichDescribesYouPage}
import play.api.data.Form
import play.api.mvc._
import services.CompareAnswerService
import utils.SessionUtils._
import views.html.sections.setup.WhichDescribesYouView
import views.html.subOptimised.sections.setup.AboutYouView

import scala.concurrent.Future

class AboutYouController @Inject()(dataCacheConnector: DataCacheConnector,
                                   navigator: Navigator,
                                   identify: IdentifierAction,
                                   getData: DataRetrievalAction,
                                   requireData: DataRequiredAction,
                                   aboutYouFormProvider: AboutYouFormProvider,
                                   whichDescribesYouFormProvider: WhichDescribesYouFormProvider,
                                   controllerComponents: MessagesControllerComponents,
                                   aboutYouView: AboutYouView,
                                   whichDescribesYouView: WhichDescribesYouView,
                                   implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents) with FeatureSwitching {
  
  val form: Form[AboutYouAnswer] = aboutYouFormProvider()
  val whichDescribedForm: Form[WhichDescribesYouAnswer] = whichDescribesYouFormProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    if(isEnabled(OptimisedFlow)) submitWhichDescribesYou(mode) else submitAboutYou(mode)
  }

  private[controllers] def view(mode: Mode)(implicit request: DataRequest[_]) = if(isEnabled(OptimisedFlow)) {
    whichDescribesYouView(request.userAnswers.get(WhichDescribesYouPage)
      .fold(whichDescribedForm)(answerModel => whichDescribedForm.fill(answerModel.answer)), mode)
  } else {
    aboutYouView(request.userAnswers.get(AboutYouPage)
      .fold(form)(answerModel => form.fill(answerModel.answer)), mode)
  }

  private[controllers] def submitWhichDescribesYou(mode: Mode)(implicit request: DataRequest[AnyContent]): Future[Result] =
    whichDescribedForm.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(whichDescribesYouView(formWithErrors, mode))),
      value => {
        val answers = CompareAnswerService.constructAnswers(request,value, WhichDescribesYouPage)
        dataCacheConnector.save(answers.cacheMap).map(
          _ => Redirect(navigator.nextPage(WhichDescribesYouPage, mode)(answers)).addingToSession(SessionKeys.userType -> UserType(value))
        )
      }
    )

  private[controllers] def submitAboutYou(mode: Mode)(implicit request: DataRequest[AnyContent]): Future[Result] =
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(aboutYouView(formWithErrors, mode))),
      value => {
        val answers = CompareAnswerService.constructAnswers(request,value,AboutYouPage)
        dataCacheConnector.save(answers.cacheMap).map(
          _ => Redirect(navigator.nextPage(AboutYouPage, mode)(answers)).addingToSession(SessionKeys.userType -> UserType(value))
        )
      }
    )
}