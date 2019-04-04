package views

import play.api.data.Form
import controllers.routes
import forms.$className$FormProvider
import models.NormalMode
import views.behaviours.IntViewBehaviours
import views.html.$className$View

class $className$ViewSpec extends IntViewBehaviours {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className$FormProvider()()

  val view = injector.instanceOf[$className$View]

  def createView = () => view(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => view(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "$className$ view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like intPage(createViewUsingForm, messageKeyPrefix, routes.$className$Controller.onSubmit(NormalMode).url)
  }
}
