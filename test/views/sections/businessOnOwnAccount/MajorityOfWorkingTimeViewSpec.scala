/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.sections.businessOnOwnAccount

import assets.messages.MajorityOfWorkingTimeMessages
import controllers.sections.businessOnOwnAccount.routes
import forms.sections.businessOnOwnAccount.MajorityOfWorkingTimeFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.businessOnOwnAccount.MajorityOfWorkingTimeView

class MajorityOfWorkingTimeViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.majorityOfWorkingTime"

  val form = new MajorityOfWorkingTimeFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[MajorityOfWorkingTimeView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "MajorityOfWorkingTimeView" when {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.MajorityOfWorkingTimeController.onSubmit(NormalMode).url)

    "the WhoAreYou is Worker" must {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(MajorityOfWorkingTimeMessages.Worker.title, Some(MajorityOfWorkingTimeMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe MajorityOfWorkingTimeMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe MajorityOfWorkingTimeMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe MajorityOfWorkingTimeMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe MajorityOfWorkingTimeMessages.no
      }
    }

    "the WhoAreYou is Hirer" must {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(MajorityOfWorkingTimeMessages.Hirer.title, Some(MajorityOfWorkingTimeMessages.Hirer.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe MajorityOfWorkingTimeMessages.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe MajorityOfWorkingTimeMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe MajorityOfWorkingTimeMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe MajorityOfWorkingTimeMessages.no
      }
    }
  }
}
