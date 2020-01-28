/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.sections.businessOnOwnAccount

import assets.messages.PreviousContractMessages
import controllers.sections.businessOnOwnAccount.routes
import forms.sections.businessOnOwnAccount.PreviousContractFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.businessOnOwnAccount.PreviousContractView

class PreviousContractViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.previousContract"

  val form = new PreviousContractFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[PreviousContractView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "PreviousContractView" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.PreviousContractController.onSubmit(NormalMode).url)

    "the WhoAreYou is Worker" must {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(PreviousContractMessages.Worker.title, Some(PreviousContractMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe PreviousContractMessages.Worker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe PreviousContractMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe PreviousContractMessages.no
      }
    }

    "the WhoAreYou is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(PreviousContractMessages.Hirer.title, Some(PreviousContractMessages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe PreviousContractMessages.Hirer.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe PreviousContractMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe PreviousContractMessages.no
      }
    }
  }
}
