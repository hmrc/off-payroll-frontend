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

package uk.gov.hmrc.offpayroll

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by peter on 22/12/2016.
  */
class PropertyFileLoaderSpec  extends FlatSpec with Matchers {

  val personalServiceKey = "personalService.wouldWorkerPayHelper"

  "A Property File Loader " should " get the messages file as a Map[String, String]" in {
    val messagesMap = PropertyFileLoader.getMessagesFileAsMap

    messagesMap.contains(personalServiceKey) shouldBe true

  }

  it should "Filter the messages to the ones for a given cluster" in {
    val cluster = PropertyFileLoader.getMessagesForACluster("personalService")
    
    cluster.contains(personalServiceKey) shouldBe true
    cluster.size shouldBe 10


  }

  it should "Filter the messages in the Exit Cluster" in {
    val cluster = PropertyFileLoader.getMessagesForACluster("exit")

    cluster.contains("exit.officeHolder") shouldBe true
    cluster.size shouldBe 1

  }

  it should "Drop Children style tags" in {
    val noChildren = PropertyFileLoader.transformMapFromQuestionTextToAnswersDropChildren("control")
    val answers = PropertyFileLoader.transformMapToAListOfAnswers(noChildren)

  }

}
