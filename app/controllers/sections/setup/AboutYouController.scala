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

import javax.inject.Inject

import config.featureSwitch.FeatureSwitching
import config.{FrontendAppConfig, SessionKeys}
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.setup.{AboutYouFormProvider, WhichDescribesYouFormProvider}
import models.requests.DataRequest
import models.sections.setup.{AboutYouAnswer, WhichDescribesYouAnswer}
import models.{Mode, UserType}
import navigation.SetupNavigator
import pages.sections.setup.{AboutYouPage, WhichDescribesYouPage}
import play.api.data.Form
import play.api.mvc._
import services.{CheckYourAnswersService, CompareAnswerService}
import utils.SessionUtils._
import views.html.sections.setup.WhichDescribesYouView

import scala.concurrent.Future

class AboutYouController @Inject()(identify: IdentifierAction,
                                   getData: DataRetrievalAction,
                                   requireData: DataRequiredAction,
                                   aboutYouFormProvider: AboutYouFormProvider,
                                   whichDescribesYouFormProvider: WhichDescribesYouFormProvider,
                                   controllerComponents: MessagesControllerComponents,
                                   whichDescribesYouView: WhichDescribesYouView,
                                   checkYourAnswersService: CheckYourAnswersService,
                                   compareAnswerService: CompareAnswerService,
                                   dataCacheConnector: DataCacheConnector,
                                   navigator: SetupNavigator,
                                   implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController(controllerComponents, compareAnswerService, dataCacheConnector, navigator) with FeatureSwitching {

  val form: Form[AboutYouAnswer] = aboutYouFormProvider()
  val whichDescribedForm: Form[WhichDescribesYouAnswer] = whichDescribesYouFormProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    submitWhichDescribesYou(mode)
  }

  private[controllers] def view(mode: Mode)(implicit request: DataRequest[_]) = {
    whichDescribesYouView(request.userAnswers.get(WhichDescribesYouPage)
      .fold(whichDescribedForm)(answerModel => whichDescribedForm.fill(answerModel.answer)), mode)
  }

  private[controllers] def submitWhichDescribesYou(mode: Mode)(implicit request: DataRequest[AnyContent]): Future[Result] =
    whichDescribedForm.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(whichDescribesYouView(formWithErrors, mode))),
      value => {
        redirect(mode, value, WhichDescribesYouPage).map(result => result.addingToSession(SessionKeys.userType -> UserType(value)))
      }
    )
}
