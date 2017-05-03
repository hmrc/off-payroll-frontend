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

import com.kenshoo.play.metrics.PlayModule
import org.scalatest.concurrent.ScalaFutures
import play.api.http.Status
import play.api.mvc.{Request, Session}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, _}
import uk.gov.hmrc.offpayroll.models._
import uk.gov.hmrc.offpayroll.resources.{partialInterview_hasContractStarted_Yes, _}
import uk.gov.hmrc.offpayroll.services.{FlowService, IR35FlowService, InterviewEvaluation}
import uk.gov.hmrc.offpayroll.util.{ElementProvider, InterviewSessionStack, InterviewStack}
import uk.gov.hmrc.offpayroll.{FrontendDecisionConnector, WithTestFakeApplication}
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

import scala.concurrent.Future


class InterviewControllerSpec extends UnitSpec with WithFakeApplication with ScalaFutures {


  val TEST_SESSION_ID = "41c1fc6444bb7e"
  private val indexValue = InterviewStack.elementIndex(SetupCluster.clusterElements(0)).getOrElse(0).toString
  private val mockSessionAsPair = (InterviewSessionStack.INTERVIEW_CURRENT_INDEX, indexValue)
  override def bindModules = Seq(new PlayModule)

  class TestSessionHelper extends SessionHelper {
    override def createCorrelationId(request: Request[_]): String = TEST_SESSION_ID
  }

  class InstrumentedIR35FlowService extends IR35FlowService(new FrontendDecisionConnector) {
    var passedCorrelationId = ""
    override def evaluateInterview(interview: Map[String, String], currentQnA: (String, String), correlationId: String): Future[InterviewEvaluation] = {
      val futureInterviewEvaluation = super.evaluateInterview(interview, currentQnA, correlationId)
      passedCorrelationId = correlationId
      futureInterviewEvaluation
    }
  }

  class TestFlowService extends FlowService {
    override val flow: Webflow = new TestFlow

    override def evaluateInterview(interview: Map[String, String], currentQnA: (String, String), correlationId: String):
    Future[InterviewEvaluation] = ???

    override def getStart(interview: Map[String, String]): Option[Element] = flow.getStart(interview)

    override def getAbsoluteElement(clusterId: Int, elementId: Int): Element = ???

    class TestFlow extends Webflow {
      override def getNext(currentElement: Element): Option[Element] = ???

      override def getStart(interview: Map[String, String]): Option[Element] =
        Some(Element("tag", RADIO, 0, SetupCluster))

      override def getElementById(clusterId: Int, elementId: Int): Option[Element] = ???

      override def getElementByTag(tag: String): Option[Element] = ???

      override def clusters: List[Cluster] = ???

      override def getClusterByName(name: String): Cluster = ???
    }
  }

  "GET /cluster/ with Session Interview" should {
    "return 200" in {

      val interviewController = new InterviewController(new TestFlowService, new TestSessionHelper)

      val request = FakeRequest("GET", "/cluster/")
      val result = await(interviewController.begin.apply(request))
      status(result) shouldBe Status.OK
    }
  }

  "POST /cluster/0/element/0" should {
    "return 200" in {
      val request = FakeRequest().withSession(mockSessionAsPair)
      .withFormUrlEncodedBody(
        setup_endUserRolePersonDoingWork
      )
      val result = new InterviewController(IR35FlowService(), new TestSessionHelper()).processElement(0, 0)(request).futureValue
      status(result) shouldBe Status.OK
      contentAsString(result) should include(setup_hasContractStarted)
    }
  }

  "POST /cluster/0/element/0 without a cookie" should {
    "intercept an exception" in {
      val request = FakeRequest().withSession(mockSessionAsPair)
      .withFormUrlEncodedBody(
        setup_endUserRolePersonDoingWork
      )
      intercept[NoSuchElementException]{InterviewController().processElement(0, 0)(request).futureValue}
    }
  }

  "POST /cluster/0/element/0 with test correlation id" should {
    "return 200" in {
      val request = FakeRequest().withSession(mockSessionAsPair)
      .withFormUrlEncodedBody(
        setup_endUserRolePersonDoingWork
      )
      val flowService = new InstrumentedIR35FlowService
      val result = new InterviewController(flowService, new TestSessionHelper()).processElement(0, 0)(request).futureValue
      status(result) shouldBe Status.OK
      contentAsString(result) should include(setup_hasContractStarted)
      flowService.passedCorrelationId shouldBe TEST_SESSION_ID
    }
  }
}
