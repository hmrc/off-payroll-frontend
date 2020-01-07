/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.sections.businessOnOwnAccount

import assets.messages.TransferOfRightsMessages
import controllers.sections.businessOnOwnAccount.routes
import forms.sections.businessOnOwnAccount.TransferOfRightsFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.businessOnOwnAccount.TransferOfRightsView

class TransferOfRightsViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.transferOfRights"

  val form = new TransferOfRightsFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[TransferOfRightsView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "TransferOfRightsView" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.TransferOfRightsController.onSubmit(NormalMode).url)

    "WhoAreYou is Worker" must {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(TransferOfRightsMessages.Worker.title, Some(TransferOfRightsMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe TransferOfRightsMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe TransferOfRightsMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe TransferOfRightsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe TransferOfRightsMessages.no
      }
    }

    "WhoAreYou is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(TransferOfRightsMessages.Hirer.title, Some(TransferOfRightsMessages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe TransferOfRightsMessages.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe TransferOfRightsMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe TransferOfRightsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe TransferOfRightsMessages.no
      }
    }
  }
}
