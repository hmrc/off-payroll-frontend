/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package utils

import java.io.ByteArrayInputStream

import org.scalamock.scalatest.MockFactory

import scala.io.{BufferedSource, Source}

object MockSourceUtil extends SourceUtil with MockFactory {
  override def fromURL(url: String): BufferedSource = Source.createBufferedSource(new ByteArrayInputStream("abc".getBytes))
}
