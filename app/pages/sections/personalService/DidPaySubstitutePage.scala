/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.sections.personalService

import pages.QuestionPage

case object DidPaySubstitutePage extends QuestionPage[Boolean] {

  override def toString: String = "didPaySubstitute"
}
