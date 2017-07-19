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

import javax.inject.Inject

import play.api.Logger
import play.api.Play._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import play.api.mvc.Action
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.connectors.PdfGeneratorConnector
import uk.gov.hmrc.offpayroll.models._
import uk.gov.hmrc.offpayroll.util.{CompressedInterview, OffPayrollSwitches, ResultPageHelper}
import uk.gov.hmrc.play.frontend.controller.FrontendController

import scala.concurrent.Future
import uk.gov.hmrc.offpayroll.util.HtmlHelper.removeScriptTags
import uk.gov.hmrc.offpayroll.util.InterviewSessionStack.asMap
import uk.gov.hmrc.play.http.BadRequestException



class PrintController @Inject() (pdfGeneratorConnector: PdfGeneratorConnector) extends OffpayrollController {

  val emptyForm = Form(single("" -> text))

  def format = Action.async { implicit request =>

    val formatPrint = Form(
      mapping(
        "promise" -> text.verifying(_.equalsIgnoreCase("on")),
        "esi" -> boolean,
        "decisionResult" -> nonEmptyText,
        "decisionVersion" -> nonEmptyText,
        "compressedInterview" -> nonEmptyText,
        "decisionCluster" -> nonEmptyText
      )(FormatPrint.apply)(FormatPrint.unapply)
    )

    formatPrint.bindFromRequest.fold (
      error => {

        val compressedInterviewRawString = error.data("compressedInterview")
        val compressedInterview = CompressedInterview(compressedInterviewRawString)
        val decisionType = getDecisionType(error.data("decisionResult"))
        val version = error.data("decisionVersion")
        val cluster = error.data("decisionCluster")
        val esi = error.data("esi").toBoolean
        val decision = Decision(compressedInterview.asMap,decisionType,version,cluster)
        val interviewAsRawList = compressedInterview.asRawList
        val fragments = fragmentService.getAllFragmentsForInterview(compressedInterview.asMap) ++ fragmentService.getFragmentsByFilenamePrefix("result")
        val resultPageHelper = ResultPageHelper(interviewAsRawList, decisionType, fragments, cluster, esi)

        Future.successful(BadRequest(uk.gov.hmrc.offpayroll.views.html.interview.display_decision(decision, interviewAsRawList, esi, compressedInterviewRawString, resultPageHelper, error)))
      },
      formSuccess => {
        Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.formatPrint(formSuccess)))
      }
    )
  }

  private def getDecisionType(decisionResult: String): DecisionType = decisionResult match {
    case "IN" => IN
    case "OUT" => OUT
    case _ => UNKNOWN
  }

  def printResult = Action.async { implicit request =>

    val printPrint = Form(
      mapping(
        "esi" -> boolean,
        "decisionResult" -> nonEmptyText,
        "decisionVersion" -> nonEmptyText,
        "compressedInterview" -> nonEmptyText,
        "decisionCluster" -> nonEmptyText,
        "completedBy" -> text,
        "client" -> text,
        "job" -> text,
        "reference" -> optional(text)
      )(PrintResult.apply)(PrintResult.unapply)
    )

    printPrint.bindFromRequest.fold (
      _ => {
        throw new IllegalStateException("Hidden fields missing from the form")
      },
      printResult => {
        val interview = CompressedInterview(printResult.compressedInterview).asRawList
        val session = CompressedInterview(printResult.compressedInterview).asMap
        val fragments = fragmentService.getAllFragmentsForInterview(session) ++ fragmentService.getFragmentsByFilenamePrefix("result")

        val decision = Decision(session,getDecisionType(printResult.decisionResult),printResult.decisionVersion,printResult.decisionCluster)
        val resultPageHelper = ResultPageHelper(interview, getDecisionType(printResult.decisionResult), fragments, printResult.decisionCluster, printResult.esi)

        val body: Html = uk.gov.hmrc.offpayroll.views.html.interview.printResult(printResult, decision, resultPageHelper, emptyForm)

        if (OffPayrollSwitches.offPayrollPdf.enabled) {
          pdfGeneratorConnector.generatePdf(removeScriptTags(body.toString)).map { response =>
            if (response.status != OK)
              throw new BadRequestException(response.body)
            else
              Ok(response.bodyAsBytes.toArray).as("application/pdf")
                .withHeaders("Content-Disposition" -> s"attachment; filename=${printResult.reference.getOrElse("result")}.pdf")
          }
        }
        else {
          Future.successful(Ok(body))
        }
      }
    )
  }

}

case class FormatPrint(promise: String, esi: Boolean, decisionResult: String, decisionVersion: String, compressedInterview: String, decisionCluster: String)

case class PrintResult(esi: Boolean, decisionResult: String, decisionVersion: String, compressedInterview: String, decisionCluster: String, completedBy: String, client: String, job: String, reference: Option[String] )
