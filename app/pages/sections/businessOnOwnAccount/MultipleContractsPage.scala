/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.sections.businessOnOwnAccount

import pages.QuestionPage

case object MultipleContractsPage extends QuestionPage[Boolean] {

  override def toString: String = "multipleContracts"
}
