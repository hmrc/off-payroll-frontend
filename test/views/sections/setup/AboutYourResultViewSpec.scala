/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.sections.setup

import assets.messages.AboutYourResultMessages
import play.api.mvc.Call
import views.behaviours.ViewBehaviours
import views.html.sections.setup.AboutYourResultView

class AboutYourResultViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors {
    val p1 = "#content > article > form > p"
  }

  val messageKeyPrefix = "aboutYourResult"

  val view = injector.instanceOf[AboutYourResultView]

  def createView = () => view(Call("POST", "/"))(fakeRequest, messages, frontendAppConfig)

  "AboutYourResults view" must {

    behave like normalPage(
      createView,
      messageKeyPrefix,
      hasSubheading = false
    )

    behave like pageWithBackLink(createView, frontendAppConfig.govUkStartPageUrl)

    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(AboutYourResultMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe AboutYourResultMessages.heading
    }

    "have the correct p1" in {
      document.select(Selectors.p1).text mustBe AboutYourResultMessages.p1
    }

    "have the correct warning" in {
      document.select(Selectors.exclamation).text mustBe s"${AboutYourResultMessages.p2} ${AboutYourResultMessages.p3}"
    }
  }
}
