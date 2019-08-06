package views

import assets.messages.TransferOfRightsMessages
import config.SessionKeys
import play.api.data.Form
import controllers.routes
import forms.TransferOfRightsFormProvider
import views.behaviours.YesNoViewBehaviours
import models.NormalMode
import models.UserType.{Hirer, Worker}
import play.api.libs.json.Json
import play.api.mvc.Request
import views.html.TransferOfRightsView

class TransferOfRightsViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "transferOfRights"

  val form = new TransferOfRightsFormProvider()()

  val view = injector.instanceOf[TransferOfRightsView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "TransferOfRightsView" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.TransferOfRightsController.onSubmit(NormalMode).url)

    lazy val request = workerFakeRequest
    lazy val document = asDocument(createViewWithRequest(request))

    "have the correct title" in {
      document.title mustBe title(TransferOfRightsMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe TransferOfRightsMessages.heading
    }

    "have the correct radio option messages" in {
      document.select(Selectors.multichoice(1)).text mustBe TransferOfRightsMessages.yes
      document.select(Selectors.multichoice(2)).text mustBe TransferOfRightsMessages.no
    }
  }
}
