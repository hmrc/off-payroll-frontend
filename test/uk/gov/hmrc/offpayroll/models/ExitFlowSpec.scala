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
import uk.gov.hmrc.offpayroll.resources._
import uk.gov.hmrc.play.test.WithFakeApplication

/**
  * Created by peter on 11/01/2017.
  */
class ExitFlowSpec  extends FlatSpec with WithFakeApplication with Matchers {

  private val answers = PropertyFileLoader.transformMapFromQuestionTextToAnswers("exit")
  private val emptyInterview = Map[String, String]()

  val start = ExitFlow.getStart(emptyInterview)

  "An ExitFlow " should "get the start Element from the ExitCluster " in {
    start.isDefined shouldBe true
    start.get.questionTag shouldBe "exit.officeHolder"
  }

  it should "have an exit cluster " in {
    ExitFlow.getClusterByName("exit").name shouldBe "exit"
  }

  it should "get an element by its tag name" in {
    ExitFlow.getElementByTag("exit.officeHolder").isDefined shouldBe true
  }

  it should "get an element bu its id and cluster id " in {
    ExitFlow.getElementById(0,0).isDefined shouldBe true
  }

  it should "get the next element will be empty as we have one Question" in {
    start.isDefined shouldBe true
    ExitFlow.getNext(start.get).isDefined shouldBe false
  }

  it should "indicate that office holder was yes and an in IR35 Decision has been reached " in {
    val result = ExitFlow.shouldAskForNext(Map(officeHolderYes), officeHolderYes)

    result.inIr35 shouldBe true
    result.element.isEmpty shouldBe true
  }

  it should "indicate that office holder was no and continue " in {
    val result = ExitFlow.shouldAskForNext(Map(officeHolderNo), officeHolderNo)

    result.inIr35 shouldBe false
    result.element.isEmpty shouldBe true
  }

  it should "be at version 1.2.0-final" in {
    ExitFlow.version shouldBe "1.2.0-final"
  }

}
