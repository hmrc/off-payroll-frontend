/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package viewmodels

import models.Section.SectionEnum
import play.twirl.api.Html

case class AnswerSection(headingKey: String,
                         whyResult: Option[Html] = None,
                         rows: Seq[(AnswerRow, Option[Html])],
                         useProgressiveDisclosure: Boolean = false,
                         section: SectionEnum) extends Section {
  val nonEmpty: Boolean = rows.nonEmpty
}
