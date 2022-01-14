/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages.results

object AdditionalPDFMessages extends BaseResultMessages {

  val timestamp = (time: String) => s"$time (UTC)"
  val completedBy = (name: String) => s"Your name $name"
  val client = (name: String) => s"Your organisation’s name $name"
  val worker = (name: String) => s"Client’s name $name"
  val jobTitle = (title: String) => s"Contract or role title $title"
  val reference = (ref: String) => s"Other information $ref"
}
