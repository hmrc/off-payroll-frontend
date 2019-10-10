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

import config.featureSwitch.FeatureSwitching
import config.{FrontendAppConfig, SessionKeys}
import controllers.actions._
import handlers.ErrorHandler
import javax.inject.Inject
import models.{AdditionalPdfDetails, DecisionResponse, Timestamp}
import pages.{CustomisePDFPage, Timestamp}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, EncryptionService, OptimisedDecisionService}
import utils.SessionUtils._
import viewmodels.ResultPrintPreview
import views.html.FinishedCheckingView

class PrintPreviewController @Inject()(identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       controllerComponents: MessagesControllerComponents,
                                       optimisedDecisionService: OptimisedDecisionService,
                                       checkYourAnswersService: CheckYourAnswersService,
                                       finishedCheckingView: FinishedCheckingView,
                                       encryptionService: EncryptionService,
                                       time: Timestamp,
                                       errorHandler: ErrorHandler,
                                       implicit val appConfig: FrontendAppConfig)
  extends BaseController(controllerComponents) with FeatureSwitching {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>

    val pdfDetails = request.userAnswers.get(CustomisePDFPage).map(answer => encryptionService.decryptDetails(answer.answer)).getOrElse(AdditionalPdfDetails())
    val timestamp = time.timestamp(request.userAnswers.get(Timestamp).map(_.answer))

    request.session.getModel[DecisionResponse](SessionKeys.decisionResponse) match {
      case Some(decision) =>
        optimisedDecisionService.determineResultView(
          decision = decision,
          resultMode = ResultPrintPreview,
          answerSections = checkYourAnswersService.sections,
          additionalPdfDetails = Some(pdfDetails),
          timestamp = Some(timestamp)
        )

        match {
          case Right(result) => Ok(result)
          case Left(_) => InternalServerError(errorHandler.internalServerErrorTemplate)
        }
      case _ => InternalServerError(errorHandler.internalServerErrorTemplate)
    }
  }
}
