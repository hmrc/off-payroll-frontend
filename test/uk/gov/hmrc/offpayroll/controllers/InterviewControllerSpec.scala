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

package uk.gov.hmrc.offpayroll.controllers

import com.kenshoo.play.metrics.PlayModule
import org.scalatest.concurrent.ScalaFutures
import play.api.data.Form
import play.api.http.Status
import play.api.mvc.{Cookie, Request, Session}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, _}
import uk.gov.hmrc.http.{HeaderCarrier, SessionKeys}
import uk.gov.hmrc.http.logging.SessionId
import uk.gov.hmrc.offpayroll.WSHttp
import uk.gov.hmrc.offpayroll.connectors.DecisionConnector
import uk.gov.hmrc.offpayroll.models._
import uk.gov.hmrc.offpayroll.resources._
import uk.gov.hmrc.offpayroll.services.{FlowService, IR35FlowService, InterviewEvaluation}
import uk.gov.hmrc.offpayroll.util.{InterviewSessionStack, InterviewStack}
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

import scala.concurrent.Future


class InterviewControllerSpec extends UnitSpec with WithFakeApplication with ScalaFutures {


  val TEST_SESSION_ID = "41c1fc6444bb7e"
  private val indexValue = InterviewStack.elementIndex(SetupCluster.clusterElements(0)).getOrElse(0).toString
  private val mockSessionAsPair = (InterviewSessionStack.INTERVIEW_CURRENT_INDEX, indexValue)
  private val setupTag = "setup.tag"
  private val TEST_COMPRESSED_INTERVIEW = "7yYJCkUbY"
  override def bindModules = Seq(new PlayModule)

  val setupHasContractStarted = Element("hasContractStarted", RADIO, 1, SetupCluster)
  val setupEndUserRole =  Element("endUserRole", MULTI, 0, SetupCluster, List(
    Element("endUserRole.personDoingWork", RADIO, 0, SetupCluster),
    Element("endUserRole.endClient", RADIO, 1, SetupCluster),
    Element("endUserRole.placingAgency", RADIO, 2, SetupCluster),
    Element("endUserRole.none", RADIO, 3, SetupCluster)
  ))

  class InstrumentedIR35FlowService extends IR35FlowService(new DecisionConnector {
    override val decisionURL: String = ""
    override val serviceLogURL: String = ""
    override val serviceDecideURL = ""
    override val http = WSHttp
  }) {
    var passedCorrelationId = ""
    override def evaluateInterview(interview: Map[String, String], currentQnA: (String, String), correlationId: String, compressedInterview: String): Future[InterviewEvaluation] = {
      val futureInterviewEvaluation = super.evaluateInterview(interview, currentQnA, correlationId, TEST_COMPRESSED_INTERVIEW)
      passedCorrelationId = correlationId
      futureInterviewEvaluation
    }
  }

  class TestFlowService extends FlowService {
    override val flow = new TestFlow

    override def evaluateInterview(interview: Map[String, String], currentQnA: (String, String), correlationId: String, compressedInterview: String):
    Future[InterviewEvaluation] = {
      InterviewEvaluation(Option(setupHasContractStarted), Option(Decision(Map(), UNKNOWN, "v1", "setup")), true, TEST_SESSION_ID)
    }

    override def getStart(interview: Map[String, String]): Option[Element] = flow.getStart(interview)

    override def getAbsoluteElement(clusterId: Int, elementId: Int): Element = setupEndUserRole

    class TestFlow extends OffPayrollWebflow {

      override def getStart(interview: Map[String, String]): Option[Element] =
        Some(Element("tag", RADIO, 0, SetupCluster))

      override def getElementById(clusterId: Int, elementId: Int): Option[Element] = Option(setupEndUserRole)

      override def getElementByTag(tag: String): Option[Element] =  Option(setupEndUserRole)

      override def clusters: List[Cluster] = List(SetupCluster)

