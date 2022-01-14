/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.sections.businessOnOwnAccount

import assets.messages.MultipleContractsMessages
import controllers.sections.businessOnOwnAccount.routes
import forms.sections.businessOnOwnAccount.MultipleContractsFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.businessOnOwnAccount.MultipleContractsView

class MultipleContractsViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.multipleContracts"

  val form = new MultipleContractsFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[MultipleContractsView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "MultipleContractsView" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.MultipleContractsController.onSubmit(NormalMode).url)


    "the WhoAreYou is Worker" must {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(MultipleContractsMessages.Worker.title, Some(MultipleContractsMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe MultipleContractsMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe MultipleContractsMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe MultipleContractsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe MultipleContractsMessages.no
      }
    }

    "the WhoAreYou is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(MultipleContractsMessages.Hirer.title, Some(MultipleContractsMessages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe MultipleContractsMessages.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe MultipleContractsMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe MultipleContractsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe MultipleContractsMessages.no
      }
    }
  }
}
