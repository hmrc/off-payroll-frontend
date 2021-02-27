/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package connectors.utils

import play.api.libs.ws.ahc.AhcWSResponse
import play.api.libs.ws.ahc.cache.{CacheableHttpResponseBodyPart, CacheableHttpResponseStatus}
import play.shaded.ahc.io.netty.handler.codec.http.DefaultHttpHeaders
import play.shaded.ahc.org.asynchttpclient.Response
import play.shaded.ahc.org.asynchttpclient.uri.Uri

trait WsUtils {

  def wsResponse(status: Int, responseBody: String) = {

    val respBuilder = new Response.ResponseBuilder()
    respBuilder.accumulate(new CacheableHttpResponseBodyPart(responseBody.getBytes(), true))
    respBuilder.accumulate(new DefaultHttpHeaders().add("Content-Type", "application/json"))
    respBuilder.accumulate(new CacheableHttpResponseStatus(Uri.create("http://localhost:8080"), status, "", "json"))
    new AhcWSResponse(respBuilder.build())

  }
}
