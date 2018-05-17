/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.offpayroll.connectors


import com.google.inject.ImplementedBy
import play.api.libs.ws.{WSClient, WSResponse}
import uk.gov.hmrc.offpayroll.FrontendPdfGeneratorConnector

import scala.concurrent.Future

/**
  * Created by peter on 12/12/2016.
  */
@ImplementedBy(classOf[FrontendPdfGeneratorConnector])
trait PdfGeneratorConnector {

  val serviceURL: String

  def getWsClient: WSClient

  def generatePdf(html: String): Future[WSResponse] = {
    getWsClient.url(serviceURL).post(Map("html" -> Seq(html)))
  }

}
