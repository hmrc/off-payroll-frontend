/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package viewmodels

import config.FrontendAppConfig
import models.Section.SectionEnum
import play.api.i18n.Messages
import play.api.mvc.Request
import play.twirl.api.Html

case class Accordion(sections: Seq[AccordionSection]) {

  def html(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig) =
    views.html.components.accordion.accordion(sections)
}

case class AccordionSection(section: SectionEnum,
                            headingKey: String,
                            body: Html,
                            expanded: Boolean = false) {

  def html(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig) =
    views.html.components.accordion.accordion_section(section.toString, headingKey, body, expanded)
}

object Accordion {
  def apply(sections: Seq[AnswerSection], sectionToExpand: Option[SectionEnum])
           (implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Accordion = new Accordion(
    sections = sections.map(section => AccordionSection(
      section.section,
      section.headingKey,
      CheckYourAnswersSection(section.rows.map(_._1)).html,
      sectionToExpand.contains(section.section)
    ))
  )
}
