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
import pages.{CustomisePDFPage, ResultPage, Timestamp}
import play.api.data.Form
import play.api.mvc._
import play.twirl.api.HtmlFormat
import services.{DecisionService, PDFService}
import utils.UserAnswersUtils
import views.html.{AddDetailsView, CustomisePDFView}

import scala.concurrent.Future

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
                              pdfService: PDFService,
                              errorHandler: ErrorHandler,
                              time: Timestamp,
                              implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents)
  with FeatureSwitching with UserAnswersUtils {

  val form: Form[AdditionalPdfDetails] = formProvider()

  private def view(form: Form[AdditionalPdfDetails], mode: Mode)(implicit request: Request[_]): HtmlFormat.Appendable =
    if(isEnabled(OptimisedFlow)) addDetailsView(appConfig, form, mode) else customisePdfView(appConfig, form, mode)


  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(CustomisePDFPage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      additionalData => {
        val timestamp = request.userAnswers.get(Timestamp)
        printResult(additionalData, time.timestamp(timestamp.map(_.answer)))
      }
    )
  }

  private def printResult(additionalPdfDetails: AdditionalPdfDetails, timestamp: String)(implicit request: DataRequest[_]): Future[Result] = {
    lazy val view = decisionService.determineResultView(answers, printMode = true, additionalPdfDetails = Some(additionalPdfDetails),
      timestamp = Some(timestamp))
    if (isEnabled(PrintPDF)) {
      pdfService.generatePdf(view) map {
        case Right(result: SuccessfulPDF) => {
          val fileName = additionalPdfDetails.reference.getOrElse("result")
          Ok(result.pdf.toArray)
            .as("application/pdf")
            .withHeaders("Content-Disposition" -> s"attachment; filename=$fileName.pdf")
        }
        case _ => InternalServerError(errorHandler.internalServerErrorTemplate)
      }
    } else {
      Future.successful(Ok(view))
    }
  }
}
