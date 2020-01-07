/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object FollowOnContractMessages extends BaseMessages {

  val error = "Select yes if this contract will start immediately after the previous one ended"

  object Worker {
    val title = "Will this contract start immediately after the previous one ended?"
    val heading = "Will this contract start immediately after the previous one ended?"
    val subheading = "Worker’s contracts"
    val p1 = "This does not include any holiday period between the two contracts."
  }

  object Hirer {
    val title = "Will this contract start immediately after the previous one ended?"
    val heading = "Will this contract start immediately after the previous one ended?"
    val subheading = "Worker’s contracts"
    val p1 = "This does not include any holiday period between the two contracts."
  }
}
