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
import config.featureSwitch.{FeatureSwitching, PrintPDF}
import connectors.DataCacheConnector
import connectors.httpParsers.PDFGeneratorHttpParser.SuccessfulPDF
import controllers.actions._
import forms.CustomisePDFFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.requests.DataRequest
import models.{AdditionalPdfDetails, Answers, Mode, Timestamp}
import navigation.Navigator
import pages.{CustomisePDFPage, ResultPage}
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{DecisionService, PDFService}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.UserAnswersUtils
import views.html.CustomisePDFView
import models.Answers._

import scala.concurrent.{ExecutionContext, Future}

class PDFController @Inject()(dataCacheConnector: DataCacheConnector,
                              navigator: Navigator,
                              identify: IdentifierAction,
                              getData: DataRetrievalAction,
                              requireData: DataRequiredAction,
                              formProvider: CustomisePDFFormProvider,
                              controllerComponents: MessagesControllerComponents,
                              customisePdfView: CustomisePDFView,
                              decisionService: DecisionService,
                              pdfService: PDFService,
                              errorHandler: ErrorHandler,
                              implicit val appConfig: FrontendAppConfig
                             ) extends FrontendController(controllerComponents) with I18nSupport with FeatureSwitching with UserAnswersUtils {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(customisePdfView(appConfig, request.userAnswers.get(CustomisePDFPage).fold(form)(answerModel => form.fill(answerModel.answer)), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(customisePdfView(appConfig, formWithErrors, mode))),
      additionalData => {
        val timestamp = request.userAnswers.get(ResultPage)
        printResult(additionalData, if(timestamp.isDefined) timestamp.get.answer else Timestamp.timestamp)
      }
    )
  }

  private def printResult(additionalPdfDetails: AdditionalPdfDetails, timestamp: String)(implicit request: DataRequest[_]): Future[Result] = {

    if (isEnabled(PrintPDF)) {

      pdfService.generatePdf(decisionService.determineResultView(answers, printMode = true, additionalPdfDetails = Some(additionalPdfDetails),
        timestamp = Some(timestamp))) map {
        case Right(result: SuccessfulPDF) => {
          val fileName = additionalPdfDetails.reference.getOrElse("result")
          Ok(result.pdf.toArray)
            .as("application/pdf")
            .withHeaders("Content-Disposition" -> s"attachment; filename=$fileName.pdf")
        }
        case _ => InternalServerError(errorHandler.internalServerErrorTemplate)
      }
    } else {
      Future.successful(Ok(decisionService.determineResultView(answers)))
    }
  }
}
