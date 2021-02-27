/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.sections.setup

import models.sections.setup.WorkerType
import pages.QuestionPage

case object WorkerTypePage extends QuestionPage[WorkerType] {

  override def toString: String = "workerType"
}
