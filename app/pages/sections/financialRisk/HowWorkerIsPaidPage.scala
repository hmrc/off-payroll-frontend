/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.sections.financialRisk

import models.sections.financialRisk.HowWorkerIsPaid
import pages.QuestionPage

case object HowWorkerIsPaidPage extends QuestionPage[HowWorkerIsPaid] {
  override def toString: String = "howWorkerIsPaid"
}
