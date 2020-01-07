/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object RejectSubstituteMessages extends BaseMessages {

    object Worker {
      val error = "Select yes if your client has the right to reject a substitute"
      val heading = "Does your client have the right to reject a substitute?"
      val title = heading
      val p1 = "A substitute is someone you send in your place to do your role."
      val p2 = "This can include rejecting a substitute even if they are equally qualified, and meet your client’s interviewing, vetting and security clearance procedures."
    }

    object Hirer {
      val error = "Select yes if you have the right to reject a substitute"
      val heading = "Do you have the right to reject a substitute?"
      val title = heading
      val p1 = "A substitute is someone the worker sends in their place to do their role."
      val p2 = "This can include rejecting a substitute even if they are equally qualified, and meet your interviewing, vetting and security clearance procedures."
    }
  }
