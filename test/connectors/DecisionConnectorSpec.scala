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

import _root_.utils.MockDateTimeUtil
import base.{GuiceAppSpecBase, MockServicesConfig}
import connectors.mocks.MockHttp
import models.ArrangedSubstitute.YesClientAgreed
import models.ChooseWhereWork.WorkerChooses
import models.HowWorkIsDone.WorkerAgreeWithOthers
import models.HowWorkerIsPaid.HourlyDailyOrWeekly
import models.IdentifyToStakeholders.WorkAsIndependent
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.AsPartOfUsualRateInWorkingHours
import models.ResultEnum.{SELF_EMPLOYED, UNKNOWN}
import models.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.UserType.Worker
import models.WeightedAnswerEnum.{HIGH, LOW}
import models.WorkerType.SoleTrader
import models._
import models.logging.LogInterview
import play.api.libs.json.Json
import play.mvc.Http.Status
import repositories.FakeParallelRunningRepository

import scala.concurrent.Future


class DecisionConnectorSpec extends GuiceAppSpecBase with MockHttp with MockServicesConfig {

  object FakeTimestamp extends Timestamp {

    override def timestamp(time: Option[String]): String = s"01 January 2019, 00:00:00"

  }

  object TestDecisionConnector extends DecisionConnector(mockHttp, servicesConfig, frontendAppConfig, MockDateTimeUtil, new FakeParallelRunningRepository, FakeTimestamp)

