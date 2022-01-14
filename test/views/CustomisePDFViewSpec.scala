/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views

import controllers.routes
import forms.AdditionalPdfDetailsFormProvider
import models.{AdditionalPdfDetails, NormalMode}
import play.api.data.Form
import views.behaviours.QuestionViewBehaviours
import views.html.CustomisePDFView

class CustomisePDFViewSpec extends QuestionViewBehaviours[AdditionalPdfDetails] {

  val messageKeyPrefix = "customisePDF"

  val form = new AdditionalPdfDetailsFormProvider()()

  val view = injector.instanceOf[CustomisePDFView]

  def createView = () => view(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[AdditionalPdfDetails]) => view(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "CustomisePDF view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    behave like pageWithTextFields(
      createViewUsingForm,
      messageKeyPrefix,
      routes.PDFController.onSubmit(NormalMode).url,
      "completedBy", "client", "job", "reference"
    )
  }
}
