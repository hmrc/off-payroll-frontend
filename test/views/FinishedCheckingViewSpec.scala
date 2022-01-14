/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views

import assets.messages.FinishedCheckingMessages
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.FinishedCheckingView

class FinishedCheckingViewSpec extends ViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()

  }

  object Selectors extends BaseCSSSelectors{
    val exit = "#content > article > form > p > a:nth-child(1)"
    val startAgain = "#content > article > form > p > a:nth-child(2)"
    val p1 = "#content > article > form > p"
  }

  val messageKeyPrefix = "finishedChecking"

  val view = injector.instanceOf[FinishedCheckingView]

  def createView = () => view(frontendAppConfig, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => view(frontendAppConfig, NormalMode)(fakeRequest, messages)

  def createViewWithRequest = (req: Request[_]) => view(frontendAppConfig, NormalMode)(req, messages)

  "FinishedChecking view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    lazy val document = asDocument(createView())

      "have the correct title" in {
        document.title mustBe title(FinishedCheckingMessages.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe FinishedCheckingMessages.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p1).text mustBe FinishedCheckingMessages.p1
      }

     "have the correct exit href" in {
       document.select(Selectors.exit).attr("href") mustBe "/check-employment-status-for-tax/exit-survey"
     }

    "have the correct start again href" in {
      document.select(Selectors.startAgain).attr("href") mustBe "/check-employment-status-for-tax/redirect-to-disclaimer"
    }
  }
}
