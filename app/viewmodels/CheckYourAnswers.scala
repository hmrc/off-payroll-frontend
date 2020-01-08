/*
 * Copyright 2020 HM Revenue & Customs
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
import models.Section.SectionEnum
import play.api.i18n.Messages
import play.api.mvc.Request


case class CheckYourAnswers(sections: Seq[CheckYourAnswersSection])

case class CheckYourAnswersSection(rows: Seq[CheckYourAnswersRow], section: Option[SectionEnum] = None, headingKey: Option[String] = None) {
  def html(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig) = {
    views.html.components.checkYourAnswers.cya_section(rows, section, headingKey)
  }
}

case class CheckYourAnswersRow(question: String,
                                answer: String,
                                answerIsMessageKey: Boolean,
                                panelIndent: Boolean = false,
                                changeUrl: Option[String],
                                changeContextMsgKey: Option[String]) {

  def html(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig) =
    views.html.components.checkYourAnswers.cya_row(question, answer, answerIsMessageKey, panelIndent, changeUrl, changeContextMsgKey)
}

object CheckYourAnswers {
  def apply(sections: Seq[AnswerSection])(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): CheckYourAnswers =
    CheckYourAnswers(sections.map(section => {
      CheckYourAnswersSection(
        section.rows.map(_._1),
        section.section,
        section.headingKey
      )
    }))
}

object CheckYourAnswersSection {

  def constructModel(rows: Seq[AnswerRow], section: Option[SectionEnum] = None, headingKey: Option[String] = None) =
    CheckYourAnswersSection(
      rows = rows.collect {
        case row: SingleAnswerRow => CheckYourAnswersRow(
          question = row.label,
          answer = row.answer,
          answerIsMessageKey = row.answerIsMessageKey,
          changeUrl = row.changeUrl,
          changeContextMsgKey = row.changeContextMsgKey
        )
      },
      section,
      headingKey
    )

  def apply(rows: Seq[AnswerRow])(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): CheckYourAnswersSection =
    constructModel(rows)

  def apply(rows: Seq[AnswerRow], section: SectionEnum, headingKey: String)
           (implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): CheckYourAnswersSection = {
    constructModel(rows, Some(section), Some(headingKey))
  }
}
