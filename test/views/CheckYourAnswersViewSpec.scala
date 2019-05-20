package views

import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class CheckYourAnswersViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "checkYourAnswers"

  val view = injector.instanceOf[CheckYourAnswersView]

  def createView = () => view(frontendAppConfig)(fakeRequest, messages)

  "CheckYourAnswers view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)
  }
}
