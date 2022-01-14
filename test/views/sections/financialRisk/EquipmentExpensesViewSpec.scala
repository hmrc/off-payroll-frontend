/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.sections.financialRisk

import assets.messages.{EquipmentExpensesMessages, SubHeadingMessages}
import controllers.sections.financialRisk.routes
import forms.sections.financialRisk.EquipmentExpensesFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.financialRisk.EquipmentExpensesView

class EquipmentExpensesViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.equipmentExpenses"

  val form = new EquipmentExpensesFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[EquipmentExpensesView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "EquipmentExpensesView" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.EquipmentExpensesController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(EquipmentExpensesMessages.Worker.title, Some(SubHeadingMessages.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe EquipmentExpensesMessages.Worker.heading
      }

      "have the correct text" in {
        document.select(Selectors.p(1)).text mustBe EquipmentExpensesMessages.Worker.p1
        document.select(Selectors.p(2)).text mustBe EquipmentExpensesMessages.Worker.p2
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe EquipmentExpensesMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe EquipmentExpensesMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(EquipmentExpensesMessages.Hirer.title, Some(SubHeadingMessages.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe EquipmentExpensesMessages.Hirer.heading
      }

      "have the correct text" in {
        document.select(Selectors.p(1)).text mustBe EquipmentExpensesMessages.Hirer.p1
        document.select(Selectors.p(2)).text mustBe EquipmentExpensesMessages.Hirer.p2
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe EquipmentExpensesMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe EquipmentExpensesMessages.no
      }
    }
  }
}
