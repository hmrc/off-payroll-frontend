/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views.sections.setup

import assets.messages.WhatDoYouWantToDoMessages
import controllers.sections.setup.routes
import forms.sections.setup.WhatDoYouWantToDoFormProvider
import models.NormalMode
import models.sections.setup.WhatDoYouWantToDo
import play.api.data.Form
import views.behaviours.QuestionViewBehaviours
import views.html.sections.setup.WhatDoYouWantToDoView


class WhatDoYouWantToDoViewSpec extends QuestionViewBehaviours[WhatDoYouWantToDo] {

  object Selectors extends BaseCSSSelectors {
    val exit = "#finish-link"
  }

  val messageKeyPrefix = "whatDoYouWantToDo"

  val form = new WhatDoYouWantToDoFormProvider()()

  val view = injector.instanceOf[WhatDoYouWantToDoView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithUser = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  "WhichDescribesYou view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    behave like pageWithTextFields(createViewUsingForm, messageKeyPrefix, routes.WhatDoYouWantToDoController.onSubmit(NormalMode).url)

    "when not given a user type" must {

      lazy val document = asDocument(createView())

      "have the correct title" in {
        document.title mustBe title(WhatDoYouWantToDoMessages.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe WhatDoYouWantToDoMessages.heading
      }

      "have the correct first multi choice answer" in {
        document.select(Selectors.multichoice(1)).text mustBe WhatDoYouWantToDoMessages.newDetermination
      }

      "have the correct second multi choice answer" in {
        document.select(Selectors.multichoice(2)).text mustBe WhatDoYouWantToDoMessages.checkDetermination
      }
    }

    "when given a user type" must {

      lazy val document = asDocument(createViewWithUser())

      "have the correct title" in {
        document.title mustBe title(WhatDoYouWantToDoMessages.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe WhatDoYouWantToDoMessages.heading
      }

      "have the correct first multi choice answer" in {
        document.select(Selectors.multichoice(1)).text mustBe WhatDoYouWantToDoMessages.newDetermination
      }

      "have the correct second multi choice answer" in {
        document.select(Selectors.multichoice(2)).text mustBe WhatDoYouWantToDoMessages.checkDetermination
      }
    }

  }
}
