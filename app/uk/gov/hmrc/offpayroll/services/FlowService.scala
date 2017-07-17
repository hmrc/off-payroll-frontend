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

import javax.inject.Inject

import com.google.inject.ImplementedBy
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import uk.gov.hmrc.offpayroll.FrontendDecisionConnector
import uk.gov.hmrc.offpayroll.connectors.DecisionConnector
import uk.gov.hmrc.offpayroll.models.{OffPayrollWebflow, UNKNOWN, _}
import uk.gov.hmrc.play.http.HeaderCarrier

import scala.concurrent.Future

/**
  * Created by peter on 09/12/2016.
  */
@ImplementedBy(classOf[IR35FlowService])
abstract class FlowService {

  val flow: OffPayrollWebflow
  /**
    *
    * @param interview
    * @return
    */
  def evaluateInterview(interview: Map[String, String], currentQnA: (String, String), correlationId:String, compressedInterview: String): Future[InterviewEvaluation]

  def getStart(interview: Map[String, String]): Option[Element]

  def getAbsoluteElement(clusterId: Int, elementId: Int): Element

}

object IR35FlowService {
  def apply() = new IR35FlowService(new FrontendDecisionConnector)
}


class IR35FlowService @Inject() (val decisionConnector: DecisionConnector) extends FlowService {

  private val STOP = false
  private val CONTINUE = true
  implicit val hc = HeaderCarrier()

  val flow = OffPayrollWebflow

  override def getStart(interview: Map[String, String]): Option[Element] = flow.getStart(interview)

  private def guardValidElement(currentTag: String): Element = {
    val tag = flow.getElementByTag(currentTag)
    if (tag.isEmpty) throw new IllegalAccessException("No Such Element: " + currentTag)
    else tag.get
  }

  private def getStatus(decision: DecisionResponse):DecisionType = decision.result match {
    case "Outside IR35" => OUT
    case "Inside IR35" => IN
    case "Unknown" => UNKNOWN
    case _ => UNKNOWN
  }

  override def evaluateInterview(interview: Map[String, String], currentQnA: (String, String), correlationId:String, compressedInterview: String): Future[InterviewEvaluation] = {

    val cleanInterview = interview.filter(qa => flow.clusters.exists(clsrt => qa._1.startsWith(clsrt.name)))
    val currentTag = currentQnA._1
    val currentElement: Element = guardValidElement(currentTag)
    val optionalNextElement = flow.shouldAskForDecision(interview, currentQnA)

    def logInterview(decisionRequest: DecisionRequest, decision: DecisionResponse) = {
      decisionConnector.log(LogInterviewBuilder.buildLogRequest(decisionRequest, compressedInterview, decision))
    }

    if (optionalNextElement.isEmpty) {

      val decisionRequest = DecisionBuilder.buildDecisionRequest(cleanInterview, correlationId)
      decisionConnector.decide(decisionRequest).map[InterviewEvaluation](
        decision => {
          Logger.debug("Decision received from Decision Service: " + decision)
            if (getStatus(decision) == UNKNOWN) {
              if (flow.getNext(interview, currentElement, true).isEmpty) {
                logInterview(decisionRequest, decision)
                InterviewEvaluation(Option.empty[Element], Option(Decision(cleanInterview, UNKNOWN, flow.version, currentElement.clusterParent.name)), STOP, decision.correlationID)
              }
              else
                InterviewEvaluation(flow.getNext(interview, currentElement, true), Option.empty[Decision], CONTINUE, decision.correlationID)
            } else {
              logInterview(decisionRequest, decision)
              InterviewEvaluation(Option.empty[Element], Option.apply(Decision(cleanInterview, getStatus(decision), flow.version, currentElement.clusterParent.name)), STOP, decision.correlationID)
            }
          }
          )
    } else {
      Future.successful(InterviewEvaluation(optionalNextElement, Option.empty[Decision], CONTINUE, correlationId))
    }
  }


  override def getAbsoluteElement(clusterId: Int, elementId: Int): Element = {
    val currentElement = flow.getElementById(clusterId, elementId)
    if (currentElement.nonEmpty) currentElement.head
    else throw new NoSuchElementException("No Element found matching: " + clusterId + "/" + elementId)
  }

}

case class InterviewEvaluation(element: Option[Element], decision: Option[Decision], continueWithQuestions: Boolean, correlationId:String)
