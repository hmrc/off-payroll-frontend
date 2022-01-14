/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.errors

import assets.messages.SessionTimeoutMessages
import views.behaviours.ViewBehaviours
import views.html.errors.SessionExpiredView

class SessionExpiredViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors {
    val startAgainButton = "a.button"
  }

  val view = injector.instanceOf[SessionExpiredView]

  def createView = () => view(frontendAppConfig)(fakeRequest, messages)

  "Session Expired view" must {
    behave like normalPage(createView, "session.expired", hasSubheading = false)
  }

  "Have a link to the IndexController" in {
    val button = asDocument(createView()).select(Selectors.startAgainButton)
    button.attr("href") mustBe controllers.routes.StartAgainController.redirectToDisclaimer.url

    button.text mustBe SessionTimeoutMessages.startAgain
  }
}
