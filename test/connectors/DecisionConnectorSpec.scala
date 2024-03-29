/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package connectors

import _root_.utils.MockDateTimeUtil
import base.{GuiceAppSpecBase, MockServicesConfig}
import connectors.mocks.MockHttp
import models.ResultEnum.{SELF_EMPLOYED, UNKNOWN}
import models.WeightedAnswerEnum.{HIGH, LOW}
import models._
import models.sections.control.ChooseWhereWork.WorkerChooses
import models.sections.control.HowWorkIsDone.WorkerAgreeWithOthers
import models.sections.control.MoveWorker.CanMoveWorkerWithPermission
import models.sections.control.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.sections.financialRisk.HowWorkerIsPaid.HourlyDailyOrWeekly
import models.sections.financialRisk.PutRightAtOwnCost.AsPartOfUsualRateInWorkingHours
import models.sections.partAndParcel.IdentifyToStakeholders.WorkAsIndependent
import models.sections.personalService.ArrangedSubstitute.YesClientAgreed
import models.sections.setup.WhoAreYou.Worker
import play.mvc.Http.Status

import scala.concurrent.Future


class DecisionConnectorSpec extends GuiceAppSpecBase with MockHttp with MockServicesConfig {

  override def beforeEach(): Unit = {
    super.beforeEach()

  }

  object FakeTimestamp extends Timestamp {
    override def timestamp(time: Option[String]): String = s"01 January 2019, 00:00:00"
  }

  object TestDecisionConnector extends DecisionConnector(mockHttp, servicesConfig, frontendAppConfig, MockDateTimeUtil, FakeTimestamp)

  val emptyInterviewModel: Interview = Interview(
    "12345"
  )
  val interviewModel: Interview = Interview(
    "12345",
    Some(Worker),
    Some(false),
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
}
