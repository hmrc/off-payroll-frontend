/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package viewmodels

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import play.api.i18n.Messages
import play.api.mvc.Request
import play.twirl.api.Html

sealed trait AnswerRow {
  val label: String
  def answerHtml(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Html
}

case class SingleAnswerRow(label: String,
                           answer: String,
                           answerIsMessageKey: Boolean,
                           changeUrl: Option[String],
                           changeContextMsgKey: Option[String]) extends AnswerRow with FeatureSwitching {

  override def answerHtml(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Html =
      views.html.components.checkYourAnswers.cya_row(label, answer, answerIsMessageKey, panelIndent = false, changeUrl, changeContextMsgKey)
}

case class MultiAnswerRow(label: String,
                          answers: Seq[SingleAnswerRow],
                          changeUrl: Option[String],
                          changeContextMsgKey: Option[String]) extends AnswerRow with FeatureSwitching {

  override def answerHtml(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Html = {
    val listItems = answers.foldLeft(""){
      case (output, answer) => output + s"<li>${if(answer.answerIsMessageKey) messages(answer.answer) else answer.answer}</li>"
    }
    Html(s"<ul class='no-bullet-pdf'>$listItems</ul>")
  }
}

object AnswerRow {

  def apply(label: String, answer: String, answerIsMessageKey: Boolean) =
    SingleAnswerRow(label, answer, answerIsMessageKey, None, None)

  def apply(label: String, answer: String, answerIsMessageKey: Boolean, changeUrl: Option[String], changeContextMsgKey: Option[String]) =
    SingleAnswerRow(label, answer, answerIsMessageKey, changeUrl, changeContextMsgKey)

  def apply(label: String, answers: Seq[SingleAnswerRow], changeUrl: Option[String], changeContextMsgKey: Option[String]) =
    MultiAnswerRow(label, answers, changeUrl, changeContextMsgKey)

  def apply(label: String, answers: Seq[SingleAnswerRow]) =
    MultiAnswerRow(label, answers, None, None)
}
