/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package connectors.utils

import play.api.libs.ws.ahc.AhcWSResponse
import play.api.libs.ws.ahc.cache.{CacheableHttpResponseBodyPart, CacheableHttpResponseStatus}
import play.shaded.ahc.org.asynchttpclient.Response
import play.shaded.ahc.org.asynchttpclient.uri.Uri

trait WsUtils {

  def wsResponse(status: Int, responseBody: String) =
    new AhcWSResponse(new Response.ResponseBuilder()
      .accumulate(new CacheableHttpResponseStatus(Uri.create("http://localhost:8080"), status, "", ""))
      .accumulate(new CacheableHttpResponseBodyPart(responseBody.getBytes(), true))
      .build())

}
