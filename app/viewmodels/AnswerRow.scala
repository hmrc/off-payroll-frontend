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

package viewmodels

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
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
    if(isEnabled(OptimisedFlow)) {
      views.html.components.checkYourAnswers.cya_row(label, answer, answerIsMessageKey, panelIndent = false, changeUrl, changeContextMsgKey)
    } else {
      Html(if(answerIsMessageKey) messages(answer) else answer)
    }
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
