/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views.errors

import assets.messages.NotFoundMessages
import views.behaviours.ViewBehaviours
import views.html.errors.NotFoundView

class NotFoundViewSpec extends ViewBehaviours {

  val view = injector.instanceOf[NotFoundView]

  object Selectors extends BaseCSSSelectors{
    val link = "#content > article > p:nth-child(2) > a"
    val p1 = "#content > article > p:nth-child(2)"
    val p2 = "#content > article > p:nth-child(3)"
  }

  def createView = () => view(frontendAppConfig)(fakeRequest, messages)

  "notfound view" must {

    behave like normalPage(createView, "newPageNotFoundErrorMessage", hasSubheading = false)

    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(NotFoundMessages.title)
    }

    "have the correct heading" in {
      document.title mustBe title(NotFoundMessages.heading)
    }

    "have the correct p1" in {
      document.select(Selectors.p1).text mustBe NotFoundMessages.p1
    }


    "have the correct start again href" in {
      document.select(Selectors.link).attr("href") mustBe "/check-employment-status-for-tax/redirect-to-disclaimer"
    }

    "have the correct p2" in {
      document.select(Selectors.p2).text mustBe NotFoundMessages.p2
    }


  }


}
