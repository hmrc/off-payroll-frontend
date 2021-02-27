/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views.sections.businessOnOwnAccount

import assets.messages.FollowOnContractMessages
import controllers.sections.businessOnOwnAccount.routes
import forms.sections.businessOnOwnAccount.FollowOnContractFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.businessOnOwnAccount.FollowOnContractView

class FollowOnContractViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.followOnContract"

  val form = new FollowOnContractFormProvider()()

  val view = injector.instanceOf[FollowOnContractView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "FollowOnContractView" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.FollowOnContractController.onSubmit(NormalMode).url)

    "the WhoAreYou is Worker" must {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(FollowOnContractMessages.Worker.title, Some(FollowOnContractMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe FollowOnContractMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe FollowOnContractMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe FollowOnContractMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe FollowOnContractMessages.no
      }
    }

    "the WhoAreYou is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(FollowOnContractMessages.Hirer.title, Some(FollowOnContractMessages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe FollowOnContractMessages.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe FollowOnContractMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe FollowOnContractMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe FollowOnContractMessages.no
      }
    }
  }
}
