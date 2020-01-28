/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.sections.setup

import models.sections.setup.WhatDoYouWantToFindOut
import pages.QuestionPage

case object WhatDoYouWantToFindOutPage extends QuestionPage[WhatDoYouWantToFindOut] {

  override def toString: String = "whatDoYouWantToFindOut"
}
