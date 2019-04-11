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
package services

import connectors.DecisionConnector
import models.AboutYouAnswer.Worker
import models.ArrangedSubstitue.YesClientAgreed
import models.ChooseWhereWork.Workerchooses
import models.HowWorkIsDone.WorkerAgreeWithOthers
import models.HowWorkerIsPaid.HourlyDailyOrWeekly
import models.IdentifyToStakeholders.WorkAsIndependent
import models.{DecisionResponse, ErrorResponse, Interview, Score}
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.AsPartOfUsualRateInWorkingHours
import models.ResultEnum.UNKNOWN
import models.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.WeightedAnswerEnum.{HIGH, LOW}
import models.WorkerType.SoleTrader
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import utils.ImplicitConfig
import org.mockito.Mockito.when

import scala.concurrent.Future

class DecisionServiceSpec extends UnitSpec with WithFakeApplication with ImplicitConfig {

  implicit val headerCarrier = new HeaderCarrier()
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  val connector = mock[DecisionConnector]

  val service: DecisionService = new DecisionServiceImpl(connector)

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

  val response = DecisionResponse("1.0.0-beta", "12345",
    Score(None, None, Some(HIGH),Some(LOW),Some(LOW),Some(LOW)),
    UNKNOWN
  )

  "Calling the decide service" should {
    "return a decision based on the interview" in {

      when(connector.decide(interview)).thenReturn(Future.successful(Right(response)))

      await(service.decide(interview)) shouldBe Right(response)

    }
    "handle errors" in {

      when(connector.decide(interview)).thenReturn(Future.successful(Left(ErrorResponse(400,"Bad"))))

      await(service.decide(interview)) shouldBe Left(ErrorResponse(400,"Bad"))

    }
  }

}
