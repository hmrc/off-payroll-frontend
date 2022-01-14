/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package base

import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

trait MockServicesConfig extends MockFactory {

  val servicesConfig = mock[ServicesConfig]

  val stubPort = 8080

  (servicesConfig.baseUrl(_: String)).expects(*).returns(s"http://localhost:$stubPort")
}
