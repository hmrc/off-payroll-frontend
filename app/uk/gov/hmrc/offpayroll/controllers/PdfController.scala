/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.controllers

import java.io.ByteArrayInputStream
import javax.inject.Inject

import play.api.Logger
import play.api.libs.iteratee.Enumerator
import play.api.mvc.Action
import play.api.mvc.Results.Ok
import uk.gov.hmrc.offpayroll.connectors.PdfGeneratorConnector
import uk.gov.hmrc.offpayroll.services.InterviewEvaluation

import scala.concurrent.ExecutionContext.Implicits.global


class PdfController @Inject() (pdfGeneratorConnector: PdfGeneratorConnector){

//  def generatePdf(decision: InterviewEvaluation, interview: List[(String, List[String])], isEsi: Boolean, compressedInterview: String ) = Action.async { implicit request =>
  def generatePdf() = Action.async { implicit request =>

//    val result = uk.gov.hmrc.offpayroll.views.html.interview.display_decision(decision.decision.head, interview, isEsi, compressedInterview)

    val s = "<h1>HELLO <i>WORLD ...</i> !!!</h1>"

    Logger.debug("********** Calling PDF Service ***********")

    val wsResponse = pdfGeneratorConnector.generatePdf(s)

    wsResponse.map{
      response =>
        val bytes = response.bodyAsBytes.toArray
        val inputStream = new ByteArrayInputStream(bytes)

        val dataContent: Enumerator[Array[Byte]] = Enumerator.fromStream(inputStream)

        Ok.chunked(dataContent).as("application/pdf")
    }
  }

}
