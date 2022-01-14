/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views

import assets.messages.PersonalServiceSectionChangeWarningMessages
import play.api.mvc.Call
import views.behaviours.ViewBehaviours
import views.html.PersonalServiceSectionChangeWarningView

class PersonalServiceSectionChangeWarningSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "personalServiceSectionChangeWarning"

  val view = injector.instanceOf[PersonalServiceSectionChangeWarningView]

  def continueCall = Call("GET", "/foo")

  def createView = () => view(continueCall)(fakeRequest, messages, frontendAppConfig)

  "PersonalServiceSectionChangeWarning view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(PersonalServiceSectionChangeWarningMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe PersonalServiceSectionChangeWarningMessages.heading
    }
  }
}
