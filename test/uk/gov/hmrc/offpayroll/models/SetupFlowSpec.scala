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
import uk.gov.hmrc.offpayroll.resources._
import uk.gov.hmrc.play.test.WithFakeApplication

/**
  * Created by peter on 09/01/2017.
  */
class SetupFlowSpec  extends FlatSpec with WithFakeApplication with Matchers {

  private val setupFlow = SetupFlow
  private val maybeElement = setupFlow.getStart(Map[String, String]())


  "A SetupFlow " should " be at version 1.2.0-final " in {
    setupFlow.version shouldBe "1.2.0-final"
  }


  it should "get the " + setup_endUserRole + " as the first element" in {
    maybeElement.isDefined shouldBe true
    maybeElement.get.questionTag shouldBe setup_endUserRole
  }

  it should "get an element by id only when the cluster is Zero and the element is in range " in {
    setupFlow.getElementById(0,1).nonEmpty shouldBe true
    setupFlow.getElementById(0,10).nonEmpty shouldBe false
    setupFlow.getElementById(1,1).nonEmpty shouldBe false
  }

  it should "get the next element relative the element passed in or empty if there are no more"  in {
    setupFlow.getNext(maybeElement.get).nonEmpty shouldBe true
  }

  it should "get an element by its tag e.g. " + setup_endUserRole in {
    setupFlow.getElementByTag(setup_endUserRole).nonEmpty shouldBe true
  }

  it should "get a cluster by its name if the name is " + SETUP in {
    setupFlow.getClusterByName(SETUP).name shouldBe SETUP
  }

  it should "shouldAskForNext " + setup_endUserRole in {
    setupFlow.shouldAskForNext(Map(setup_endUserRolePersonDoingWork), setup_endUserRolePersonDoingWork).maybeElement.nonEmpty shouldBe true
  }

  it should "should not exit the tool if the we are a Sole Trader trying to use the tool " in {
    val result = setupFlow.shouldAskForNext(Map(setup_endUserRolePersonDoingWork, setup_hasContractStartedYes, setup_SoleTrader), setup_SoleTrader)
    result.exitTool shouldBe false
    result.maybeElement.isEmpty shouldBe true
  }


}
