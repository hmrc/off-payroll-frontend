/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.sections.businessOnOwnAccount

import assets.messages.ExtendContractMessages
import controllers.sections.businessOnOwnAccount.routes
import forms.sections.businessOnOwnAccount.ExtendContractFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.businessOnOwnAccount.ExtendContractView

class ExtendContractViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.extendContract"

  val form = new ExtendContractFormProvider()()

  val view = injector.instanceOf[ExtendContractView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "ExtendContractView" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.ExtendContractController.onSubmit(NormalMode).url)

    "the WhoAreYou is Worker" must {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ExtendContractMessages.Worker.title, Some(ExtendContractMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ExtendContractMessages.Worker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ExtendContractMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe ExtendContractMessages.no
      }
    }

    "the WhoAreYou is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ExtendContractMessages.Hirer.title, Some(ExtendContractMessages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ExtendContractMessages.Hirer.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ExtendContractMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe ExtendContractMessages.no
      }
    }
  }
}
