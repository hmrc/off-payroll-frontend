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

import base.SpecBase
import connectors.mocks.{MockHttp, MockWsClient}
import models.AboutYouAnswer.Worker
import models.ArrangedSubstitute.YesClientAgreed
import models.ChooseWhereWork.Workerchooses
import models.HowWorkIsDone.WorkerAgreeWithOthers
import models.HowWorkerIsPaid.HourlyDailyOrWeekly
import models.IdentifyToStakeholders.WorkAsIndependent
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.AsPartOfUsualRateInWorkingHours
import models.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.WorkerType.SoleTrader
import models.logging.LogInterview
import models._
import play.api.http.Status
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.Future


class DecisionConnectorSpec extends SpecBase with MockHttp {

  object TestDecisionConnector extends DecisionConnector(mockHttp, servicesConfig, frontendAppConfig)

  val emptyInterviewModel: Interview = Interview(
    "12345"
  )
  val interviewModel: Interview = Interview(
    "12345",
    Some(Worker),
    Some(false),
    Some(SoleTrader),
    Some(false),
    Some(YesClientAgreed),
    Some(false),
    Some(true),
    Some(true),
    Some(true),
    Some(CanMoveWorkerWithPermission),
    Some(WorkerAgreeWithOthers),
    Some(WorkerAgreeSchedule),
    Some(Workerchooses),
    Some(false),
    Some(false),
    Some(false),
    Some(false),
    Some(false),
    Some(HourlyDailyOrWeekly),
    Some(AsPartOfUsualRateInWorkingHours),
    Some(true),
    Some(true),
    Some(false),
    Some(WorkAsIndependent)
  )

  private val decisionResponseString =
    s"""
       |{
       |  "version": "1.0.0-beta",
       |  "correlationID": "12345",
       |  "score": {
       |    "personalService": "${WeightedAnswerEnum.HIGH}",
       |    "control": "${WeightedAnswerEnum.LOW}",
       |    "financialRisk": "${WeightedAnswerEnum.LOW}",
       |    "partAndParcel": "${WeightedAnswerEnum.LOW}"
       |  },
       |  "result": "${ResultEnum.UNKNOWN}"
       |}
    """.stripMargin

  val fullDecisionResponseString: String =
    s"""
       |{
       |  "version": "1.5.0-final",
       |  "correlationID": "12345",
       |  "score": {
       |    "setup": "${SetupEnum.CONTINUE}",
       |    "exit": "${ExitEnum.CONTINUE}",
       |    "personalService": "${WeightedAnswerEnum.HIGH}",
       |    "control": "${WeightedAnswerEnum.LOW}",
       |    "financialRisk": "${WeightedAnswerEnum.LOW}",
       |    "partAndParcel": "${WeightedAnswerEnum.LOW}"
       |  },
       |  "result": "${ResultEnum.SELF_EMPLOYED}"
       |}
    """.stripMargin

  val emptyInterview: String =
    s"""
       |{
       |  "version" : "1.5.0-final",
       |  "correlationID" : "12345",
       |  "interview" : {
       |    "setup" : { },
       |    "exit" : { },
       |    "personalService" : { },
       |    "control" : { },
       |    "financialRisk" : { },
       |    "partAndParcel" : { }
       |  }
       |}
     """.stripMargin

  val fullInterview: String =
    s"""
       |{
       |  "version": "1.5.0-final",
       |  "correlationID": "12345",
       |  "interview": {
       |    "setup": {
       |      "endUserRole": "personDoingWork",
       |      "hasContractStarted": "No",
       |      "provideServices": "soleTrader"
       |    },
       |    "exit": {
       |      "officeHolder": "No"
       |    },
       |    "personalService": {
       |      "workerSentActualSubstitute": "yesClientAgreed",
       |      "workerPayActualSubstitute": "No",
       |      "possibleSubstituteRejection": "wouldReject",
       |      "possibleSubstituteWorkerPay": "Yes",
       |      "wouldWorkerPayHelper": "Yes"
       |    },
       |    "control": {
       |      "engagerMovingWorker": "canMoveWorkerWithPermission",
       |      "workerDecidingHowWorkIsDone": "workerAgreeWithOthers",
       |      "whenWorkHasToBeDone": "workerAgreeSchedule",
       |      "workerDecideWhere": "workerChooses"
       |    },
       |    "financialRisk": {
       |      "workerProvidedMaterials": "No",
       |      "workerProvidedEquipment": "No",
       |      "workerUsedVehicle": "No",
       |      "workerHadOtherExpenses": "No",
       |      "expensesAreNotRelevantForRole": "No",
       |      "workerMainIncome": "incomeCalendarPeriods",
       |      "paidForSubstandardWork": "asPartOfUsualRateInWorkingHours"
       |    },
       |    "partAndParcel": {
       |      "workerReceivesBenefits": "Yes",
       |      "workerAsLineManager": "Yes",
       |      "contactWithEngagerCustomer": "No",
       |      "workerRepresentsEngagerBusiness": "workAsIndependent"
       |    }
       |  }
       |}
     """.stripMargin

