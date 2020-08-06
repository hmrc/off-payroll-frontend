/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers


import config.featureSwitch.FeatureSwitching
import models._
import models.requests.DataRequest
import pages.QuestionPage
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.libs.json.Format
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController

import scala.concurrent.ExecutionContext

trait BaseController
  extends FrontendBaseController with I18nSupport with Enumerable.Implicits with FeatureSwitching {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  def fillForm[A](page: QuestionPage[A], form: Form[A])(implicit request: DataRequest[_], format: Format[A]): Form[A] =
    request.userAnswers.get(page).fold(form)(answerModel => form.fill(answerModel))
}
