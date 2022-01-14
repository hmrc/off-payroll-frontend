/*
 * Copyright 2022 HM Revenue & Customs
 *
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
    val startAgain = "#start-again"
    val understandingOffPayroll = "#understanding-off-payroll"
  }

  val messageKeyPrefix = "worker.intermediaryResult"

  val view = injector.instanceOf[IntermediaryView]

  def createView = () => view(controllers.routes.StartAgainController.redirectToDisclaimer
)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest =
    (req: Request[_]) => view(controllers.routes.StartAgainController.redirectToDisclaimer
)(req, messages, frontendAppConfig)


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
    }
  }
}
