/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.sections.control

import models.sections.control.HowWorkIsDone
import pages.QuestionPage

case object HowWorkIsDonePage extends QuestionPage[HowWorkIsDone] {
  override def toString: String = "howWorkIsDone"
}
