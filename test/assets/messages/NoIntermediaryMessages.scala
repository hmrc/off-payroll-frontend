/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages

object NoIntermediaryMessages extends BaseMessages {

  object Worker {

    val title = "You need to start again"
    val heading = "You need to start again"
    val p1 = "The off-payroll working rules (IR35) (opens in a new window) can only apply if you are providing your services through an intermediary."
    val p2 = "As you told us there is no intermediary involved, you need to find out if this work is classed as employment or self-employment for tax purposes."
    val startAgain = "Start again"
  }

  object Hirer {

    val title = "You need to start again"
    val heading = "You need to start again"
    val p1 = "The off-payroll working rules (IR35) (opens in a new window) can only apply if the worker provides their services through an intermediary."
    val p2 = "As you told us there is no intermediary involved, you need to find out if this work is classed as employment or self-employment for tax purposes."
    val startAgain = "Start again"
  }
}