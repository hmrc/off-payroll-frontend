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
import config.featureSwitch.{FeatureSwitching, OptimisedFlow, PrintPDF}
import connectors.DataCacheConnector
import connectors.httpParsers.PDFGeneratorHttpParser.SuccessfulPDF
import controllers.actions._
import forms.CustomisePDFFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Answers._
import models.requests.DataRequest
import models.{AdditionalPdfDetails, Mode, Timestamp}
import navigation.Navigator
import pages.{CustomisePDFPage, ResultPage}
import play.api.mvc._
import play.twirl.api.Html
import services._
import utils.UserAnswersUtils
import views.html.CustomisePDFView

import scala.concurrent.Future

class PDFController @Inject()(dataCacheConnector: DataCacheConnector,
                              navigator: Navigator,
                              identify: IdentifierAction,
                              getData: DataRetrievalAction,
                              requireData: DataRequiredAction,
                              formProvider: CustomisePDFFormProvider,
                              controllerComponents: MessagesControllerComponents,
                              customisePdfView: CustomisePDFView,
                              decisionService: DecisionService,
                              optimisedDecisionService: OptimisedDecisionService,
                              pdfService: PDFService,
                              errorHandler: ErrorHandler,
                              time: Timestamp,
                              checkYourAnswersService: CheckYourAnswersService,
                              compareAnswerService: CompareAnswerService,
                              implicit val appConfig: FrontendAppConfig) extends BaseController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) with FeatureSwitching with UserAnswersUtils {

  val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(customisePdfView(appConfig, fillForm(CustomisePDFPage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(customisePdfView(appConfig, formWithErrors, mode))),
      additionalData => {
        val timestamp = time.timestamp(request.userAnswers.get(ResultPage).map(_.answer))
        if(isEnabled(OptimisedFlow)) {
          optimisedPrintResult(additionalData, timestamp)
        } else {
          printResult(additionalData, timestamp)
        }
      }
    )
  }

  private def printResult(additionalPdfDetails: AdditionalPdfDetails, timestamp: String)(implicit request: DataRequest[_]): Future[Result] = {
    val html = decisionService.determineResultView(
      answers,
      printMode = true,
      additionalPdfDetails = Some(additionalPdfDetails),
      timestamp = Some(timestamp)
    )
    generatePdf(html, additionalPdfDetails.reference)
  }

  private def optimisedPrintResult(additionalPdfDetails: AdditionalPdfDetails, timestamp: String)(implicit request: DataRequest[_]): Future[Result] = {
    optimisedDecisionService.determineResultView(
      controllers.routes.ResultController.onSubmit(),
      checkYourAnswersService.sections,
      printMode = true,
      additionalPdfDetails = Some(additionalPdfDetails),
      timestamp = Some(timestamp)
    ).flatMap {
      case Right(html) => generatePdf(html, additionalPdfDetails.reference)
      case Left(_) => Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
    }
  }

  private def generatePdf(view: Html, reference: Option[String])(implicit request: DataRequest[_]): Future[Result] = {
    if (isEnabled(PrintPDF)) {
      val filename = reference.fold("result")(_)
      pdfService.generatePdf(view) map {
        case Right(result: SuccessfulPDF) =>
          Ok(result.pdf.toArray)
            .as("application/pdf")
            .withHeaders("Content-Disposition" -> s"attachment; filename=$filename.pdf")
        case _ => InternalServerError(errorHandler.internalServerErrorTemplate)
      }
    } else {
      Future.successful(Ok(view))
    }
  }
}
