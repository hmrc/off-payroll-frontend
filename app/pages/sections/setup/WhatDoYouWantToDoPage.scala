/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.sections.setup

import models.sections.setup.WhatDoYouWantToDo
import pages.QuestionPage

case object WhatDoYouWantToDoPage extends QuestionPage[WhatDoYouWantToDo] {

  override def toString: String = "whatDoYouWantToDo"
}
