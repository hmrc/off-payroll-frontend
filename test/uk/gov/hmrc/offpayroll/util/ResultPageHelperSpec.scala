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

package uk.gov.hmrc.offpayroll.util

import org.scalatest.{FlatSpec, Matchers}
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.models.{IN, OUT, UNKNOWN}
import uk.gov.hmrc.offpayroll.resources._

/**
  * Created by Habeeb on 05/05/2017.
  */
class ResultPageHelperSpec extends FlatSpec with Matchers {

  "An ResultPageHelper " should " be able to get the correct set of Q and As for a given cluster name" in {
    val resultPageHelper = ResultPageHelper(endToEndEsiInterview, IN, fragments)

    val questionsAndAnswersForCluster = resultPageHelper.getQuestionsAndAnswersForCluster("financialRisk")

    questionsAndAnswersForCluster.isEmpty shouldBe false
    questionsAndAnswersForCluster.size shouldBe 3
    checkExist(questionsAndAnswersForCluster, "financialRisk.haveToPayButCannotClaim")
    checkExist(questionsAndAnswersForCluster, "financialRisk.workerMainIncome")
    checkExist(questionsAndAnswersForCluster, "financialRisk.paidForSubstandardWork")

  }

  it should " be able to return an empty list if the given cluster name is not in the list" in {
    val questionsAndAnswersForCluster = ResultPageHelper(endToEndEsiInterview, IN, fragments).getQuestionsAndAnswersForCluster("bobby")

    questionsAndAnswersForCluster.isEmpty shouldBe true
  }

  it should " be able to return the correct 'decision type' for an ir35 office holder interview" in {

    val outcomeType = ResultPageHelper(ir35OfficeHolderYesInterview, IN, fragments).decisionKey
    outcomeType shouldBe "officeHolder.in.ir35"
  }

  it should " be able to return the correct 'decision type' for an esi office holder interview" in {
    val outcomeType = ResultPageHelper(esiOfficeHolderYesInterview, IN, fragments).decisionKey
    outcomeType shouldBe "officeHolder.in.esi"
  }

  it should " be able to return the correct 'decision type' for an ir35 personal service (early exit) current route interview" in {
    val outcomeType = ResultPageHelper(personalServiceIr35CurrentInterview, OUT, fragments).decisionKey
    outcomeType shouldBe "personalServiceCluster.current.out.ir35"
  }

  it should " be able to return the correct 'decision type' for an esi personal service (early exit) current route interview" in {
    val outcomeType = ResultPageHelper(personalServiceEsiCurrentInterview, OUT, fragments).decisionKey
    outcomeType shouldBe "earlyExit.out.esi"
  }

  it should " be able to return the correct 'decision type' for an ir35 personal service (early exit) future route interview" in {
    val outcomeType = ResultPageHelper(personalServiceIr35FutureInterview, OUT, fragments).decisionKey
    outcomeType shouldBe "personalServiceCluster.future.out.ir35"
  }

  it should " be able to return the correct 'decision type' for an esi personal service (early exit) future route interview" in {
    val outcomeType = ResultPageHelper(personalServiceEsiFutureInterview, OUT, fragments).decisionKey
    outcomeType shouldBe "earlyExit.out.esi"
  }

  it should " be able to return the correct 'decision type' for an esi control (early exit) interview" in {
    val outcomeType = ResultPageHelper(controlEsiInterview, OUT, fragments).decisionKey
    outcomeType shouldBe "earlyExit.out.esi"
  }

  it should " be able to return the correct 'decision type' for an esi financial risk (early exit) interview" in {
    val outcomeType = ResultPageHelper(financialRiskEsiInterview, OUT, fragments).decisionKey
    outcomeType shouldBe "earlyExit.out.esi"
  }

  it should " be able to return the correct 'decision type' for an ir35 control (early exit) interview" in {
    val outcomeType = ResultPageHelper(controlInterview, OUT, fragments).decisionKey
    outcomeType shouldBe "controlCluster.out.ir35"
  }

  it should " be able to return the correct 'decision type' for an ir35 financial risk (early exit) interview" in {
    val outcomeType = ResultPageHelper(financialRiskInterview, OUT, fragments).decisionKey
    outcomeType shouldBe "financialRiskCluster.out.ir35"
  }

  it should " be able to return the correct 'decision type' for an inside ir35 end to end interview" in {
    val outcomeType = ResultPageHelper(endToEndIr35Interview, IN, fragments).decisionKey
    outcomeType shouldBe "matrix.in.ir35"
  }

  it should " be able to return the correct 'decision type' for an unknown ir35 end to end interview" in {
    val outcomeType = ResultPageHelper(endToEndIr35Interview, UNKNOWN, fragments).decisionKey
    outcomeType shouldBe "matrix.unknown"
  }

  it should " be able to return the correct 'decision type' for an unknown esi end to end interview" in {
    val outcomeType = ResultPageHelper(endToEndEsiInterview, UNKNOWN, fragments).decisionKey
    outcomeType shouldBe "matrix.unknown"
  }

  def checkExist(qAndAs : List[(String, List[String])], questionTagToMatch: String): Unit ={
    qAndAs.toMap.get(questionTagToMatch).isDefined shouldBe true
  }

}
