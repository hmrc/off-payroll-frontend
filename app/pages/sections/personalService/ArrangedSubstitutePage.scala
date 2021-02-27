/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.sections.personalService

import models.sections.personalService.ArrangedSubstitute
import pages.QuestionPage

case object ArrangedSubstitutePage extends QuestionPage[ArrangedSubstitute] {
  override def toString: String = "arrangedSubstitute"
}
