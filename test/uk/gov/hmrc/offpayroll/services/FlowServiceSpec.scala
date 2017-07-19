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

package uk.gov.hmrc.offpayroll.services

import com.kenshoo.play.metrics.PlayModule
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import play.api.http.Status.OK
import play.api.libs.json.Json
import uk.gov.hmrc.offpayroll.connectors.DecisionConnector
import uk.gov.hmrc.offpayroll.models.DecisionResponse
import uk.gov.hmrc.offpayroll.modelsFormat._
import uk.gov.hmrc.offpayroll.resources._
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.http.HttpResponse
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Created by peter on 09/12/2016.
  */
class FlowServiceSpec extends UnitSpec with MockitoSugar with ServicesConfig with WithFakeApplication {

  private val TEST_CORRELATION_ID = "00000001099"
  private val TEST_COMPRESSED_INTERVIEW = "7yYJCkUbY"

  override def bindModules = Seq(new PlayModule)

  private val decisionResponseString_inIr35 =
    """
      |{
      |  "version": "1.0.0-beta",
      |  "correlationID": "00000001099",
      |  "score": {
      |    "personalService": "HIGH",
      |    "control": "LOW",
      |    "financialRisk": "LOW",
      |    "partAndParcel": "LOW"
      |  },
      |  "result": "Inside IR35"
      |}
    """.stripMargin

  private val decisionResponseString_unknown =
    """
      |{
      |  "version": "1.0.0-beta",
      |  "correlationID": "00000001099",
      |  "score": {
      |    "personalService": "HIGH",
      |    "control": "LOW",
      |    "financialRisk": "",
      |    "partAndParcel": "MEDIUM"
      |  },
      |  "result": "Unknown"
      |}
    """.stripMargin


  private val jsonResponse_inIr35 = Json.fromJson[DecisionResponse](Json.parse(decisionResponseString_inIr35)).get
  private val jsonResponse_unknown = Json.fromJson[DecisionResponse](Json.parse(decisionResponseString_unknown)).get
  val mockDecisionConnector = mock[DecisionConnector]

  val testFlowService = new IR35FlowService(mockDecisionConnector)

  "A Flow Service should " should {
    " be able to get the start of an Interview" in {
      testFlowService.getStart(Map[String, String]()) should not be (null)
    }

    " move to the next cluster when cannotFixWorkerLocation is answered for control.workerDecideWhere" in {

      when(mockDecisionConnector.decide(any())(any())).thenReturn(Future(jsonResponse_unknown))

      val interview: Map[String, String] = Map(control_workerDecideWhere_cannotFixWorkerLocation)
      val currentElement: (String, String) = control_workerDecideWhere_cannotFixWorkerLocation

      val interviewEvalResult = await(testFlowService.evaluateInterview(interview, currentElement, TEST_CORRELATION_ID, TEST_COMPRESSED_INTERVIEW))

      interviewEvalResult.continueWithQuestions shouldBe true
      interviewEvalResult.element.head.questionTag shouldBe "financialRisk.haveToPayButCannotClaim"
      interviewEvalResult.correlationId shouldBe TEST_CORRELATION_ID
    }

    " Exit when Yes is answered for exit.officeHolder as it would be the final question" in {

      when(mockDecisionConnector.decide(any())(any())).thenReturn(Future(jsonResponse_inIr35))
      when(mockDecisionConnector.log(any())(any())).thenReturn(Future(HttpResponse.apply(OK)))

      val interview: Map[String, String] = fullInterview_ir35OfficeHolderYes
      val currentElement: (String, String) = "exit.officeHolder" -> "Yes"

      val interviewEvalResult = await(testFlowService.evaluateInterview(interview, currentElement, TEST_CORRELATION_ID, TEST_COMPRESSED_INTERVIEW))

      interviewEvalResult.correlationId shouldBe TEST_CORRELATION_ID
      interviewEvalResult.continueWithQuestions shouldBe false
    }

    " be able to process a partial personalService and expect it to return Continue" in {

      when(mockDecisionConnector.decide(any())(any())).thenReturn(Future(jsonResponse_unknown))

      val interview: Map[String, String] = Map(personalService_workerSentActualSubstituteYesClientAgreed)

      val currentElement: (String, String) = personalService_workerSentActualSubstituteYesClientAgreed

      val interviewEvalResult = await(testFlowService.evaluateInterview(interview, currentElement, TEST_CORRELATION_ID, TEST_COMPRESSED_INTERVIEW))

      assert(interviewEvalResult.continueWithQuestions === true, "Only a partial personalService so we need to continue")
      assert(interviewEvalResult.element.head.questionTag === personalService_workerPayActualSubstitute) //next tag
      interviewEvalResult.correlationId shouldBe TEST_CORRELATION_ID
    }

    " be able to get an element in personalService cluster" in {
      assert(testFlowService.getAbsoluteElement(2, 1).questionTag == personalService_workerPayActualSubstitute)
    }

    " be able to get an element in setup cluster" in {
      assert(testFlowService.getAbsoluteElement(0, 1).questionTag == setup_hasContractStarted)
    }
  }


}
