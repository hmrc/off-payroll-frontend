/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views.sections.setup

import assets.messages.NoIntermediaryMessages
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.setup.NoIntermediaryView

class NoIntermediaryViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors {
    override val h2 = (i: Int) => s"#content article h2:nth-of-type($i)"
    val p1 = "#content > article > p:nth-child(3)"
    val p2 = "#content > article > p:nth-child(4)"
    val startAgain = "#start-again"
    val understandingOffPayroll = "#understanding-off-payroll"
  }

  val messageKeyPrefix = "worker.noIntermediaryResult"

  val view = injector.instanceOf[NoIntermediaryView]

  def createView = () => view(controllers.routes.StartAgainController.redirectToDisclaimer())(workerFakeRequest, messages, frontendAppConfig)
  def createViewWithRequest =
    (req: Request[_]) => view(controllers.routes.StartAgainController.redirectToDisclaimer())(req, messages, frontendAppConfig)

  "no intermediary view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    "for worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeDataRequest))

      "have the correct title" in {
        document.title mustBe title(NoIntermediaryMessages.Worker.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe NoIntermediaryMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p1).text mustBe NoIntermediaryMessages.Worker.p1
        document.select(Selectors.understandingOffPayroll).attr("href") mustBe frontendAppConfig.understandingOffPayrollUrl
      }

      "have the correct p2" in {
        document.select(Selectors.p2).text mustBe NoIntermediaryMessages.Worker.p2
      }

      "have the correct start again link" in {
        document.select(Selectors.startAgain).attr("href") mustBe controllers.routes.StartAgainController.redirectToDisclaimer().url
      }
    }

    "for hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeDataRequest))

      "have the correct title" in {
        document.title mustBe title(NoIntermediaryMessages.Hirer.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe NoIntermediaryMessages.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p1).text mustBe NoIntermediaryMessages.Hirer.p1
        document.select(Selectors.understandingOffPayroll).attr("href") mustBe frontendAppConfig.understandingOffPayrollUrl
      }

      "have the correct p2" in {
        document.select(Selectors.p2).text mustBe NoIntermediaryMessages.Hirer.p2
      }

      "have the correct start again link" in {
        document.select(Selectors.startAgain).attr("href") mustBe controllers.routes.StartAgainController.redirectToDisclaimer().url
      }
    }
  }
}
