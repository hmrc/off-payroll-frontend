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
import play.api.i18n.Messages
import play.api.mvc.Request
import play.twirl.api.Html

sealed trait AnswerRow {
  val label: String
  val answerIsMessageKey: Boolean
  val changeUrl: String
  def answerHtml(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Html
}

case class SingleAnswerRow(label: String,
                           answer: String,
                           answerIsMessageKey: Boolean,
                           changeUrl: String) extends AnswerRow {

  override def answerHtml(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Html =
    Html(if(answerIsMessageKey) messages(answer) else answer)
}

case class MultiAnswerRow(label: String,
                          answers: Seq[String],
                          answerIsMessageKey: Boolean,
                          changeUrl: String) extends AnswerRow {

  override def answerHtml(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Html = {
    val listItems = answers.foldLeft(""){
      case (output, answer) => output + s"<li class='no-bullet-pdf'>${if(answerIsMessageKey) messages(answer) else answer}</li>"
    }
    Html(s"<ul>$listItems</ul>")
  }
}

object AnswerRow {

  def apply(label: String, answer: String, answerIsMessageKey: Boolean, changeUrl: String) =
    SingleAnswerRow(label, answer, answerIsMessageKey, changeUrl)

  def apply(label: String, answer: Seq[String], answerIsMessageKey: Boolean, changeUrl: String) =
    MultiAnswerRow(label, answer, answerIsMessageKey, changeUrl)
}