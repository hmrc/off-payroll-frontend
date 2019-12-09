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

package views.sections.setup

import assets.messages.IntermediaryMessages
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.setup.IntermediaryView

class IntermediaryViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors {
    override val h2 = (i: Int) => s"#content article h2:nth-of-type($i)"
    val p1 = "#content > article > p:nth-child(3)"
    val p2 = "#content > article > p:nth-child(4)"
    val p3 = "#content > article > p:nth-child(6)"
    val p4 = "#content > article > p:nth-child(7)"
    val startAgain = "#start-again"
    val understandingOffPayroll = "#understanding-off-payroll"
  }

  val messageKeyPrefix = "worker.intermediaryResult"

  val view = injector.instanceOf[IntermediaryView]

  def createView = () => view(controllers.routes.StartAgainController.redirectToDisclaimer())(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest =
    (req: Request[_]) => view(controllers.routes.StartAgainController.redirectToDisclaimer())(req, messages, frontendAppConfig)


  "intermediary view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    "For worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeDataRequest))

      "have the correct title" in {
        document.title mustBe title(IntermediaryMessages.Worker.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe IntermediaryMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p1).text mustBe IntermediaryMessages.Worker.p1
      }

      "have the correct p2" in {
        document.select(Selectors.p2).text mustBe IntermediaryMessages.Worker.p2
      }

      "have the correct h2" in {
        document.select(Selectors.h2(1)).text mustBe IntermediaryMessages.Worker.subheading
      }

      "have the correct p3" in {
        document.select(Selectors.p3).text mustBe IntermediaryMessages.Worker.p3
      }

      "have the correct p4" in {
        document.select(Selectors.p4).text mustBe IntermediaryMessages.Worker.p4
        document.select(Selectors.understandingOffPayroll).attr("href") mustBe frontendAppConfig.understandingOffPayrollUrl
        document.select(Selectors.startAgain).attr("href") mustBe controllers.routes.StartAgainController.redirectToDisclaimer().url
      }
    }

    "For Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeDataRequest))

      "have the correct title" in {
        document.title mustBe title(IntermediaryMessages.Hirer.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe IntermediaryMessages.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p1).text mustBe IntermediaryMessages.Hirer.p1
      }

      "have the correct p2" in {
        document.select(Selectors.p2).text mustBe IntermediaryMessages.Hirer.p2
      }

      "have the correct h2" in {
        document.select(Selectors.h2(1)).text mustBe IntermediaryMessages.Hirer.subheading
      }

      "have the correct p3" in {
        document.select(Selectors.p3).text mustBe IntermediaryMessages.Hirer.p3
      }

      "have the correct p4" in {
        document.select(Selectors.p4).text mustBe IntermediaryMessages.Hirer.p4
        document.select(Selectors.understandingOffPayroll).attr("href") mustBe frontendAppConfig.understandingOffPayrollUrl
        document.select(Selectors.startAgain).attr("href") mustBe controllers.routes.StartAgainController.redirectToDisclaimer().url
      }
    }
  }
}
