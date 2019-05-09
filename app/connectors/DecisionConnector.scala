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

import java.time.{Instant, ZoneOffset}

import config.FrontendAppConfig
import connectors.DecisionHttpParser.DecisionReads
import javax.inject.{Inject, Singleton}

import models.WorkerType.SoleTrader
import models._
import org.joda.time.{DateTime, DateTimeZone}
import play.api.Logger
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import InterviewFormat._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import play.mvc.Http.Status.INTERNAL_SERVER_ERROR

trait DecisionConnector {

  val baseUrl: String
  val decideUrl: String
  val logUrl: String

  def decide(decisionRequest: Interview)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]]
  def log(decisionRequest: Interview,
          decisionResult: DecisionResponse)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]]
}

@Singleton
class DecisionConnectorImpl @Inject()(http: HttpClient,
                                      servicesConfig: ServicesConfig,
                                      conf: FrontendAppConfig) extends DecisionConnector {

  override val baseUrl: String = servicesConfig.baseUrl("cest-decision")
  override val decideUrl = s"$baseUrl/cest-decision/decide"
  override val logUrl = s"$baseUrl/cest-decision/log"

  def decide(decisionRequest: Interview)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {

    http.POST[Interview, Either[ErrorResponse, DecisionResponse]](decideUrl, decisionRequest).recover{
      case e: Exception =>
        Logger.error(s"Unexpected response from decide API - Response: ${e.getMessage}")
        Left(ErrorResponse(INTERNAL_SERVER_ERROR, s"Exception: ${e.getMessage}"))
    }
  }

  def log(decisionRequest: Interview,
          decisionResult: DecisionResponse)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {
    http.POST[LogInterview, Either[ErrorResponse, DecisionResponse]](logUrl,interviewToLogInterview(decisionRequest,decisionResult)).recover{
      case e: Exception =>
        Logger.error(s"Unexpected response from log API - Response: ${e.getMessage}")
        Left(ErrorResponse(INTERNAL_SERVER_ERROR, s"Exception: ${e.getMessage}"))
    }
  }

  private def interviewToLogInterview(decisionRequest: Interview, decisionResult: DecisionResponse): LogInterview = {
    LogInterview(
      conf.decisionVersion,
      "",
      decisionRequest.provideServices match {
        case Some(workerType) if workerType == SoleTrader => "ESI"
        case _ => "IR35"
      },
      decisionResult.result.toString,
      None,
      Setup(
        decisionRequest.endUserRole.get.toString,
        booleanToYesNo(decisionRequest.hasContractStarted.get),
        decisionRequest.provideServices.get.toString
      ),
      Exit(booleanToYesNo(decisionRequest.officeHolder.get)),
      Some(PersonalService(
        decisionRequest.workerSentActualSubstitute.fold(None: Option[String]){sub => Some(sub.toString)},
        decisionRequest.workerPayActualSubstitute.fold(None: Option[String]){pay => Some(pay.toString)},
        decisionRequest.possibleSubstituteRejection.fold(None: Option[String]){reject => Some(reject.toString)},
        decisionRequest.possibleSubstituteWorkerPay.fold(None: Option[String]){possible => Some(possible.toString)},
        decisionRequest.wouldWorkerPayHelper.fold(None: Option[String]){helper => Some(helper.toString)}
      )),
      Some(Control(decisionRequest.engagerMovingWorker.fold(None: Option[String]){move => Some(move.toString)},
        decisionRequest.workerDecidingHowWorkIsDone.fold(None: Option[String]){decide => Some(decide.toString)},
        decisionRequest.whenWorkHasToBeDone.fold(None: Option[String]){done => Some(done.toString)},
        decisionRequest.workerDecideWhere.fold(None: Option[String]){where => Some(where.toString)}
      )),
      Some(FinancialRisk(
        decisionRequest.workerProvidedMaterials.fold(None: Option[String]){material => Some(booleanToYesNo(material))},
        decisionRequest.workerProvidedEquipment.fold(None: Option[String]){equip => Some(booleanToYesNo(equip))},
        decisionRequest.workerUsedVehicle.fold(None: Option[String]){vehicle => Some(booleanToYesNo(vehicle))},
        decisionRequest.workerHadOtherExpenses.fold(None: Option[String]){expense => Some(booleanToYesNo(expense))},
        decisionRequest.expensesAreNotRelevantForRole.fold(None: Option[String]){relevant => Some(booleanToYesNo(relevant))},
        decisionRequest.workerMainIncome.fold(None: Option[String]){income => Some(income.toString)},
        decisionRequest.paidForSubstandardWork.fold(None: Option[String]){paid => Some(paid.toString)}
      )),
      Some(PartAndParcel(
        decisionRequest.workerReceivesBenefits.fold(None: Option[String]){benefit => Some(booleanToYesNo(benefit))},
        decisionRequest.workerAsLineManager.fold(None: Option[String]){manager => Some(booleanToYesNo(manager))},
        decisionRequest.contactWithEngagerCustomer.fold(None: Option[String]){customer => Some(booleanToYesNo(customer))},
        decisionRequest.workerRepresentsEngagerBusiness.fold(None: Option[String]){business => Some(business.toString)}
      )),
      new DateTime(DateTimeZone.UTC)
    )
  }

  private def booleanToYesNo(value: Boolean): String =
    if (value) "Yes" else "No"

}
