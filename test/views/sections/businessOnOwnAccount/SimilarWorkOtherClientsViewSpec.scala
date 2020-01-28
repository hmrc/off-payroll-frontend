/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.sections.businessOnOwnAccount

import assets.messages.SimilarWorkOtherClientsMessages
import controllers.sections.businessOnOwnAccount.routes
import forms.sections.businessOnOwnAccount.SimilarWorkOtherClientsFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.businessOnOwnAccount.SimilarWorkOtherClientsView

class SimilarWorkOtherClientsViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.similarWorkOtherClients"

  val form = new SimilarWorkOtherClientsFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[SimilarWorkOtherClientsView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "SimilarWorkOtherClientsView" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.SimilarWorkOtherClientsController.onSubmit(NormalMode).url)

    "the WhoAreYou is Worker" must {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(SimilarWorkOtherClientsMessages.Worker.title, Some(SimilarWorkOtherClientsMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe SimilarWorkOtherClientsMessages.Worker.heading
      }

      "have the correct content" in {
        document.select(Selectors.p(1)).text mustBe SimilarWorkOtherClientsMessages.Worker.p1
        document.select(Selectors.p(2)).text mustBe SimilarWorkOtherClientsMessages.Worker.p2

      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe SimilarWorkOtherClientsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe SimilarWorkOtherClientsMessages.no
      }
    }

    "the WhoAreYou is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(SimilarWorkOtherClientsMessages.Hirer.title, Some(SimilarWorkOtherClientsMessages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe SimilarWorkOtherClientsMessages.Hirer.heading
      }

      "have the correct content" in {
        document.select(Selectors.p(1)).text mustBe SimilarWorkOtherClientsMessages.Hirer.p1
        document.select(Selectors.p(2)).text mustBe SimilarWorkOtherClientsMessages.Hirer.p2

      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe SimilarWorkOtherClientsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe SimilarWorkOtherClientsMessages.no
      }
    }
  }
}
