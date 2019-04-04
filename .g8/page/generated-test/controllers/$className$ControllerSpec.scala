package controllers

import controllers.actions._
import play.api.test.Helpers._
import views.html.$className$View

class $className$ControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[$className$View]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) = new $className$Controller(
    frontendAppConfig,
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    view = view
  )

  def viewAsString() = view(frontendAppConfig)(fakeRequest, messages).toString

  "$className$ Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }
  }
}




