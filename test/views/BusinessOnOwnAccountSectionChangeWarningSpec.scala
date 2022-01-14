/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views

import assets.messages.BusinessOnOwnAccountSectionChangeWarningMessages
import play.api.mvc.Call
import views.behaviours.ViewBehaviours
import views.html.BusinessOnOwnAccountSectionChangeWarningView

class BusinessOnOwnAccountSectionChangeWarningSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "businessOnOwnAccountSectionChangeWarning"

  val view = injector.instanceOf[BusinessOnOwnAccountSectionChangeWarningView]

  def continueCall = Call("GET", "/foo")

  def createView = () => view(continueCall)(fakeRequest, messages, frontendAppConfig)

  "BusinessOnOwnAccountSectionChangeWarning view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(BusinessOnOwnAccountSectionChangeWarningMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe BusinessOnOwnAccountSectionChangeWarningMessages.heading
    }
  }
}
