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

import models.AboutYouAnswer.Worker
import models.ArrangedSubstitue.YesClientAgreed
import models.ChooseWhereWork.Workerchooses
import models.HowWorkIsDone.WorkerAgreeWithOthers
import models.HowWorkerIsPaid.HourlyDailyOrWeekly
import models.IdentifyToStakeholders.WorkAsIndependent
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.AsPartOfUsualRateInWorkingHours
import models.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.WorkerType.SoleTrader
import models.{DecisionResponse, ErrorResponse, ExitEnum, Interview, Score, SetupEnum}
import uk.gov.hmrc.http.HeaderCarrier


class DecisionConnectorSpec extends ConnectorTests {

  implicit val headerCarrier = new HeaderCarrier()
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  val connector: DecisionConnector = new FrontendDecisionConnector(client, servicesConfig, configuration)

  val decisionConnectorWiremock = new DecisionConnectorWiremock
  val emptyInterview: Interview = Interview(
    "12345"
  )
  val interview: Interview = Interview(
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
    """
      |{
      |  "version": "1.0.0-beta",
      |  "correlationID": "12345",
      |  "score": {
      |    "personalService": "HIGH",
      |    "control": "LOW",
      |    "financialRisk": "LOW",
      |    "partAndParcel": "LOW"
      |  },
      |  "result": "Unknown"
      |}
    """.stripMargin

  "Calling the decide connector" should {
    "return a decision based on the empty interview" in {
      decisionConnectorWiremock.mockForSuccessResponse()

      import models.ResultEnum._
      import models.WeightedAnswerEnum._

      val clientResponse = await(connector.decide(emptyInterview))
      clientResponse shouldBe Right(DecisionResponse("1.0.0-beta", "12345",
        Score(None, None, Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
        UNKNOWN
      ))
    }
    "return a decision based on the populated interview" in {
      decisionConnectorWiremock.mockForSuccessFullInterviewResponse()

      import models.ResultEnum._
      import models.WeightedAnswerEnum._

      val clientResponse = await(connector.decide(interview))
      clientResponse shouldBe Right(DecisionResponse("1.5.0-final", "12345",
        Score(Some(SetupEnum.CONTINUE), Some(ExitEnum.CONTINUE), Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
        SELF_EMPLOYED
      ))
    }

    "return an error if a bad request is returned" in {
      decisionConnectorWiremock.mockFor400FailureResponse()

      val clientResponse = await(connector.decide(emptyInterview))
      clientResponse shouldBe Left(ErrorResponse(400, "Unexpected Response. Response: "))
    }
    "return an error if a 499 is returned" in {
      decisionConnectorWiremock.mockFor499FailureResponse()

      val clientResponse = await(connector.decide(emptyInterview))
      clientResponse shouldBe Left(ErrorResponse(499, "Unexpected Response. Response: "))

    }
    "return an error a 500 is returned" in {
      decisionConnectorWiremock.mockFor500FailureResponse()

      val clientResponse = await(connector.decide(emptyInterview))
      clientResponse shouldBe Left(ErrorResponse(500, "Unexpected Response. Response: "))

    }
  }
}
