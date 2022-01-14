/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.errors

import views.behaviours.ViewBehaviours
import views.html.errors.UnauthorisedView

class UnauthorisedViewSpec extends ViewBehaviours {

  val view = injector.instanceOf[UnauthorisedView]

  def createView = () => view(frontendAppConfig)(fakeRequest, messages)

  "Unauthorised view" must {

    behave like normalPage(createView, "unauthorised", hasSubheading = false)
  }
}
