/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.sections.control

import models.sections.control.ChooseWhereWork
import pages.QuestionPage

case object ChooseWhereWorkPage extends QuestionPage[ChooseWhereWork] {
  override def toString: String = "chooseWhereWork"
}