  val emptyInterviewModel: Interview = Interview(
    "12345"
  )
  val interviewModel: Interview = Interview(
    "12345",
    Some(Worker),
    Some(false),
    Some(SoleTrader),
    None,
    Some(false),
    Some(YesClientAgreed),
    Some(false),
    Some(true),
    Some(true),
    Some(true),
    Some(CanMoveWorkerWithPermission),
    Some(WorkerAgreeWithOthers),
    Some(WorkerAgreeSchedule),
    Some(WorkerChooses),
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
      val response = Right(DecisionResponse("1.0.0-beta", "12345",
        Score(None, None, Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
        UNKNOWN
      ))

      setupMockHttpPost(TestDecisionConnector.decideUrl, emptyInterviewModel)(Future.successful(response))

      val clientResponse = await(TestDecisionConnector.decide(emptyInterviewModel))
      clientResponse mustBe response
    }

    "return a decision based on the populated interview" in {
      val response = Right(DecisionResponse("1.5.0-final", "12345",
        Score(Some(SetupEnum.CONTINUE), Some(ExitEnum.CONTINUE), Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
        SELF_EMPLOYED
      ))

      setupMockHttpPost(TestDecisionConnector.decideUrl, interviewModel)(Future.successful(response))

      val clientResponse = await(TestDecisionConnector.decide(interviewModel))
      clientResponse mustBe response
    }

    "return a decision based on the populated interview and the new decision service" in {
      val response = Right(DecisionResponse("1.5.0-final", "12345",
        Score(Some(SetupEnum.CONTINUE), Some(ExitEnum.CONTINUE), Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
        SELF_EMPLOYED
      ))

      setupMockHttpPost(TestDecisionConnector.decideUrl + "/new", interviewModel)(Future.successful(response))

      val clientResponse = await(TestDecisionConnector.decideNew(interviewModel))
      clientResponse mustBe response
    }

    "return a decision based on the personal service section (writesPersonalService)" in {
      val response = Right(DecisionResponse("1.0.0-beta", "12345",
        Score(None, None, Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
        UNKNOWN
      ))

      setupMockHttpPost(TestDecisionConnector.decideUrl, interviewModel)(Future.successful(response))

      val clientResponse = await(TestDecisionConnector.decide(interviewModel,Interview.writesPersonalService))
      clientResponse mustBe response
    }

    "return a decision based on the control section (Interview.writesControl)"  in {
      val response = Right(DecisionResponse("1.0.0-beta", "12345",
        Score(None, None, Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
        UNKNOWN
      ))

      setupMockHttpPost(TestDecisionConnector.decideUrl, interviewModel)(Future.successful(response))

      val clientResponse = await(TestDecisionConnector.decide(interviewModel,Interview.writesControl))
      clientResponse mustBe response
    }

    "return a decision based on the financial risk section (Interview.writesFinancialRisk)"  in {
      val response = Right(DecisionResponse("1.0.0-beta", "12345",
        Score(None, None, Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
        UNKNOWN
      ))

      setupMockHttpPost(TestDecisionConnector.decideUrl, interviewModel)(Future.successful(response))

      val clientResponse = await(TestDecisionConnector.decide(interviewModel,Interview.writesFinancialRisk))
      clientResponse mustBe response
    }

    "return an error if a bad request is returned" in  {
      val fail = Left(ErrorResponse(400, "Unexpected Response returned from decision API"))
      setupMockHttpPost(TestDecisionConnector.decideUrl, emptyInterviewModel)(Future.successful(fail))

      val clientResponse = await(TestDecisionConnector.decide(emptyInterviewModel))
      clientResponse mustBe fail
    }
    "return an error if a 499 is returned" in  {
      val fail = Left(ErrorResponse(499, "Unexpected Response returned from decision API"))
      setupMockHttpPost(TestDecisionConnector.decideUrl, emptyInterviewModel)(Future.successful(fail))

      val clientResponse = await(TestDecisionConnector.decide(emptyInterviewModel))
      clientResponse mustBe fail

    }
    "return an error a 500 is returned" in  {
      val fail = Left(ErrorResponse(500, "Unexpected Response returned from decision API"))
      setupMockHttpPost(TestDecisionConnector.decideUrl, emptyInterviewModel)(Future.successful(fail))

      val clientResponse = await(TestDecisionConnector.decide(emptyInterviewModel))
      clientResponse mustBe fail
    }

    "handle and return an exception" in {
      setupMockHttpPost(TestDecisionConnector.decideUrl, emptyInterviewModel)(Future.failed(new Exception("ohno")))

      val clientResponse = await(TestDecisionConnector.decide(emptyInterviewModel))
      clientResponse mustBe Left(ErrorResponse(Status.INTERNAL_SERVER_ERROR, s"HTTP exception returned from decision API: ohno"))
    }
  }

  "Calling the log API" should {
    "return a 204" in {
      val logResponse = Right(true)

      val decisionResponse = Json.parse(decisionResponseString).as[DecisionResponse]

      setupMockHttpPost(TestDecisionConnector.logUrl, LogInterview(interviewModel,decisionResponse,MockDateTimeUtil))(Future.successful(logResponse))

      val clientResponse = await(TestDecisionConnector.log(interviewModel,decisionResponse))
      clientResponse mustBe logResponse
    }

    "return an error a 500 is returned" in  {
      val decisionResponse = Json.parse(decisionResponseString).as[DecisionResponse]

      val fail = Left(ErrorResponse(500, "Unexpected Response returned from log API"))
      setupMockHttpPost(TestDecisionConnector.logUrl, LogInterview(interviewModel,decisionResponse, MockDateTimeUtil))(Future.successful(fail))

      val clientResponse = await(TestDecisionConnector.log(interviewModel,decisionResponse))
      clientResponse mustBe fail

    }

    "handle and return an exception" in {
      val decisionResponse = Json.parse(decisionResponseString).as[DecisionResponse]

      setupMockHttpPost(TestDecisionConnector.logUrl, LogInterview(interviewModel,decisionResponse, MockDateTimeUtil))(Future.failed(new Exception("ohno")))

      val clientResponse = await(TestDecisionConnector.log(interviewModel,decisionResponse))
      clientResponse mustBe Left(ErrorResponse(Status.INTERNAL_SERVER_ERROR, s"HTTP exception returned from decision API: ohno"))
    }
  }
}
