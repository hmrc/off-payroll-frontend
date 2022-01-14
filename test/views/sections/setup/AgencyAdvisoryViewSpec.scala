/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.sections.setup

import assets.messages.AgencyAdvisoryMessages
import play.api.mvc.Call
import views.behaviours.ViewBehaviours
import views.html.sections.setup.AgencyAdvisoryView

class AgencyAdvisoryViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors {
    val finish = "#finish-link"
  }

  val messageKeyPrefix = "agencyAdvisory"

  val view = injector.instanceOf[AgencyAdvisoryView]

  def continueCall = Call("POST", "/foo")

  def createView = () => view(continueCall)(fakeRequest, messages, frontendAppConfig)

  "AgencyAdvisory view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)
    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(AgencyAdvisoryMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe AgencyAdvisoryMessages.heading
    }

    "have the correct p1" in {
      document.select(Selectors.p(1)).text mustBe AgencyAdvisoryMessages.p1
    }
  }
}
