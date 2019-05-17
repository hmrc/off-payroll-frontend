package views

import play.api.data.Form
import forms.BusinessSizeFormProvider
import models.NormalMode
import models.BusinessSize
import views.behaviours.ViewBehaviours
import views.html.BusinessSizeView

class BusinessSizeViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "businessSize"

  val form = new BusinessSizeFormProvider()()

  val view = injector.instanceOf[BusinessSizeView]

  def createView = () => view(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => view(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "BusinessSize view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)
  }

  "BusinessSize view" when {
    "rendered" must {
      "contain checkboxes for the values" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- BusinessSize.options) {
          assertContainsRadioButton(doc, option.id, "businessSize[]", option.value, false)
        }
      }
    }

    for(option <- BusinessSize.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' checkbox selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("businessSize[]" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "businessSize[]", option.value, true)

          for(unselectedOption <- BusinessSize.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "businessSize[]", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
