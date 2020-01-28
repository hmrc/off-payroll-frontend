/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package viewmodels

sealed trait ResultMode

object Result extends ResultMode
object ResultPrintPreview extends ResultMode
object ResultPDF extends ResultMode
