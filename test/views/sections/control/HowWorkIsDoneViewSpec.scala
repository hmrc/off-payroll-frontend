/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views.sections.control

import assets.messages.{HowWorkIsDoneMessages, SubHeadingMessages}
import forms.sections.control.HowWorkIsDoneFormProvider
import models.NormalMode
import models.sections.control.HowWorkIsDone
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.control.HowWorkIsDoneView

class HowWorkIsDoneViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.howWorkIsDone"

  val form = new HowWorkIsDoneFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[HowWorkIsDoneView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "HowWorkIsDone view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(HowWorkIsDoneMessages.OptimisedWorker.title, Some(SubHeadingMessages.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowWorkIsDoneMessages.OptimisedWorker.heading
      }

      "have the correct content" in {
        document.select(Selectors.p(1)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.p1
        document.select(Selectors.p(2)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.p2
        document.select(Selectors.p(3)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.p3
        document.select("#employmentStatusManualLink").attr("href") mustBe frontendAppConfig.employmentStatusManualESM0527Url
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.yesClientDecides
        document.select(Selectors.multichoice(2)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.noWorkerDecides
        document.select(Selectors.multichoice(3)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.partly
        document.select(Selectors.multichoice(4)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.noSkilledRole

      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(HowWorkIsDoneMessages.OptimisedHirer.title, Some(SubHeadingMessages.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowWorkIsDoneMessages.OptimisedHirer.heading
      }

      "have the correct content" in {
        document.select(Selectors.p(1)).text mustBe HowWorkIsDoneMessages.OptimisedHirer.p1
        document.select(Selectors.p(2)).text mustBe HowWorkIsDoneMessages.OptimisedHirer.p2
        document.select(Selectors.p(3)).text mustBe HowWorkIsDoneMessages.OptimisedHirer.p3
        document.select("#employmentStatusManualLink").attr("href") mustBe frontendAppConfig.employmentStatusManualESM0527Url
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowWorkIsDoneMessages.OptimisedHirer.yesClientDecides
        document.select(Selectors.multichoice(2)).text mustBe HowWorkIsDoneMessages.OptimisedHirer.noWorkerDecides
        document.select(Selectors.multichoice(3)).text mustBe HowWorkIsDoneMessages.OptimisedHirer.partly
        document.select(Selectors.multichoice(4)).text mustBe HowWorkIsDoneMessages.OptimisedHirer.noSkilledRole

      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(HowWorkIsDoneMessages.OptimisedWorker.title, Some(SubHeadingMessages.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowWorkIsDoneMessages.OptimisedWorker.heading
      }

      "have the correct content" in {
        document.select(Selectors.p(1)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.p1
        document.select(Selectors.p(2)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.p2
        document.select(Selectors.p(3)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.p3
        document.select("#employmentStatusManualLink").attr("href") mustBe frontendAppConfig.employmentStatusManualESM0527Url
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.yesClientDecides
        document.select(Selectors.multichoice(2)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.noWorkerDecides
        document.select(Selectors.multichoice(3)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.partly
        document.select(Selectors.multichoice(4)).text mustBe HowWorkIsDoneMessages.OptimisedWorker.noSkilledRole

      }
    }
  }

  "HowWorkIsDone view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- HowWorkIsDone.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, isChecked = false)
        }
      }
    }

    for(option <- HowWorkIsDone.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, isChecked = true)

          for(unselectedOption <- HowWorkIsDone.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, isChecked = false)
          }
        }
      }
    }
  }
}
