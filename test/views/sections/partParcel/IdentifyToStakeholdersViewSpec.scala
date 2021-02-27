/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views.sections.partParcel

import assets.messages.{IdentifyToStakeholdersMessages, SubHeadingMessages}
import forms.sections.partAndParcel.IdentifyToStakeholdersFormProvider
import models.NormalMode
import models.sections.partAndParcel.IdentifyToStakeholders
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.partParcel.IdentifyToStakeholdersView

class IdentifyToStakeholdersViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.identifyToStakeholders"

  val form = new IdentifyToStakeholdersFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[IdentifyToStakeholdersView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "IdentifyToStakeholders view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.Worker.title, Some(SubHeadingMessages.partAndParcel))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe IdentifyToStakeholdersMessages.Worker.heading
      }

      "have the correct radio options" in {
        document.select(Selectors.multichoice(1)).text mustBe IdentifyToStakeholdersMessages.Worker.workForEndClient
        document.select(Selectors.multichoice(2)).text mustBe IdentifyToStakeholdersMessages.Worker.workAsIndependent
        document.select(Selectors.multichoice(3)).text mustBe IdentifyToStakeholdersMessages.Worker.workAsBusiness
        document.select(Selectors.multichoice(3)).text mustBe IdentifyToStakeholdersMessages.Worker.workAsBusiness
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.Hirer.title, Some(SubHeadingMessages.partAndParcel))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe IdentifyToStakeholdersMessages.Hirer.heading
      }

      "have the correct radio options" in {
        document.select(Selectors.multichoice(1)).text mustBe IdentifyToStakeholdersMessages.Hirer.workForEndClient
        document.select(Selectors.multichoice(2)).text mustBe IdentifyToStakeholdersMessages.Hirer.workAsIndependent
        document.select(Selectors.multichoice(3)).text mustBe IdentifyToStakeholdersMessages.Hirer.workAsBusiness
        document.select(Selectors.multichoice(4)).text mustBe IdentifyToStakeholdersMessages.Hirer.wouldNotHappen
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.Worker.title, Some(SubHeadingMessages.partAndParcel))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe IdentifyToStakeholdersMessages.Worker.heading
      }

      "have the correct radio options" in {
        document.select(Selectors.multichoice(1)).text mustBe IdentifyToStakeholdersMessages.Worker.workForEndClient
        document.select(Selectors.multichoice(2)).text mustBe IdentifyToStakeholdersMessages.Worker.workAsIndependent
        document.select(Selectors.multichoice(3)).text mustBe IdentifyToStakeholdersMessages.Worker.workAsBusiness
        document.select(Selectors.multichoice(4)).text mustBe IdentifyToStakeholdersMessages.Worker.wouldNotHappen
      }
    }
  }

  "IdentifyToStakeholders view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- IdentifyToStakeholders.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, isChecked = false)
        }
      }
    }


    for(option <- IdentifyToStakeholders.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, isChecked = true)

          for(unselectedOption <- IdentifyToStakeholders.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, isChecked = false)
          }
        }
      }
    }
  }
}
