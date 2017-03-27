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
import play.api.Play._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import play.api.libs.iteratee.Enumerator
import play.api.libs.ws.WSClient
import play.api.mvc.Action
import play.api.mvc.Results.Ok
import uk.gov.hmrc.offpayroll.connectors.PdfGeneratorConnector
import uk.gov.hmrc.offpayroll.util.{CompressedInterview, OffPayrollSwitches}
import uk.gov.hmrc.play.frontend.controller.FrontendController

import scala.concurrent.Future
import uk.gov.hmrc.offpayroll.FrontendPdfGeneratorConnector



class PrintController @Inject() (pdfGeneratorConnector: PdfGeneratorConnector) extends FrontendController {

  def format = Action.async { implicit request =>

    val formatPrint = Form(
      mapping(
        "esi" -> boolean,
        "decisionResult" -> nonEmptyText,
        "decisionVersion" -> nonEmptyText,
        "compressedInterview" -> nonEmptyText
      )(FormatPrint.apply)(FormatPrint.unapply)
    )

    formatPrint.bindFromRequest.fold (
      formWithErrors => {
        throw new IllegalStateException("Hidden fields missing from the form")
      },
      formSuccess => {
        Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.formatPrint(formSuccess.esi, formSuccess.decisionResult, formSuccess.decisionVersion, formSuccess.compressedInterview)))
      }
      )
  }

  def printResult = Action.async { implicit request =>

    val printPrint = Form(
      mapping(
        "esi" -> boolean,
        "decisionResult" -> nonEmptyText,
        "decisionVersion" -> nonEmptyText,
        "compressedInterview" -> nonEmptyText,
        "completedBy" -> text,
        "client" -> text,
        "job" -> text,
        "reference" -> text
      )(PrintResult.apply)(PrintResult.unapply)
    )

    printPrint.bindFromRequest.fold (
      formWithErrors => {
        throw new IllegalStateException("Hidden fields missing from the form")
      },
      printResult => {
        val interview = CompressedInterview(printResult.compressedInterview).asRawList
        val printData = Ok(uk.gov.hmrc.offpayroll.views.html.interview.printResult(interview, printResult))
        if (OffPayrollSwitches.offPayrollPdf.enabled) {
//          val s = "<h1>HELLO <i>WORLD ...</i> !!!</h1>"
          val wsResponse = pdfGeneratorConnector.generatePdf(printResult.toString)
          wsResponse.map{
            response =>
              val bytes = response.bodyAsBytes.toArray
              Logger.debug(s"********** got ${bytes.length} bytes ***********")
              Logger.debug(s"********** got ${response.status} ***********")
              Logger.debug(s"********** got ${response.statusText} ***********")
              if (response.status == 400){
                Ok(response.body)
              }
              else {
                val inputStream = new ByteArrayInputStream(bytes)
                val dataContent: Enumerator[Array[Byte]] = Enumerator.fromStream(inputStream)
                Ok.chunked(dataContent).as("application/pdf")
              }
          }
        }
        else {
          Future.successful(printData)
        }
      }
    )
  }

}

case class FormatPrint(esi: Boolean, decisionResult: String, decisionVersion: String, compressedInterview: String)

case class PrintResult(esi: Boolean, decisionResult: String, decisionVersion: String, compressedInterview: String, completedBy: String, client: String, job: String, reference: String )
