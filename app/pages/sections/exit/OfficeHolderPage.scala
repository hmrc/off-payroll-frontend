/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.sections.exit

import pages.QuestionPage

case object OfficeHolderPage extends QuestionPage[Boolean] {

  override def toString: String = "officeHolder"
}
