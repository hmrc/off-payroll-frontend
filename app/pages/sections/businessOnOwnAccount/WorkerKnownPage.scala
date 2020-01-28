/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.sections.businessOnOwnAccount

import pages.QuestionPage

case object WorkerKnownPage extends QuestionPage[Boolean] {

  override def toString: String = "workerKnown"
}
