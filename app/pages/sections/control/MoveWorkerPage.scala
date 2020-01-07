/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.sections.control

import models.sections.control.MoveWorker
import pages.QuestionPage

case object MoveWorkerPage extends QuestionPage[MoveWorker] {
  override def toString: String = "moveWorker"
}