  "Calling the decide connector" should {
    "return a decision based on the empty interview" in {
      val response = HttpResponse(Status.OK, Some(Json.parse(decisionResponseString)))

      setupMockHttpPost(TestDecisionConnector.decideUrl, Json.toJson(emptyInterviewModel))(Future.successful(response))

      import models.ResultEnum._
      import models.WeightedAnswerEnum._

      val clientResponse: Either[ErrorResponse, DecisionResponse] = await(TestDecisionConnector.decide(emptyInterviewModel))
      clientResponse mustBe Right(DecisionResponse("1.0.0-beta", "12345",
        Score(None, None, Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
        UNKNOWN
      ))
    }

    "return a decision based on the populated interview" in {
      val response = HttpResponse(Status.OK, Some(Json.parse(fullDecisionResponseString)))

      setupMockHttpPost(TestDecisionConnector.decideUrl, Json.toJson(interviewModel))(Future.successful(response))

      import models.ResultEnum._
      import models.WeightedAnswerEnum._

      val clientResponse: Either[ErrorResponse, DecisionResponse] = await(TestDecisionConnector.decide(interviewModel))
      clientResponse mustBe Right(DecisionResponse("1.5.0-final", "12345",
        Score(Some(SetupEnum.CONTINUE), Some(ExitEnum.CONTINUE), Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
        SELF_EMPLOYED
      ))
    }

    "return an error if a bad request is returned" in  {
      val fail = HttpResponse(Status.BAD_REQUEST, Some(Json.parse(fullDecisionResponseString)))
      setupMockHttpPost(TestDecisionConnector.decideUrl, Json.toJson(emptyInterviewModel))(Future.successful(fail))

      val clientResponse: Either[ErrorResponse, DecisionResponse] = await(TestDecisionConnector.decide(emptyInterviewModel))
      clientResponse mustBe Left(ErrorResponse(400, "Unexpected Response returned from decision API"))
    }
    "return an error if a 499 is returned" in  {
      val fail = HttpResponse(499, Some(Json.parse(fullDecisionResponseString)))
      setupMockHttpPost(TestDecisionConnector.decideUrl, Json.toJson(emptyInterviewModel))(Future.successful(fail))

      val clientResponse: Either[ErrorResponse, DecisionResponse] = await(TestDecisionConnector.decide(emptyInterviewModel))
      clientResponse mustBe Left(ErrorResponse(499, "Unexpected Response returned from decision API"))

    }
    "return an error a 500 is returned" in  {
      val fail = HttpResponse(Status.INTERNAL_SERVER_ERROR, Some(Json.parse(fullDecisionResponseString)))
      setupMockHttpPost(TestDecisionConnector.decideUrl, Json.toJson(emptyInterviewModel))(Future.successful(fail))

      val clientResponse: Either[ErrorResponse, DecisionResponse] = await(TestDecisionConnector.decide(emptyInterviewModel))
      clientResponse mustBe Left(ErrorResponse(500, "Unexpected Response returned from decision API"))
    }
  }

  "Calling the log API" should {
    "return a 204" in {
      val logResponse = HttpResponse(Status.NO_CONTENT, None)

      val decisionResponse = Json.parse(decisionResponseString).as[DecisionResponse]

      setupMockHttpPost(TestDecisionConnector.logUrl,  Json.toJson(LogInterview.createFromInterview(interviewModel,decisionResponse)))(Future.successful(logResponse))

      val clientResponse = await(TestDecisionConnector.log(interviewModel,decisionResponse))
      clientResponse mustBe Right(true)
    }

    "return an error a 500 is returned" in  {
      val decisionResponse = Json.parse(decisionResponseString).as[DecisionResponse]

      val fail = HttpResponse(Status.INTERNAL_SERVER_ERROR, Some(Json.parse(fullDecisionResponseString)))
      setupMockHttpPost(TestDecisionConnector.logUrl,  Json.toJson(LogInterview.createFromInterview(interviewModel,decisionResponse)))(Future.successful(fail))

      val clientResponse = await(TestDecisionConnector.log(interviewModel,decisionResponse))
      clientResponse mustBe Left(ErrorResponse(500, "Unexpected Response returned from log API"))

    }
  }
}
