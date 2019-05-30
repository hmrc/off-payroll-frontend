/*
 * Copyright 2019 HM Revenue & Customs
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

package connectors

import config.FrontendAppConfig
import connectors.httpParsers.PDFGeneratorHttpParser
import connectors.httpParsers.PDFGeneratorHttpParser.Response
import javax.inject.Inject
import models.PdfRequest
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.twirl.api.Html
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}


class PDFGeneratorConnector @Inject()(ws: WSClient,
                                      appConfig: FrontendAppConfig) {

  private[connectors] lazy val url = appConfig.pdfGeneratorService + "/pdf-generator-service/generate"

  def generatePdf(html: Html)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Response] = {
    Logger.debug(s"[PDFGeneratorConnector][generatePdf] PDF HTML:\n\n$html")
    ws.url(url).post(Json.toJson(PdfRequest(html))) map PDFGeneratorHttpParser.reads
  }
}