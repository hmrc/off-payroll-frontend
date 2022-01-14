/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.sections.financialRisk

import assets.messages.{OtherExpensesMessages, SubHeadingMessages}
import controllers.sections.financialRisk.routes
import forms.sections.financialRisk.OtherExpensesFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.financialRisk.OtherExpensesView

class OtherExpensesViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.otherExpenses"

  val form = new OtherExpensesFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[OtherExpensesView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "OtherExpensesView" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.OtherExpensesController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(OtherExpensesMessages.Worker.title, Some(SubHeadingMessages.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe OtherExpensesMessages.Worker.heading
      }

      "have the correct text" in {
        document.select(Selectors.p(1)).text mustBe OtherExpensesMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe OtherExpensesMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe OtherExpensesMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(OtherExpensesMessages.Hirer.title, Some(SubHeadingMessages.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe OtherExpensesMessages.Hirer.heading
      }

      "have the correct text" in {
        document.select(Selectors.p(1)).text mustBe OtherExpensesMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe OtherExpensesMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe OtherExpensesMessages.no
      }
    }
  }
}
