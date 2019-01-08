/*
 * Copyright 2019 HM Revenue & Customs
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
import play.api.http.Status
import play.api.libs.ws.WSClient
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, route, _}
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.connectors.PdfGeneratorConnector
import uk.gov.hmrc.offpayroll.models.{Decision, IN, UNKNOWN}
import uk.gov.hmrc.offpayroll.util.InterviewSessionStack.{asMap, asRawList}
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms.single
import uk.gov.hmrc.offpayroll.services.FragmentService
import uk.gov.hmrc.offpayroll.util.{ResultPageHelper, TestHtmlHelper}
import uk.gov.hmrc.offpayroll.util.TestHtmlHelper.containsHiddenInput
import uk.gov.hmrc.offpayroll.views.html.interview.display_decision
import play.api.data.Forms._

/**
  * Created by peter on 09/01/2017.
  */
class DisplayDecisionViewSpec extends UnitSpec with WithFakeApplication with ScalaFutures {

  override def bindModules = Seq(new PlayModule)

  "display_decision view" should {
    "have the correct hidden fields" in {

      val decisionCluster = "personalService"
      val version = "1.2.0-final"
      val in = IN
      val decision = Decision(Map(), in, version, decisionCluster)
      val compressedInterview = "dummy"
      implicit val request = (FakeRequest(POST, "/check-employment-status-for-tax/dummy"))
      val fragments: Map[String, Html] = Map()

      val displayDecision: Html = display_decision(decision, List(), true, compressedInterview, ResultPageHelper(List(),UNKNOWN,fragments, "partParcel", true), Form(single("" -> text)), false)

      val hiddenFields = List(
        ("decisionCluster", decisionCluster),
        ("decisionVersion", version),
        ("compressedInterview", compressedInterview),
        ("decisionResult", in.toString),
        ("esi", true.toString)
      )

      hiddenFields.map{
        case (name, value) => containsHiddenInput(displayDecision.toString(),name, value)
      }.forall(identity) shouldBe true
    }
  }
}
