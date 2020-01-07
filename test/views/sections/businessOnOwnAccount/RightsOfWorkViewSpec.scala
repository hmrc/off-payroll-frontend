/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.sections.businessOnOwnAccount

import assets.messages.RightsOfWorkMessages
import forms.sections.businessOnOwnAccount.RightsOfWorkFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.businessOnOwnAccount.RightsOfWorkView



class RightsOfWorkViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.rightsOfWork"

  val form = new RightsOfWorkFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[RightsOfWorkView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "RightsOfWork view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RightsOfWorkMessages.Worker.title, Some(RightsOfWorkMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RightsOfWorkMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RightsOfWorkMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RightsOfWorkMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RightsOfWorkMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RightsOfWorkMessages.Hirer.title,  Some(RightsOfWorkMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RightsOfWorkMessages.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RightsOfWorkMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RightsOfWorkMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RightsOfWorkMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RightsOfWorkMessages.Worker.title, Some(RightsOfWorkMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RightsOfWorkMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RightsOfWorkMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RightsOfWorkMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RightsOfWorkMessages.no
      }
    }
  }
}

