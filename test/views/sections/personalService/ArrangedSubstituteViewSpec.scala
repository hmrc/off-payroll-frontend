/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.sections.personalService

import assets.messages.{ArrangedSubstituteMessages, SubHeadingMessages}
import forms.sections.personalService.ArrangedSubstituteFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.personalService.ArrangedSubstituteView

class ArrangedSubstituteViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.arrangedSubstitute"

  val form = new ArrangedSubstituteFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[ArrangedSubstituteView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "ArrangedSubstitute view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ArrangedSubstituteMessages.Worker.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ArrangedSubstituteMessages.Worker.heading
      }

      "have the correct content" in {
        document.select(Selectors.p(1)).text mustBe ArrangedSubstituteMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ArrangedSubstituteMessages.Worker.yesClientAgreed
        document.select(Selectors.multichoice(2)).text mustBe ArrangedSubstituteMessages.Worker.yesClientNotAgreed
        document.select(Selectors.multichoice(3)).text mustBe ArrangedSubstituteMessages.Worker.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ArrangedSubstituteMessages.Hirer.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ArrangedSubstituteMessages.Hirer.heading
      }

      "have the correct content" in {
        document.select(Selectors.p(1)).text mustBe ArrangedSubstituteMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ArrangedSubstituteMessages.Hirer.yesClientAgreed
        document.select(Selectors.multichoice(2)).text mustBe ArrangedSubstituteMessages.Hirer.yesClientNotAgreed
        document.select(Selectors.multichoice(3)).text mustBe ArrangedSubstituteMessages.Hirer.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ArrangedSubstituteMessages.Worker.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ArrangedSubstituteMessages.Worker.heading
      }

      "have the correct content" in {
        document.select(Selectors.p(1)).text mustBe ArrangedSubstituteMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ArrangedSubstituteMessages.Worker.yesClientAgreed
        document.select(Selectors.multichoice(2)).text mustBe ArrangedSubstituteMessages.Worker.yesClientNotAgreed
        document.select(Selectors.multichoice(3)).text mustBe ArrangedSubstituteMessages.Worker.no
      }
    }
  }
}