      override def getClusterByName(name: String): Cluster = SetupCluster
    }
  }

  class Setup(flowService: FlowService = new TestFlowService) {

    val interviewController = new InterviewController(flowService)

  }

  val testElement = Element("endUserRole.personDoingWork", RADIO, 0, SetupCluster)

  "verifyElement" should {
    "return a required constraint" in new Setup {
      interviewController.verifyElement(testElement).name shouldBe Some("constraint.required")
    }
  }

  "createListForm" should {
    "build an empty form of type list" in new Setup {
      val form: Form[List[String]] = interviewController.createListForm(testElement)
      form.data shouldBe Map()
      form.value shouldBe None
    }
  }

  "yesNo" should {
    "convert a boolean into a string" in new Setup {
      interviewController.yesNo(true) shouldBe "Yes"
      interviewController.yesNo(false) shouldBe "No"
    }
  }

  "GET /cluster/ with Session Interview" should {
    "return 200" in new Setup {
      val request = FakeRequest("GET", "/cluster/")
      val result = await(interviewController.begin.apply(request))
      status(result) shouldBe Status.OK
    }
  }

  "POST /cluster/0/element/0" should {
    "return 200" in new Setup {
      interviewController.begin.apply(FakeRequest("GET", "/setup"))
      val request = FakeRequest().withSession(SessionKeys.sessionId -> TEST_SESSION_ID)
        .withFormUrlEncodedBody(
          setup_endUserRolePersonDoingWork
        )
      val result = interviewController.processElement(0, 0)(request).futureValue
      status(result) shouldBe Status.OK
      contentAsString(result) should include(setup_hasContractStarted)
    }
  }

  "POST /cluster/0/element/0 with a sole trader" should {
    "return a 400" in new Setup {
      val request = FakeRequest().withSession(SessionKeys.sessionId -> TEST_SESSION_ID)
      .withFormUrlEncodedBody(
        setup_SoleTrader
      )
      val result = interviewController.processElement(0, 0)(request).futureValue
      status(result) shouldBe Status.BAD_REQUEST
    }
  }

  "POST /cluster/0/element/0 with test correlation id" should {
    "return 200" in {
      implicit val hc = HeaderCarrier(sessionId = Some(SessionId(TEST_SESSION_ID)))
      val flowService = new InstrumentedIR35FlowService
      val interviewController = new InterviewController(flowService)
      interviewController.begin.apply(FakeRequest("GET", "/setup"))
      val request = FakeRequest().withSession(SessionKeys.sessionId -> TEST_SESSION_ID)
      .withFormUrlEncodedBody(
        setup_endUserRolePersonDoingWork
      )
      val result = interviewController.processElement(0, 0)(request).futureValue
      status(result) shouldBe Status.OK
      contentAsString(result) should include(setup_hasContractStarted)
      flowService.passedCorrelationId shouldBe TEST_SESSION_ID
    }
  }

  val soleTrader = Map("a" -> "b", "c" -> "setup.provideServices.soleTrader", "d" -> "e")
  val nonSoleTrader = Map("a" -> "b", "setup.provideServices.soleTrader" -> "c", "d" -> "e")

  "esi" should {
    "check for the presence of the answer 'soleTrader'" in new Setup {
      interviewController.esi(soleTrader) shouldBe true
      interviewController.esi(nonSoleTrader) shouldBe false
    }
  }

  "logResponse" should {
    "log errors when there is no interview field" in new Setup {
      interviewController.logResponse(None, Session(nonSoleTrader), "my-id") shouldBe ""
    }
    "log correctly when there is an interview field" in new Setup {
      interviewController.logResponse(None, Session(soleTrader.+("interview" -> "testval")), "my-id") shouldBe "testval"
      interviewController.logResponse(None, Session(nonSoleTrader.+("interview" -> "otherval")), "my-id") shouldBe "otherval"
    }
  }
}
