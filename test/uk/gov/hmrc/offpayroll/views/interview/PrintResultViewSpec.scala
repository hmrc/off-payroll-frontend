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

package uk.gov.hmrc.offpayroll.views.interview

import com.kenshoo.play.metrics.PlayModule
import org.scalatest.concurrent.ScalaFutures
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms.single
import play.api.data.Forms.text
import play.api.i18n.Messages.Implicits._
import play.api.test.FakeRequest
import play.api.test.Helpers.POST
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.controllers.PrintResult
import uk.gov.hmrc.offpayroll.models.{Decision, IN}
import uk.gov.hmrc.offpayroll.util.ResultPageHelper
import uk.gov.hmrc.offpayroll.views.html.interview.printResult
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}


class PrintResultViewSpec extends UnitSpec with WithFakeApplication with ScalaFutures {


  override def bindModules = Seq(new PlayModule)

  val printResultObject: PrintResult = PrintResult(false, "IN", "1.4.0-final", "dummy", "Exit", "John", "Home Office", "Cleaner", None)
  implicit val request = (FakeRequest(POST, "/check-employment-status-for-tax/dummy"))
  val emptyForm = Form(single("" -> text))

  private val helpText = "An office holder holds an office"
  private val helpTextQuestionTag = "exit.officeHolder"

  val interview = List(helpTextQuestionTag -> List("No"))
  
  val fragments: Map[String, Html] = Map(helpTextQuestionTag -> Html(helpText))
  private val personalservice = "personalService"
  val resultPageHelper = ResultPageHelper(interview, IN, fragments, personalservice, true)
  val decision = Decision(Map(),IN,"1.5.0",personalservice)

  "printResult view" should {
    "display the correct help text and guidance" in {
      val displayDecision: Html = printResult(printResultObject, decision, resultPageHelper, emptyForm)

      displayDecision.body.contains(helpText) shouldBe true
    }
  }


}
