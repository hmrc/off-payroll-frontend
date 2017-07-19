package uk.gov.hmrc.offpayroll.services

/*
 * Copyright 2016 HM Revenue & Customs
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

import com.kenshoo.play.metrics.PlayModule
import org.scalatest.concurrent.ScalaFutures
import uk.gov.hmrc.offpayroll.PropertyFileLoader
import uk.gov.hmrc.offpayroll.models.{OUT, UNKNOWN}
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}


/**
  * Created by peter on 09/12/2016.
  */
class FlowServiceIntegrationSpec extends UnitSpec with WithFakeApplication with ScalaFutures  {
  private val TEST_CORRELATION_ID = "00000001099"
  private val TEST_COMPRESSED_INTERVIEW = "7yYJCkUbY"

  private val personalService = PropertyFileLoader.transformMapFromQuestionTextToAnswers("personalService")
  private val csrf = "csrf"
  private val fullPlusJunk:Map[String,String] = personalService + (csrf -> "112361283681230")
  override def bindModules = Seq(new PlayModule)

  lazy val flowservice: FlowService = IR35FlowService()

  val lastElement: (String, String) = "personalService.wouldWorkerPayHelper" -> "Yes"

  "A flow Service" should {
    "Process a full Interview and give a decision" in {
      val result = await(flowservice.evaluateInterview(fullPlusJunk, lastElement, TEST_CORRELATION_ID, TEST_COMPRESSED_INTERVIEW))
      result.continueWithQuestions shouldBe false
      result.element.isEmpty shouldBe true
      result.correlationId shouldBe TEST_CORRELATION_ID
      result.decision.isDefined shouldBe true
      result.decision.map { decision =>
        decision.decision shouldBe OUT
        decision.qa.forall(value => !value._1.contains(csrf)) shouldBe true
      }
    }
  }

}
