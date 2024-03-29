/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.sections.businessOnOwnAccount

import assets.messages.OwnershipRightsMessages
import controllers.sections.businessOnOwnAccount.routes
import forms.sections.businessOnOwnAccount.OwnershipRightsFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.businessOnOwnAccount.OwnershipRightsView

class OwnershipRIghtsViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.ownershipRights"

  val form = new OwnershipRightsFormProvider()()

  val view = injector.instanceOf[OwnershipRightsView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "OwnershipRightsView" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.OwnershipRightsController.onSubmit(NormalMode).url)

    "the WhoAreYou is Worker" must {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(OwnershipRightsMessages.Worker.title, Some(OwnershipRightsMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe OwnershipRightsMessages.Worker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe OwnershipRightsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe OwnershipRightsMessages.no
      }
    }

    "the WhoAreYou is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(OwnershipRightsMessages.Hirer.title, Some(OwnershipRightsMessages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe OwnershipRightsMessages.Hirer.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe OwnershipRightsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe OwnershipRightsMessages.no
      }
    }
  }
}
