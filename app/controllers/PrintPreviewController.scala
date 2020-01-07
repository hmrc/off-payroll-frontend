/*
 * Copyright 2020 HM Revenue & Customs
 *
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
import services.{CheckYourAnswersService, DecisionService, EncryptionService}
import utils.SessionUtils._
import viewmodels.ResultPrintPreview
import views.html.FinishedCheckingView

class PrintPreviewController @Inject()(identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       override val controllerComponents: MessagesControllerComponents,
                                       decisionService: DecisionService,
                                       checkYourAnswersService: CheckYourAnswersService,
                                       finishedCheckingView: FinishedCheckingView,
                                       encryptionService: EncryptionService,
                                       time: Timestamp,
                                       errorHandler: ErrorHandler,
                                       implicit val appConfig: FrontendAppConfig)
  extends BaseController with FeatureSwitching {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>

    val pdfDetails = request.userAnswers.get(CustomisePDFPage).map(answer => encryptionService.decryptDetails(answer)).getOrElse(AdditionalPdfDetails())
    val timestamp = time.timestamp(request.userAnswers.get(Timestamp))

    request.session.getModel[DecisionResponse](SessionKeys.decisionResponse) match {
      case Some(decision) =>
        decisionService.determineResultView(
          decision = decision,
          resultMode = ResultPrintPreview,
          answerSections = checkYourAnswersService.sections,
          additionalPdfDetails = Some(pdfDetails),
          timestamp = Some(timestamp)
        )

        match {
          case Right(result) => Ok(result)
          case Left(_) => Redirect(controllers.routes.StartAgainController.somethingWentWrong())
        }
      case _ => Redirect(controllers.routes.StartAgainController.somethingWentWrong())
    }
  }
}
