/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.sections.setup

import assets.messages.ContractStartedOptimisedMessages
import config.featureSwitch.FeatureSwitching
import controllers.sections.setup.routes
import forms.sections.setup.ContractStartedFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.setup.ContractStartedView

class ContractStartedViewSpec extends YesNoViewBehaviours with FeatureSwitching{

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "contractStarted"

  val form = new ContractStartedFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[ContractStartedView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "ContractStarted view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.ContractStartedController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {

        document.title mustBe title(ContractStartedOptimisedMessages.Worker.title)
      }

      "have the correct heading" in {

        document.select(Selectors.heading).text mustBe ContractStartedOptimisedMessages.Worker.heading
      }

      "have the correct radio option messages" in {

        document.select(Selectors.multichoice(1)).text mustBe ContractStartedOptimisedMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe ContractStartedOptimisedMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {

        document.title mustBe title(ContractStartedOptimisedMessages.Hirer.title)
      }

      "have the correct heading" in {

        document.select(Selectors.heading).text mustBe ContractStartedOptimisedMessages.Hirer.heading
      }

      "have the correct radio option messages" in {

        document.select(Selectors.multichoice(1)).text mustBe ContractStartedOptimisedMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe ContractStartedOptimisedMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {

        document.title mustBe title(ContractStartedOptimisedMessages.NonTailored.title)
      }

      "have the correct heading" in {

        document.select(Selectors.heading).text mustBe ContractStartedOptimisedMessages.NonTailored.heading
      }

      "have the correct radio option messages" in {

        document.select(Selectors.multichoice(1)).text mustBe ContractStartedOptimisedMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe ContractStartedOptimisedMessages.no
      }
    }
  }
}
