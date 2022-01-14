/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages

case object ResultPage extends QuestionPage[Boolean] {
  override def toString: String = "result"
}

case object Timestamp extends QuestionPage[String] {
  override def toString: String = "timestamp"
}
