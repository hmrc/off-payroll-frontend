/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.sections.personalService

import assets.messages.{NeededToPayHelperMessages, SubHeadingMessages}
import controllers.sections.personalService.routes
import forms.sections.personalService.NeededToPayHelperFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.personalService.NeededToPayHelperView

class NeededToPayHelperViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.neededToPayHelper"

  val form = new NeededToPayHelperFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[NeededToPayHelperView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "NeededToPayHelper view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.NeededToPayHelperController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(NeededToPayHelperMessages.Worker.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe NeededToPayHelperMessages.Worker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe NeededToPayHelperMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe NeededToPayHelperMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(NeededToPayHelperMessages.Hirer.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe NeededToPayHelperMessages.Hirer.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe NeededToPayHelperMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe NeededToPayHelperMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(NeededToPayHelperMessages.Worker.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe NeededToPayHelperMessages.Worker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe NeededToPayHelperMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe NeededToPayHelperMessages.no
      }
    }
  }
}
