/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages

object DidPaySubstituteMessages extends BaseMessages {

    object Worker {
      val error = "Select yes if you paid your substitute"
      val heading = "Did you pay your substitute?"
      val title = heading
      val p1 = "This includes payments made by you or your business."
    }

    object Hirer {
      val error = "Select yes if the worker paid their substitute"
      val heading = "Did the worker pay their substitute?"
      val title = heading
      val p1 = "This includes payments made by the worker or their business."
    }
  }
