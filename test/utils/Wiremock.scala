/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package utils

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

class Wiremock {

  val stubPort = 8080
  val stubHost = "localhost"

  val wireMockServer: WireMockServer = new WireMockServer(wireMockConfig().port(stubPort))

  def host(): String = s"http://$stubHost:$stubPort"

  def start(): Unit = {
    if (!wireMockServer.isRunning) {
      wireMockServer.start()
      WireMock.configureFor(stubHost, stubPort)
    }
  }

  def stop(): Unit = {
    wireMockServer.stop()
  }
}