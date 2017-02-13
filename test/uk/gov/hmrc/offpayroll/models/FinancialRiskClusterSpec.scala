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
import uk.gov.hmrc.offpayroll.PropertyFileLoader

class FinancialRiskClusterSpec extends FlatSpec with Matchers with ClusterSpecHelper {

  private val financialRiskCluster = FinancialRiskCluster

  private val propsFilteredByCluster = PropertyFileLoader.getMessagesForACluster("financialRisk")

  "The Financial Risk Cluster " should " have the correct name " in {
    financialRiskCluster.name shouldBe "financialRisk"
  }
  it should "have the correct clusterId " in {
    financialRiskCluster.clusterID shouldBe 2
  }
  it should "have the correct amount of question tags " in {
    financialRiskCluster.clusterElements.size shouldBe 3
  }

  it should "have the correct set of questions" in {
    assertAllElementsPresentForCluster(financialRiskCluster) shouldBe true
  }

}
