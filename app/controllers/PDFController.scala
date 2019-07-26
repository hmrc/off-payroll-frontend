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
import models.{AdditionalPdfDetails, Mode, NormalMode, Timestamp}
import navigation.Navigator
import pages.{CustomisePDFPage, ResultPage, Timestamp}
import play.api.Logger
import play.api.data.Form
import play.api.mvc._
import play.core.utils.AsciiSet
import play.twirl.api.{Html, HtmlFormat}
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService, EncryptionService, OptimisedDecisionService, PDFService}
import utils.UserAnswersUtils
import views.html.{AddDetailsView, CustomisePDFView}

import scala.concurrent.{ExecutionException, Future}

class PDFController @Inject()(dataCacheConnector: DataCacheConnector,
                              navigator: Navigator,
                              identify: IdentifierAction,
                              getData: DataRetrievalAction,
                              requireData: DataRequiredAction,
                              formProvider: CustomisePDFFormProvider,
                              controllerComponents: MessagesControllerComponents,
                              customisePdfView: CustomisePDFView,
                              addDetailsView: AddDetailsView,
                              decisionService: DecisionService,
                              optimisedDecisionService: OptimisedDecisionService,
                              pdfService: PDFService,
                              errorHandler: ErrorHandler,
                              time: Timestamp,
                              compareAnswerService: CompareAnswerService,
                              checkYourAnswersService: CheckYourAnswersService,
                              encryption: EncryptionService,
                              implicit val appConfig: FrontendAppConfig) extends BaseController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService)

  with FeatureSwitching with UserAnswersUtils {

  def form: Form[AdditionalPdfDetails] = formProvider()

  private def view(form: Form[AdditionalPdfDetails], mode: Mode)(implicit request: Request[_]): HtmlFormat.Appendable =
    if(isEnabled(OptimisedFlow)) addDetailsView(appConfig, form, mode) else customisePdfView(appConfig, form, mode)

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>

    val decryptedForm = request.userAnswers.get(CustomisePDFPage).fold(AdditionalPdfDetails())(x => encryption.decryptDetails(x.answer))

    Ok(view(form.fill(decryptedForm), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>

    if(isEnabled(OptimisedFlow)){

      form.bindFromRequest().fold(
        formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
        additionalData => {

          val encryptedDetails = encryption.encryptDetails(additionalData)

          redirect[AdditionalPdfDetails](NormalMode, encryptedDetails, CustomisePDFPage)
        }
      )

    } else {
      form.bindFromRequest().fold(
        formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
        additionalData => {
          val timestamp = time.timestamp(request.userAnswers.get(Timestamp).map(_.answer))
          printResult(additionalData, timestamp)
        }
      )
    }
  }

  def downloadPDF(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>

    val pdfDetails = request.userAnswers.get(CustomisePDFPage).map(answer => encryption.decryptDetails(answer.answer)).getOrElse(AdditionalPdfDetails())
    val timestamp = time.timestamp(request.userAnswers.get(Timestamp).map(_.answer))

    optimisedPrintResult(pdfDetails, timestamp)
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
      None,
      checkYourAnswersService.sections,
      printMode = true,
      additionalPdfDetails = Some(additionalPdfDetails),
      timestamp = Some(timestamp)
    ).flatMap {
      case Right(html) => generatePdf(html, additionalPdfDetails.fileName)
      case Left(_) => Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
    }
  }

  private def generatePdf(view: Html, reference: Option[String])(implicit request: DataRequest[_]): Future[Result] = {
    if (isEnabled(PrintPDF)) {

      pdfService.generatePdf(view) map {
        case Right(result: SuccessfulPDF) =>

          val fileName = reference.map(reference => reference.replaceAll(",", "").trim)

          val ascii = try {
            fileName.map(fileName => fileName.map(char => AsciiSet.apply(char)))
          } catch {
            case _: Throwable => None
          }

          val default = "result"
          val validFileName = if(ascii.isDefined && ascii.get.mkString.nonEmpty) fileName.getOrElse(default) else default

          Ok(result.pdf.toArray)
            .as("application/pdf")
            .withHeaders("Content-Disposition" -> s"attachment; filename=$validFileName.pdf")

        case _ => InternalServerError(errorHandler.internalServerErrorTemplate)
      }
    } else {
      Future.successful(Ok(view))
    }
  }
}
