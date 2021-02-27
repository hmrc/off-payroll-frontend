/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package navigation

import models.{Mode, UserAnswers}
import pages.Page
import play.api.mvc.Call

abstract class Navigator {

  def nextPage(page: Page, mode: Mode): UserAnswers => Call

}
