package views

import play.api.data.Form
import forms.$className$FormProvider
import models.NormalMode
import models.$className$
import views.behaviours.ViewBehaviours
import views.html.$className$View

class $className$ViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className$FormProvider()()

  val view = injector.instanceOf[$className$View]

  def createView = () => view(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => view(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "$className$ view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)
  }

  "$className$ view" when {
    "rendered" must {
      "contain checkboxes for the values" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- $className$.options) {
          assertContainsRadioButton(doc, option.id, "$checkboxKey$[]", option.value, false)
        }
      }
    }

    for(option <- $className$.options) {
      s"rendered with a value of '\${option.value}'" must {
        s"have the '\${option.value}' checkbox selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("$checkboxKey$[]" -> s"\${option.value}"))))
          assertContainsRadioButton(doc, option.id, "$checkboxKey$[]", option.value, true)

          for(unselectedOption <- $className$.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "$checkboxKey$[]", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
