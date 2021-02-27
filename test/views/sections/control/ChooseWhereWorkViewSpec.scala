/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views.sections.control

import assets.messages.{ChooseWhereWorkMessages, SubHeadingMessages}
import config.featureSwitch.FeatureSwitching
import forms.sections.control.ChooseWhereWorkFormProvider
import models.NormalMode
import models.sections.control.ChooseWhereWork
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.control.ChooseWhereWorkView

class ChooseWhereWorkViewSpec extends ViewBehaviours with FeatureSwitching {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.chooseWhereWork"

  val form = new ChooseWhereWorkFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[ChooseWhereWorkView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "ChooseWhereWork view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ChooseWhereWorkMessages.OptimisedWorker.title, Some(SubHeadingMessages.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ChooseWhereWorkMessages.OptimisedWorker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.clientDecides
        document.select(Selectors.multichoice(2)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.workerDecides
        document.select(Selectors.multichoice(3)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.noTaskDeterminate
        document.select(Selectors.multichoice(4)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.partly
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ChooseWhereWorkMessages.OptimisedHirer.title, Some(SubHeadingMessages.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ChooseWhereWorkMessages.OptimisedHirer.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ChooseWhereWorkMessages.OptimisedHirer.clientDecides
        document.select(Selectors.multichoice(2)).text mustBe ChooseWhereWorkMessages.OptimisedHirer.workerDecides
        document.select(Selectors.multichoice(3)).text mustBe ChooseWhereWorkMessages.OptimisedHirer.noTaskDeterminate
        document.select(Selectors.multichoice(4)).text mustBe ChooseWhereWorkMessages.OptimisedHirer.partly
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ChooseWhereWorkMessages.OptimisedWorker.title, Some(SubHeadingMessages.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ChooseWhereWorkMessages.OptimisedWorker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.clientDecides
        document.select(Selectors.multichoice(2)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.workerDecides
        document.select(Selectors.multichoice(3)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.noTaskDeterminate
        document.select(Selectors.multichoice(4)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.partly
      }
    }
  }

  "ChooseWhereWork view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- ChooseWhereWork.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, isChecked = false)
        }
      }
    }

    for(option <- ChooseWhereWork.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, isChecked = true)

          for(unselectedOption <- ChooseWhereWork.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, isChecked = false)
          }
        }
      }
    }
  }
}
