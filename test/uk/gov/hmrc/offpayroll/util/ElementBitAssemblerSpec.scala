/*
 * Copyright 2018 HM Revenue & Customs
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
import uk.gov.hmrc.offpayroll.models.{FinancialRiskCluster, PersonalServiceCluster}
import uk.gov.hmrc.offpayroll.util.ElementBitAssemblerImplicits._

class ElementBitAssemblerSpec extends FlatSpec with Matchers {
  it should "convert bit value and element into string values" in {
    val value = FinancialRiskCluster.clusterElements(0).fromBitValue(17)
    value shouldBe "|financialRisk.workerProvidedMaterials|financialRisk.expensesAreNotRelevantForRole"
  }

  it should "convert bit value and element into string values (2)" in {
    val value = PersonalServiceCluster.clusterElements(1).fromBitValue(1)
    value shouldBe "No"
  }

  it should "convert bit value and element into string values (3)" in {
    val value = PersonalServiceCluster.clusterElements(0).fromBitValue(3)
    value shouldBe "personalService.workerSentActualSubstitute.noSubstitutionHappened"
  }
}
