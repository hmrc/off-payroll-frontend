/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.sections.setup

import models.sections.setup.WhoAreYou
import pages.QuestionPage

case object WhoAreYouPage extends QuestionPage[WhoAreYou] {
  override def toString: String = "whoAreYou"
}
