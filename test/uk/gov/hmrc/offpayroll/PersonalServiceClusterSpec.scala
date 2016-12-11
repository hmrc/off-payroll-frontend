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

package uk.gov.hmrc.offpayroll

import java.util.Properties

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by peter on 11/12/2016.
  */
class PersonalServiceClusterSpec extends FlatSpec with Matchers {

  private val personalServiceCluster = PersonalServiceCluster
  private val partialAnswers = List(("personalService.workerSentActualSubstitiute", "false"))

  private val propsFilteredByCluster = PropertyFileLoader.getMessagesForACluster("personalService")

  private val allAnswers = PropertyFileLoader.convertMapToAListOfAnswers(propsFilteredByCluster)

  "The Personal Service Cluster " should
  "say continue if not all questions have been aswered" in {
    assert(personalServiceCluster.shouldAskForDecision(partialAnswers) === false, "Need more answers")
  }
  
  it should "say complete when all the questions are present" in {
    assert(personalServiceCluster.shouldAskForDecision(allAnswers) === true, "We can ask for a decision")
  }

}
