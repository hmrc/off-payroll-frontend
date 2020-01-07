/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.sections.partParcel

import models.sections.partAndParcel.IdentifyToStakeholders
import pages.QuestionPage

case object IdentifyToStakeholdersPage extends QuestionPage[IdentifyToStakeholders] {
  override def toString: String = "identifyToStakeholders"
}
