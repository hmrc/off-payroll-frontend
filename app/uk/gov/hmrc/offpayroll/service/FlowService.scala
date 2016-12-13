/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.service

import play.api.Logger
import uk.gov.hmrc.offpayroll._
import uk.gov.hmrc.offpayroll.connectors.DecisionConnector
import uk.gov.hmrc.offpayroll.models.{UNKNOWN, _}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import uk.gov.hmrc.play.http.HeaderCarrier

import scala.concurrent.Future

/**
  * Created by peter on 09/12/2016.
  */
abstract class FlowService {

  /**
    *
    * @param interview
    * @return
    */
  def evaluateInterview(interview: Map[String, String], currentQnA: (String, String)): Future[InterviewEvalResult]

  def getStart(): Element

  def getCurrent(clusterId: Int, elementId: Int): Element

}


object IR35FlowService extends FlowService {

  private val weblow: Webflow = OffPayrollWebflow

  lazy val decisionConnector: DecisionConnector = DecisionConnector

  override def getStart(): Element = weblow.getStart()

  private def guardValidEelement(currentTag: String): Element = {
    val tag = weblow.getElementByTag(currentTag)
    if (tag.isEmpty) throw new IllegalAccessException("No Such Element: " + currentTag)
    else tag.get
  }

  private def getStatus(decision: DecideResponse):DecisionType = decision.result match {
    case "Outside IR35" => OUT
    case "Inside IR35" => IN
    case "Unknown" => UNKNOWN
    case _ => UNKNOWN
  }

  override def evaluateInterview(interview: Map[String, String], currentQnA: (String, String)): Future[InterviewEvalResult] = {

    implicit val hc = HeaderCarrier()
    val currentTag = currentQnA._1
    lazy val currentElement: Element = guardValidEelement(currentTag)
    val currentCluster = weblow.getClusterByName(currentTag.takeWhile(c => c != '.'))

    if (currentCluster.shouldAskForDecision(interview)) {
      decisionConnector.decide(DecisionBuilder.buildDecisionRequest(interview)).map[InterviewEvalResult](
        decision => {
          Logger.debug("Decision received from Decision Service: " + decision)
            if (getStatus(decision) == UNKNOWN) {
              if (weblow.getNext(currentElement).isEmpty)
                InterviewEvalResult(Option.empty[Element], Option.apply(new Decision(interview, UNKNOWN)), false)
              else
                InterviewEvalResult(weblow.getNext(currentElement), Option.empty[Decision], true)
            } else {
              InterviewEvalResult(Option.empty[Element], Option.apply(new Decision(interview, getStatus(decision))), false)
            }
          }
          )
    } else {
      continueWithNextQuestion(currentElement)
    }
  }

  private def dummyResult(interview: Map[String, String]): InterviewEvalResult =
    new InterviewEvalResult(Option.empty[Element], Option.apply(new Decision(interview, OUT)), false)

  private def continueWithNextQuestion(currentElement: Element): Future[InterviewEvalResult] = scala.concurrent.Future[InterviewEvalResult] {
    val nextElement = weblow.getNext(currentElement)
    if(nextElement.nonEmpty) new InterviewEvalResult(nextElement, Option.empty[Decision], true)
    else new InterviewEvalResult(Option.empty[Element], Option.apply(new Decision(Map("unknown" -> "false"), UNKNOWN)), false) //error case
  }

  override def getCurrent(clusterId: Int, elementId: Int): Element = {
    val currentElement = weblow.getEelmentById(clusterId, elementId)
    if (currentElement.nonEmpty) currentElement.head
    else throw new NoSuchElementException("No Element found matching: " + clusterId + "/" + elementId)
  }

}

case class InterviewEvalResult(element: Option[Element], decision: Option[Decision], continueWithQuestions: Boolean) {
}
