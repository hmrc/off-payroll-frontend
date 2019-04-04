package views

import views.behaviours.ViewBehaviours
import views.html.$className$View

class $className$ViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "$className;format="decap"$"

  val view = injector.instanceOf[$className$View]

  def createView = () => view(frontendAppConfig)(fakeRequest, messages)

  "$className$ view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)
  }
}
