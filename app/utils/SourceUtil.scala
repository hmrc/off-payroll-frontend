/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package utils

import javax.inject.Inject

import scala.io.{BufferedSource, Source}

class SourceUtil @Inject()() {

  def fromURL(url: String): BufferedSource = Source.fromURL(url)

}
