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

package uk.gov.hmrc.offpayroll.models

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by brianheathcote on 15/07/2017.
  */

class LogInterviewBuilderSpec extends FlatSpec with Matchers  {

  private val interview = Map(
    "exit" -> Map("officeHolder" -> "Yes"),
    "setup" -> Map("endUserRole" -> "endClient", "hasContractStarted" -> "Yes", "provideServices" -> "partnership"),
  "personalService" -> Map("workerSentActualSubstitute" -> "Yes"))

  private val TEST_CORRELATION_ID = "00000001099"
  private val TEST_COMPRESSED_INTERVIEW = "DIFqfup0a"
  private val TEST_VERSION = "1.5.0-final"
  private val TEST_DECISION ="Inside IR35"
  private val decisionRequest = DecisionRequest(TEST_VERSION, TEST_CORRELATION_ID, interview)
  private val decision = DecisionResponse(TEST_VERSION, TEST_CORRELATION_ID, Map("testKey" -> "testValue"), TEST_DECISION)

  val logInterview = LogInterviewBuilder.buildLogRequest(decisionRequest, TEST_COMPRESSED_INTERVIEW, decision)


  "A call to LogInterviewBuilder.buildLogRequest" should "return a populated LogInterview object" in {
    logInterview.isInstanceOf[LogInterview] shouldBe true
  }

  it should "contain the version passed in the decisionRequest in" in {
    logInterview.version shouldBe TEST_VERSION
  }

  it should "contain the compressed interview passed in" in {
    logInterview.compressedInterview shouldBe TEST_COMPRESSED_INTERVIEW
  }

  it should "contain the decision retrieved from the decisionResponse passed in" in {
    logInterview.decision shouldBe TEST_DECISION
  }

  it should "determine the route form the values passed in" in {
    logInterview.route shouldBe "IR35"
  }

  it should "create and populate an exit object from the interview passed in the decisionRequest" in {
    logInterview.exit.officeHolder shouldBe "Yes"
  }

  it should "create and populate a setup object from the interview passed in the decisionRequest" in {
    logInterview.setup.endUserRole shouldBe "endClient"
    logInterview.setup.hasContractStarted shouldBe "Yes"
    logInterview.setup.provideServices shouldBe "partnership"
  }

  it should "contain a valid PersonalService section" in {
    val personalService = logInterview.personalService
    personalService shouldBe defined
    personalService.get.workerSentActualSubstitute shouldBe defined
    personalService.get.workerSentActualSubstitute.get shouldBe "Yes"
  }

}
