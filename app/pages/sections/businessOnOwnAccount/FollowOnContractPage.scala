/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.sections.businessOnOwnAccount

import pages.QuestionPage

case object FollowOnContractPage extends QuestionPage[Boolean] {

  override def toString: String = "followOnContract"
}
