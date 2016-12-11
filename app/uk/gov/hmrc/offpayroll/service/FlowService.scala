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

import uk.gov.hmrc.offpayroll._

/**
  * Created by peter on 09/12/2016.
  */
abstract class FlowService {

  /**
    *
    * @param interview
    * @return
    */
  def evaluateInterview(interview: Map[String, String], currentQnA: (String, String)): Result

  def getStart():Element

}


object IR35FlowService extends FlowService {

  private val weblow: Webflow = OffPayrollWebflow

  override def getStart(): Element = weblow.getStart()


  override def evaluateInterview(interview: Map[String, String], currentQnA: (String, String)): Result = {

    val currentTag = currentQnA._1
    val currentCluster = weblow.getClusterByName(currentTag.takeWhile(c => c != '.'))

    if(currentCluster.shouldAskForDecision(interview)) {
      // call the Decision Service here
      dummyResult(interview)
    } else {
      continueWithNextQuestion(currentTag)
    }
  }

  private def dummyResult(interview: Map[String, String]):Result =
    new Result(Option.empty[Element], Option.apply(new Decision(interview, OUT)), false)

  private def continueWithNextQuestion(currentTag: String): Result = {
    val optionalTag = weblow.getElementByTag(currentTag)
    if(optionalTag.nonEmpty) {
      new Result(weblow.getNext(optionalTag.head), Option.empty[Decision], true)
    } else {
      throw new IllegalArgumentException("Trying to continue with flow but no next Element found")
    }
  }
}

case class Result(element: Option[Element], decision: Option[Decision], continueWithQuestions: Boolean) {
}
