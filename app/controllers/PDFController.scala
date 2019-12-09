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

import config.featureSwitch.{FeatureSwitching, PrintPDF}
import config.{FrontendAppConfig, SessionKeys}
import connectors.DataCacheConnector
import connectors.httpParsers.PDFGeneratorHttpParser.SuccessfulPDF
import controllers.actions._
import forms.AdditionalPdfDetailsFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models._
import models.requests.DataRequest
import navigation.CYANavigator
import pages.{CustomisePDFPage, Timestamp}
import play.api.data.Form
import play.api.mvc._
import play.core.utils.AsciiSet
import play.twirl.api.Html
import services._
import utils.SessionUtils._
import utils.{SourceUtil, UserAnswersUtils}
import viewmodels.ResultPDF
import views.html.{AddDetailsView, CustomisePDFView}

import scala.concurrent.Future

class PDFController @Inject()(override val dataCacheConnector: DataCacheConnector,
                              override val navigator: CYANavigator,
                              identify: IdentifierAction,
                              getData: DataRetrievalAction,
                              requireData: DataRequiredAction,
                              formProvider: AdditionalPdfDetailsFormProvider,
                              override val controllerComponents: MessagesControllerComponents,
                              customisePdfView: CustomisePDFView,
                              view: AddDetailsView,
                              decisionService: DecisionService,
                              pdfService: PDFService,
                              errorHandler: ErrorHandler,
                              time: Timestamp,
                              override val compareAnswerService: CompareAnswerService,
                              checkYourAnswersService: CheckYourAnswersService,
                              encryption: EncryptionService,
                              source: SourceUtil,
                              implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController

    with FeatureSwitching with UserAnswersUtils {

  def form: Form[AdditionalPdfDetails] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>

    val decryptedForm = request.userAnswers.get(CustomisePDFPage).fold(AdditionalPdfDetails())(answer => encryption.decryptDetails(answer))

    Ok(view(form.fill(decryptedForm), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      additionalData => {

        val encryptedDetails = encryption.encryptDetails(additionalData)

        redirect[AdditionalPdfDetails](NormalMode, encryptedDetails, CustomisePDFPage)
      }
    )
  }
  def downloadPDF(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>

    val decisionResponse = request.session.getModel[DecisionResponse](SessionKeys.decisionResponse)
    val pdfDetails = request.userAnswers.get(CustomisePDFPage).map(answer => encryption.decryptDetails(answer)).getOrElse(AdditionalPdfDetails())
    val timestamp = time.timestamp(request.userAnswers.get(Timestamp))

    decisionResponse match {
      case Some(decision) => printResult(decision, pdfDetails, timestamp)
      case _ => Future.successful(Redirect(controllers.routes.StartAgainController.somethingWentWrong()))
    }
  }
  private def printResult(decision: DecisionResponse, additionalPdfDetails: AdditionalPdfDetails, timestamp: String)
                         (implicit request: DataRequest[_]): Future[Result] = {
    decisionService.determineResultView(
      decision,
      None,
      checkYourAnswersService.sections,
      ResultPDF,
      additionalPdfDetails = Some(additionalPdfDetails),
      timestamp = Some(timestamp)
    ) match {
      case Right(html) => generatePdf(html, additionalPdfDetails.fileName)
      case Left(_) => Future.successful(Redirect(controllers.routes.StartAgainController.somethingWentWrong()))
    }
  }

  private def generatePdf(view: Html, reference: Option[String])(implicit request: DataRequest[_]): Future[Result] = {

    val css = source.fromURL(controllers.routes.Assets.versioned("stylesheets/print_pdf.css").absoluteURL).mkString
    val printHtml = Html(view.toString
      .replace("<head>", s"<head><style>$css</style>")
    )
    if (isEnabled(PrintPDF)) {
      pdfService.generatePdf(printHtml) map {
        case Right(result: SuccessfulPDF) =>

          val fileName = reference.map(reference => reference.replaceAll(",", "").trim)

          val ascii = try {
            fileName.map(fileName => fileName.map(char => AsciiSet.apply(char)))
          } catch {
            case _: Throwable => None
          }
          val default = "result"
          val validFileName = if (ascii.isDefined && ascii.get.mkString.nonEmpty) fileName.getOrElse(default) else default
          Ok(result.pdf.toArray)
            .as("application/pdf")
            .withHeaders("Content-Disposition" -> s"attachment; filename=$validFileName.pdf")
        case _ => InternalServerError(errorHandler.internalServerErrorTemplate)
      }
    } else {
      Future.successful(Ok(printHtml))
    }
  }
}