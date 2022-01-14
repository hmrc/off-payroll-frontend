/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.sections.personalService

import assets.messages.{RejectSubstituteMessages, SubHeadingMessages}
import controllers.sections.personalService.routes
import forms.sections.personalService.RejectSubstituteFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.personalService.RejectSubstituteView

class RejectSubstituteViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.rejectSubstitute"

  val form = new RejectSubstituteFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[RejectSubstituteView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "RejectSubstitute view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.RejectSubstituteController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RejectSubstituteMessages.Worker.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RejectSubstituteMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RejectSubstituteMessages.Worker.p1
        document.select(Selectors.p(2)).text mustBe RejectSubstituteMessages.Worker.p2
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RejectSubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RejectSubstituteMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RejectSubstituteMessages.Hirer.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RejectSubstituteMessages.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RejectSubstituteMessages.Hirer.p1
        document.select(Selectors.p(2)).text mustBe RejectSubstituteMessages.Hirer.p2
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RejectSubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RejectSubstituteMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RejectSubstituteMessages.Worker.title, Some(SubHeadingMessages.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RejectSubstituteMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RejectSubstituteMessages.Worker.p1
        document.select(Selectors.p(2)).text mustBe RejectSubstituteMessages.Worker.p2
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RejectSubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RejectSubstituteMessages.no
      }
    }
  }
}
