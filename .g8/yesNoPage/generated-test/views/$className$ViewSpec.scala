package views

import assets.messages.$className$Messages
import config.SessionKeys
import play.api.data.Form
import controllers.routes
import forms.$className$FormProvider
import views.behaviours.YesNoViewBehaviours
import models.NormalMode
import models.UserType.{Hirer, Worker}
import play.api.libs.json.Json
import play.api.mvc.Request
import views.html.$className$View

class $className$ViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.$className;format="decap"$"

  val form = new $className$FormProvider()()

  val view = injector.instanceOf[$className$View]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "$className$View" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.$className$Controller.onSubmit(NormalMode).url)

    "the UserType is Worker" must {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title($className$Messages.Worker.title, Some($className$Messages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe $className$Messages.Worker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe $className$Messages.yes
        document.select(Selectors.multichoice(2)).text mustBe $className$Messages.no
      }
    }

    "the UserType is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title($className$Messages.Hirer.title, Some($className$Messages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe $className$Messages.Hirer.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe $className$Messages.yes
        document.select(Selectors.multichoice(2)).text mustBe $className$Messages.no
      }
    }
  }
}
