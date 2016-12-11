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

import uk.gov.hmrc.offpayroll.{Decision, Element, OffPayrollWebflow, Webflow}

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

  /**
    *
    * @param interview
    * @return
    */
  override def evaluateInterview(interview: Map[String, String], currentQnA: (String, String)): Result = {
    // make an ordered List of (String, String) based on the question order in the elementClusters

    // have we asked enough questions to ask for a decision

    // yes call the decision service

      // Decision Service gives a decision - e.g. NOT UNKNOWN

      // Descision Service returns UNKNOWN

    // no return
    // get the next question based on the interview so far
    // continueWithQuestions == true

    return null
  }

}

case class Result(element: Option[Element], decision: Option[Decision], continueWithQuestions: Boolean) {
}
