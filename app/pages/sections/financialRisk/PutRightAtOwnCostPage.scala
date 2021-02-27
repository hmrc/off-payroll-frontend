/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.sections.financialRisk

import models.sections.financialRisk.PutRightAtOwnCost
import pages.QuestionPage

case object PutRightAtOwnCostPage extends QuestionPage[PutRightAtOwnCost] {
  override def toString: String = "putRightAtOwnCost"
}
