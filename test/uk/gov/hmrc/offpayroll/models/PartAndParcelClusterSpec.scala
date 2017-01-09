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

class PartAndParcelClusterSpec extends FlatSpec with Matchers {

  private val partAndParcelCluster = PartAndParcelCluster

  private val propsFilteredByCluster = PropertyFileLoader.getMessagesForACluster("partParcel")

  "The Part and Parcel Cluster "
  it should " have the correct name " in {
    partAndParcelCluster.name shouldBe "partParcel"
  }
  it should " have the correct clusterId " in {
    partAndParcelCluster.clusterID shouldBe 4
  }
  it should " have the correct amount of question tags " in {
    partAndParcelCluster.clusterElements.size shouldBe 4
  }
  it should " not ask for a decision when there is no flow logic and there are more questions " in {
    val currentQnA = ("partParcel.workerReceivesBenefits", "No")
    val partialAnswers = List(currentQnA)

    val maybeElement = partAndParcelCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "partParcel.workerAsLineManager"
  }
  it should " ask 'partParcel.contactWithEngagerCustomer' after asking 'partParcel.workerAsLineManager' " in {
    val currentQnA = ("partParcel.workerAsLineManager", "No")
    val partialAnswers = List(
      ("partParcel.workerReceivesBenefits", "No"),
      currentQnA)

    val maybeElement = partAndParcelCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "partParcel.contactWithEngagerCustomer"
  }

  it should " ask for a decision when all questions have been asked " in {
    val currentQnA = ("partParcel.workerRepresentsEngagerBusiness", "Yes")
    val allAnswers = PropertyFileLoader.transformMapToAListOfAnswers(propsFilteredByCluster)

    partAndParcelCluster.shouldAskForDecision(allAnswers, currentQnA).isEmpty shouldBe true
  }

  it should " ask for a decision there is some flow logic and there are no more questions " in {
    val currentQnA = ("partParcel.workerReceivesBenefits", "Yes")
    val partialAnswers = List(currentQnA)

    partAndParcelCluster.shouldAskForDecision(partialAnswers, currentQnA).isEmpty shouldBe true
  }

  it should " ask for a decision there is some flow logic and there are no more questions - another flavour " in {
    val currentQnA = ("partParcel.contactWithEngagerCustomer", "No")
    val partialAnswers = List(
      ("partParcel.workerReceivesBenefits", "No"),
      ("partParcel.workerAsLineManager", "No"),
      currentQnA)

    partAndParcelCluster.shouldAskForDecision(partialAnswers, currentQnA).isEmpty shouldBe true
  }
}
