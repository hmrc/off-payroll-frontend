/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package navigation

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.routes._
import javax.inject.{Inject, Singleton}
import models._
import pages._
import play.api.mvc.Call

@Singleton
class CYANavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private val routeMap:  Map[Page, UserAnswers => Call] = Map(
    CheckYourAnswersPage -> (_ => ResultController.onPageLoad()),
    ResultPage -> { answer => answer.get(ResultPage) match {
          case Some(true) => AddReferenceDetailsController.onPageLoad()
          case _ => PrintPreviewController.onPageLoad()
        }
    },
    AddReferenceDetailsPage -> {
      answer =>
        answer.get(AddReferenceDetailsPage) match {
          case Some(true) => PDFController.onPageLoad(NormalMode)
          case _ => PrintPreviewController.onPageLoad()
        }
    },
    CustomisePDFPage -> (_ => PrintPreviewController.onPageLoad())
  )

  override def nextPage(page: Page, mode: Mode = NormalMode): UserAnswers => Call = routeMap.getOrElse(page, _ => IndexController.onPageLoad())

}
