/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views

import assets.messages.ResetAnswersMessages
import controllers.routes
import forms.ResetAnswersWarningFormProvider
import play.api.data.Form
import views.behaviours.{ViewBehaviours, YesNoViewBehaviours}
import views.html.ResetAnswersWarningView

class ResetAnswersWarningViewSpec extends YesNoViewBehaviours with ViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()

  }

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "resetAnswersWarning"

  val view = injector.instanceOf[ResetAnswersWarningView]

  val form = new ResetAnswersWarningFormProvider()()

  def createView = () => view(form)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form)(fakeRequest, messages, frontendAppConfig)

  lazy val document = asDocument(createView())

  "CheckYourAnswers view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.ResetAnswersWarningController.onSubmit().url)

    "have the correct title" in {

      document.title mustBe title(ResetAnswersMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe ResetAnswersMessages.heading
    }

    "have the correct hint" in {
      document.select(Selectors.panel).text() mustBe ResetAnswersMessages.hint
    }
  }
}
