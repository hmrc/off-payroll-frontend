/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.sections.businessOnOwnAccount

import assets.messages.FirstContractMessages
import controllers.sections.businessOnOwnAccount.routes
import forms.sections.businessOnOwnAccount.FirstContractFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.businessOnOwnAccount.FirstContractView

class FirstContractViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.firstContract"

  val form = new FirstContractFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[FirstContractView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "FirstContractView" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.FirstContractController.onSubmit(NormalMode).url)


    "the WhoAreYou is Worker" must {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(FirstContractMessages.Worker.title, Some(FirstContractMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe FirstContractMessages.Worker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe FirstContractMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe FirstContractMessages.no
      }
    }

    "the WhoAreYou is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(FirstContractMessages.Hirer.title, Some(FirstContractMessages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe FirstContractMessages.Hirer.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe FirstContractMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe FirstContractMessages.no
      }
    }
  }
}
