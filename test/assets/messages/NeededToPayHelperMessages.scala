/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object NeededToPayHelperMessages extends BaseMessages {

    object Worker {
      val error = "Select yes if you paid another person to do a significant amount of this work"
      val heading = "Have you paid another person to do a significant amount of this work?"
      val title = heading
    }

    object Hirer {
      val error = "Select yes if the worker paid another person to do a significant amount of this work"
      val heading = "Has the worker paid another person to do a significant amount of this work?"
      val title = heading
    }
}
