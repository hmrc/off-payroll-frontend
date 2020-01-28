/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object WorkerKnownMessages extends BaseMessages {

  object Hirer {
    val error = "Select yes if you know who will be doing this work"
    val title = "Does your organisation know who will be doing this work?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "If you do not know who the worker is, you will not need to answer any more questions. You will still get a determination that HMRC will stand by."
    val p2 = "You should use this tool again if the worker disagrees with the determination. You would then be asked additional questions about the worker."
  }
}
