/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object WouldPaySubstituteMessages extends BaseMessages {

    object Worker {
      val error = "Select yes if you would have to pay your substitute"
      val heading = "Would you have to pay your substitute?"
      val title = heading
      val p1 = "This would include payments made by you or your business."
    }

    object Hirer {
      val error = "Select yes if the worker would have to pay their substitute"
      val heading = "Would the worker have to pay their substitute?"
      val title = heading
      val p1 = "This would include payments made by the worker or their business."
    }
}
