/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.sections.financialRisk

import pages.QuestionPage

case object OtherExpensesPage extends QuestionPage[Boolean] {

  override def toString: String = "otherExpenses"
}
