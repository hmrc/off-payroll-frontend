/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package utils

import java.net.URI

import scala.util.Try

object RefererUtil {

  def asRelativeUrl(url: String): Option[String] =

    for {
      uri      <- Try(new URI(url)).toOption
      path     <- Option(uri.getPath).filterNot(_.isEmpty)
      query    <- Option(uri.getQuery).map("?" + _).orElse(Some(""))
      fragment <- Option(uri.getRawFragment).map("#" + _).orElse(Some(""))
    } yield s"$path$query$fragment"

}
