/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package assets.messages

object BusinessSizeMessages extends BaseMessages {

  val option1 = "More than £10.2 million annual turnover."
  val option2 = "More than £5.1 million on their balance sheet."
  val option3 = "More than 50 employees."
  val option4 = "None of the above."

  object Worker {
    val heading = "How large is this organisation? Tick all that apply"
    val title = heading
  }

  object Hirer {
    val heading = "How large is your organisation? Tick all that apply"
    val title = heading
  }
}
