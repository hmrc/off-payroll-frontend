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
import connectors.HttpParsers.PDFGeneratorHttpParser.SuccessfulPDF
import controllers.actions._
import forms.CustomisePDFFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.{AdditionalPdfDetails, Mode}
import models.requests.DataRequest
import navigation.Navigator
import pages.CustomisePDFPage
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.PDFService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.CheckYourAnswersHelper
import viewmodels.AnswerSection
import views.html.CustomisePDFView
import views.html.results._

import scala.concurrent.{ExecutionContext, Future}

class CustomisePDFController @Inject()(implicit appConfig: FrontendAppConfig,
                                       dataCacheConnector: DataCacheConnector,
                                       navigator: Navigator,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: CustomisePDFFormProvider,
                                       controllerComponents: MessagesControllerComponents,
                                       view: CustomisePDFView,
                                       officeHolderInsideIR35View: OfficeHolderInsideIR35View,
                                       officeHolderEmployedView: OfficeHolderEmployedView,
                                       currentSubstitutionView: CurrentSubstitutionView,
                                       futureSubstitutionView: FutureSubstitutionView,
                                       selfEmployedView: SelfEmployedView,
                                       employedView: EmployedView,
                                       controlView: ControlView,
                                       financialRiskView: FinancialRiskView,
                                       indeterminateView: IndeterminateView,
                                       insideIR35View: InsideIR35View,
                                       pdfService: PDFService,
                                       errorHandler: ErrorHandler
                                      ) extends FrontendController(controllerComponents) with I18nSupport with FeatureSwitching {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  val form = formProvider()

  //noinspection ScalaStyle
  private def answers(implicit request: DataRequest[_]): Seq[AnswerSection] = {
    val checkYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)

    Seq(
      AnswerSection(
        headingKey = Some("result.peopleInvolved.h2"),
        rows = Seq(
          checkYourAnswersHelper.aboutYou,
          checkYourAnswersHelper.contractStarted,
          checkYourAnswersHelper.workerType
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("result.workersDuties.h2"),
        rows = Seq(
          checkYourAnswersHelper.officeHolder
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("result.substitutesHelpers.h2"),
        rows = Seq(
          checkYourAnswersHelper.arrangedSubstitue,
          checkYourAnswersHelper.didPaySubstitute,
          checkYourAnswersHelper.rejectSubstitute,
          checkYourAnswersHelper.wouldWorkerPaySubstitute,
          checkYourAnswersHelper.neededToPayHelper
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("result.workArrangements.h2"),
        rows = Seq(
          checkYourAnswersHelper.moveWorker,
          checkYourAnswersHelper.howWorkIsDone,
          checkYourAnswersHelper.scheduleOfWorkingHours,
          checkYourAnswersHelper.chooseWhereWork
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("result.financialRisk.h2"),
        rows = Seq(
          checkYourAnswersHelper.cannotClaimAsExpense,
          checkYourAnswersHelper.howWorkerIsPaid,
          checkYourAnswersHelper.putRightAtOwnCost
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("result.partAndParcel.h2"),
        rows = Seq(
          checkYourAnswersHelper.benefits,
          checkYourAnswersHelper.lineManagerDuties,
          checkYourAnswersHelper.interactWithStakeholders,
          checkYourAnswersHelper.identifyToStakeholders
        ).flatten,
        useProgressiveDisclosure = true
      )
    )
  }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(appConfig, request.userAnswers.get(CustomisePDFPage).fold(form)(form.fill), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(appConfig, formWithErrors, mode))),
      additionalData => printResult(additionalData)
    )
  }

  def printResult(additionalPdfDetails: AdditionalPdfDetails)(implicit request: DataRequest[_]): Future[Result] = {

    val pdfView = officeHolderInsideIR35View(
      appConfig = appConfig,
      answerSections = answers,
      version = appConfig.decisionVersion,
      form = form,
      postAction = routes.ResultController.onSubmit(),
      printView = true,
      additionalPdfDetails = Some(additionalPdfDetails)
    )

    if (isEnabled(PrintPDF)) {
      pdfService.generatePdf(pdfView) map {
        case Right(result: SuccessfulPDF) => {
          val fileName = additionalPdfDetails.reference.getOrElse("result")
          Ok(result.pdf)
            .as("application/pdf")
            .withHeaders("Content-Disposition" -> s"attachment; filename=$fileName.pdf")
        }
        case _ => InternalServerError(errorHandler.internalServerErrorTemplate)
      }
    } else {
      Future.successful(Ok(pdfView))
    }
  }
}
