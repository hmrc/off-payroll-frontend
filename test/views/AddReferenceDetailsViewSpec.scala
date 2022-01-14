/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views

import assets.messages.AddReferenceDetailsMessages
import forms.AddReferenceDetailsFormProvider
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.AddReferenceDetailsView

class AddReferenceDetailsViewSpec extends YesNoViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()

  }

  object Selectors extends BaseCSSSelectors{
    val link = "#value > p:nth-child(3) > a"
  }

  val messageKeyPrefix = "addReferenceDetails"

  val form = new AddReferenceDetailsFormProvider()()

  val view = injector.instanceOf[AddReferenceDetailsView]

  def createView = () => view(form)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form)(req, messages, frontendAppConfig)

  "AddReferenceDetails view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    lazy val document = asDocument(createView())

      "have the correct title" in {
        document.title mustBe title(AddReferenceDetailsMessages.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe AddReferenceDetailsMessages.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe AddReferenceDetailsMessages.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe AddReferenceDetailsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe AddReferenceDetailsMessages.no
    }
  }
}
