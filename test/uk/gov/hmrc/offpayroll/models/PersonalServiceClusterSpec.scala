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
  * Created by peter on 11/12/2016.
  */
class PersonalServiceClusterSpec extends FlatSpec with Matchers {

  private val personalServiceCluster = PersonalServiceCluster

  "The Personal Service Cluster" should
    " if personalService.contractualObligationForSubstitute is Y then ask Question personalService.contractualObligationInPractice " in {

    val decision = personalServiceCluster.shouldAskForDecision(List("personalService.contractualObligationForSubstitute" -> "Yes"),
      "personalService.contractualObligationForSubstitute" -> "Yes")

    decision.nonEmpty shouldBe true
    decision.get.questionTag shouldBe "personalService.contractualObligationInPractice"

  }

  it should " if personalService.contractualObligationForSubstitute is N then ask Question contractualRightForSubstitute " in {
    val decision = personalServiceCluster.shouldAskForDecision(List("personalService.contractualObligationForSubstitute" -> "No"),
      "personalService.contractualObligationForSubstitute" -> "No")

    decision.nonEmpty shouldBe true
    decision.get.questionTag shouldBe "personalService.contractualRightForSubstitute"

  }

  it should " if personalService.contractualObligationInpractice is Y then ask Question contractTermsWorkerPaysSubstitute " in {
    val decision = personalServiceCluster.shouldAskForDecision(List("personalService.contractualObligationInPractice" -> "Yes"),
      "personalService.contractualObligationInPractice" -> "Yes")

    decision.nonEmpty shouldBe true
    decision.get.questionTag shouldBe "personalService.contractTermsWorkerPaysSubstitute"

  }



  it should " if personalService.contractTermsWorkerPaysSubstitute is Y then indicate no more questions " in {
    val decision = personalServiceCluster.shouldAskForDecision(List("personalService.contractTermsWorkerPaysSubstitute" -> "Yes"),
      "personalService.contractTermsWorkerPaysSubstitute" -> "Yes")

    decision.nonEmpty shouldBe false


  }



  it should " if personalService.contractualRightForSubstitute is Y then ask Question contractualRightReflectInPractice " in {
    val decision = personalServiceCluster.shouldAskForDecision(List("personalService.contractualRightForSubstitute" -> "Yes"),
      "personalService.contractualRightForSubstitute" -> "Yes")

    decision.nonEmpty shouldBe true
    decision.get.questionTag shouldBe "personalService.contractualRightReflectInPractice"

  }

}
