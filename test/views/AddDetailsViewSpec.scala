/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views

import assets.messages.AddDetailsMessages
import controllers.routes
import forms.AdditionalPdfDetailsFormProvider
import models.{AdditionalPdfDetails, NormalMode}
import play.api.data.Form
import views.behaviours.QuestionViewBehaviours
import views.html.AddDetailsView

class AddDetailsViewSpec extends QuestionViewBehaviours[AdditionalPdfDetails] {

  override def beforeEach(): Unit = {
    super.beforeEach()

  }

  object Selectors extends BaseCSSSelectors{
    val link = "#value > p:nth-child(3) > a"
  }

  val messageKeyPrefix = "addDetails"

  val form = new AdditionalPdfDetailsFormProvider()()

  val view = injector.instanceOf[AddDetailsView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages,frontendAppConfig)

  def createHirerView = () => view(form, NormalMode)(hirerFakeRequest, messages,frontendAppConfig)

  def createViewUsingForm = (form: Form[AdditionalPdfDetails]) => view(form, NormalMode)(workerFakeRequest, messages,frontendAppConfig)

  "AddDetails view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    behave like pageWithTextFields(
      createViewUsingForm,
      messageKeyPrefix,
      routes.PDFController.onSubmit(NormalMode).url,
      "completedBy", "client", "job", "reference"
    )

    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(AddDetailsMessages.title)
    }

    "have the correct heading" in {
      document.title mustBe title(AddDetailsMessages.heading)
    }

    "have the correct first label" in {
      document.select(Selectors.label(3)).text mustBe AddDetailsMessages.fileName
    }

    "have the correct second label" in {
      document.select(Selectors.label(4)).text mustBe AddDetailsMessages.name
    }

    "have the correct third label" in {
      document.select(Selectors.label(5)).text mustBe AddDetailsMessages.clientName
    }

    "have the correct fourth label" in {
      document.select(Selectors.label(6)).text mustBe AddDetailsMessages.role
    }

    "have the correct fifth label" in {
      document.select(Selectors.label(7)).text mustBe AddDetailsMessages.reference
    }

    lazy val documentHirer = asDocument(createHirerView())

    "have the correct third label for the Hirer" in {
      documentHirer.select(Selectors.label(5)).text mustBe AddDetailsMessages.orgName
    }
  }
}
