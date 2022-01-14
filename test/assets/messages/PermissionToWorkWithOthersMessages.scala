/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages

object PermissionToWorkWithOthersMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you are required to ask permission to work for other clients"
    val title = "Are you required to ask permission to work for other clients?"
    val heading = title
    val subheading = "Worker’s contracts"
  }

  object Hirer {
    val error = "Select yes if the worker is required to ask permission to work for other organisations"
    val title = "Is the worker required to ask permission to work for other organisations?"
    val heading = title
    val subheading = "Worker’s contracts"
  }
}
