/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.sections.personalService

import assets.messages.{SubHeadingMessages, WouldPaySubstituteMessages}
import controllers.sections.personalService.routes
import forms.sections.personalService.WouldWorkerPaySubstituteFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.personalService.WouldWorkerPaySubstituteView

class WouldWorkerPaySubstituteViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.wouldWorkerPaySubstitute"

  val form = new WouldWorkerPaySubstituteFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[WouldWorkerPaySubstituteView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "WouldWorkerPaySubstitute view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.WouldWorkerPaySubstituteController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(WouldPaySubstituteMessages.Worker.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe WouldPaySubstituteMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe WouldPaySubstituteMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe WouldPaySubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe WouldPaySubstituteMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(WouldPaySubstituteMessages.Hirer.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe WouldPaySubstituteMessages.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe WouldPaySubstituteMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe WouldPaySubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe WouldPaySubstituteMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(WouldPaySubstituteMessages.Worker.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe WouldPaySubstituteMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe WouldPaySubstituteMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe WouldPaySubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe WouldPaySubstituteMessages.no
      }
    }
  }
}
