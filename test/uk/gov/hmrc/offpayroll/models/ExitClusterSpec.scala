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

import org.scalatest
import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.resources._

/**
  * Created by peter on 08/01/2017.
  */
class ExitClusterSpec extends FlatSpec with Matchers with ClusterSpecHelper {
  val exitcluster = ExitCluster

  "An Exit Cluster " should " be named exit" in {
    exitcluster.name shouldBe "exit"
  }

  it should "be in position 2 of the list of clusters " in {
    exitcluster.clusterID shouldBe 1
  }

  it should "have 1 questions " in {
    exitcluster.clusterElements.size shouldBe 1
  }

  it should "have all the questions present in the messages for exit" in {
    assertAllElementsPresentForCluster(exitcluster) shouldBe true
  }

  it should "always ask for a decision if the officeHolder Question is yes " in {
    exitcluster.shouldAskForDecision(
      List(officeHolderYes), officeHolderYes).isEmpty shouldBe true
  }


}
