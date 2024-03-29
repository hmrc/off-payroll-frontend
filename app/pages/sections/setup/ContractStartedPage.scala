/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.sections.setup

import pages.QuestionPage

case object ContractStartedPage extends QuestionPage[Boolean] {

  override def toString: String = "contractStarted"
}
