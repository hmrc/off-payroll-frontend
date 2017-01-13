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
  * Created by peter on 09/01/2017.
  */
class SetupFlowSpec  extends FlatSpec with Matchers {

  private val setupFlow = SetupFlow
  private val firstElement = setupFlow.getStart()
  private val SETUP = "setup"
  private val setup_endUserRole = SETUP + ".endUserRole"
  private val YES = "Yes"

  "A SetupFlow " should " be at version 1.0.1-beta " in {
    setupFlow.version shouldBe "1.0.1-beta"
  }


  it should "get the " + setup_endUserRole + " as the first element" in {
    firstElement.questionTag shouldBe setup_endUserRole
  }

  it should "get an element by id only when the cluster is Zero and the element is in range " in {
    setupFlow.getElementById(0,1).nonEmpty shouldBe true
    setupFlow.getElementById(0,10).nonEmpty shouldBe false
    setupFlow.getElementById(1,1).nonEmpty shouldBe false
  }

  it should "get the next element relative the element passed in or empty if there are no more"  in {
    setupFlow.getNext(firstElement).nonEmpty shouldBe true
  }

  it should "get an element by its tag e.g. " + setup_endUserRole in {
    setupFlow.getElementByTag(setup_endUserRole).nonEmpty shouldBe true
  }

  it should "get a cluster by its name if the name is " + SETUP in {
    setupFlow.getClusterByName(SETUP).name shouldBe SETUP
  }

  it should "shouldAskForNext " + setup_endUserRole in {
    setupFlow.shouldAskForNext(Map(setup_endUserRole -> YES), setup_endUserRole -> YES).nonEmpty shouldBe true
  }

}
