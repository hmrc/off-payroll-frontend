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
import connectors.HttpParsers.PDFGeneratorHttpParser
import javax.inject.Inject
import play.api.libs.json.{Json, Writes}
import play.twirl.api.Html
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}


class PDFGeneratorConnector @Inject()(httpClient: HttpClient,
                                      appConfig: FrontendAppConfig) {

  private[connectors] val url = appConfig.pdfGeneratorService + "/pdf-generator-service/generate"

  private[connectors] def writes: Writes[Html] = Writes { html =>
    Json.obj("html" -> html.toString)
  }

  def generatePdf(html: Html)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[PDFGeneratorHttpParser.Response] = {
    httpClient.POST(url, html)(writes, PDFGeneratorHttpParser.reads, hc, ec)
  }
}