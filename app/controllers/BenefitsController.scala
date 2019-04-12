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
import forms.BenefitsFormProvider
import javax.inject.Inject
import models.{DecisionResponse, ExitEnum, Interview, Mode, Score, SetupEnum}
import navigation.Navigator
import pages.BenefitsPage
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.DecisionService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.BenefitsView

import scala.concurrent.{ExecutionContext, Future}

class BenefitsController @Inject()(dataCacheConnector: DataCacheConnector,
                                   navigator: Navigator,
                                   identify: IdentifierAction,
                                   getData: DataRetrievalAction,
                                   requireData: DataRequiredAction,
                                   formProvider: BenefitsFormProvider,
                                   controllerComponents: MessagesControllerComponents,
                                   view: BenefitsView,
                                   decisionService: DecisionService,
                                   implicit val appConfig: FrontendAppConfig
                                  ) extends FrontendController(controllerComponents) with I18nSupport {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  val form: Form[Boolean] = formProvider()

  implicit val headerCarrier = new HeaderCarrier()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(appConfig, request.userAnswers.get(BenefitsPage).fold(form)(form.fill), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(appConfig, formWithErrors, mode))),
      value => {

        val updatedAnswers = request.userAnswers.set(BenefitsPage, value)

        dataCacheConnector.save(updatedAnswers.cacheMap).flatMap(
          _ => {

            val continue = navigator.nextPage(BenefitsPage, mode)(updatedAnswers)
            val exit = continue
            decisionService.decide(updatedAnswers, continue, exit)

          }
        )
      }
    )
  }

  ////            decisionService.decide(Interview(updatedAnswers)(appConfig)).map {
  ////              case Right(DecisionResponse(_, _, Score(Some(SetupEnum.CONTINUE), Some(ExitEnum.CONTINUE), _, _, _, _), _)) =>
  ////                //CONTINUE
  ////
  ////                Redirect(navigator.nextPage(BenefitsPage, mode)(updatedAnswers))
  ////
  ////              case Right(model) =>
  ////                //EXIT
  ////
  ////                Redirect(navigator.nextPage(BenefitsPage, mode)(updatedAnswers))
  ////
  ////              case Left(error) =>
  ////                //ERROR? WHERE LINK TO
  ////                Redirect(navigator.nextPage(BenefitsPage, mode)(updatedAnswers))
  ////            }

}
