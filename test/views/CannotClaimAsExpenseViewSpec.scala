/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package views

import forms.CannotClaimAsExpenseFormProvider
import models.{CannotClaimAsExpense, NormalMode}
import play.api.data.Form
import views.behaviours.ViewBehaviours
import views.html.CannotClaimAsExpenseView

class CannotClaimAsExpenseViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "cannotClaimAsExpense"

  val form = new CannotClaimAsExpenseFormProvider()()

  val view = injector.instanceOf[CannotClaimAsExpenseView]

  def createView = () => view(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => view(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "CannotClaimAsExpense view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)
  }

  "CannotClaimAsExpense view" when {
    "rendered" must {
      "contain checkboxes for the values" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- CannotClaimAsExpense.options) {
          assertContainsRadioButton(doc, option.id, "cannotClaimAsExpense[]", option.value, false)
        }
      }
    }

    for(option <- CannotClaimAsExpense.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' checkbox selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("cannotClaimAsExpense[]" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "cannotClaimAsExpense[]", option.value, true)

          for(unselectedOption <- CannotClaimAsExpense.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "cannotClaimAsExpense[]", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
