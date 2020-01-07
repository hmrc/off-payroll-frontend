/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.sections.partParcel

import assets.messages.{LineManagerDutiesMessages, SubHeadingMessages}
import forms.sections.partAndParcel.LineManagerDutiesFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.partParcel.LineManagerDutiesView

class LineManagerDutiesViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.lineManagerDuties"

  val form = new LineManagerDutiesFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[LineManagerDutiesView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages,frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages,frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "LineManagerDuties view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(LineManagerDutiesMessages.Worker.title, Some(SubHeadingMessages.partAndParcel))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe LineManagerDutiesMessages.Worker.heading
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe LineManagerDutiesMessages.Worker.p1
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(LineManagerDutiesMessages.Hirer.title, Some(SubHeadingMessages.partAndParcel))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe LineManagerDutiesMessages.Hirer.heading
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe LineManagerDutiesMessages.Worker.p1
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(LineManagerDutiesMessages.Worker.title, Some(SubHeadingMessages.partAndParcel))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe LineManagerDutiesMessages.Worker.heading
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe LineManagerDutiesMessages.Worker.p1
      }
    }
  }
}
