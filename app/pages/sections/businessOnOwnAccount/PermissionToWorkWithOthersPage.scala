/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.sections.businessOnOwnAccount

import pages.QuestionPage

case object PermissionToWorkWithOthersPage extends QuestionPage[Boolean] {

  override def toString: String = "permissionToWorkWithOthers"
}
