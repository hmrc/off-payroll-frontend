/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages.results

object DecisionVersionMessages extends BaseResultMessages {

  val p1 = (ver: String) => s"Decision service version: $ver"
  val p2 = "HMRC will stand by this result as long as it reflects the actual or expected working practices. If these working practices change, you should use this tool again."

}
