/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.sections.setup

import pages.QuestionPage

case object WorkerUsingIntermediaryPage extends QuestionPage[Boolean] {

  override def toString: String = "workerUsingIntermediary"
}
