/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages

object LineManagerDutiesMessages extends BaseMessages {

    object Worker {
      val error = "Select yes if you will have any management responsibilities for your client"
      val heading = "Will you have any management responsibilities for your client?"
      val title = heading
      val p1 = "This can include deciding how much to pay someone, hiring or dismissing workers, and delivering appraisals."
    }

    object Hirer {
      val error = "Select yes if the worker will have any management responsibilities for your organisation"
      val heading = "Will the worker have any management responsibilities for your organisation?"
      val title = heading
      val p1 = "This can include deciding how much to pay someone, hiring or dismissing workers, and delivering appraisals."
    }
  }
