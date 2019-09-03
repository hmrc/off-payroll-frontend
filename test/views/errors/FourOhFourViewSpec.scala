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

package views.errors

import assets.messages.AddDetailsMessages
import views.behaviours.ViewBehaviours
import views.html.errors.FourOhFourView

class FourOhFourViewSpec extends ViewBehaviours {

  val view = injector.instanceOf[FourOhFourView]

  object Selectors extends BaseCSSSelectors{
    val link = "#value > p:nth-child(3) > a"
  }

  def createView = () => view(frontendAppConfig)(fakeRequest, messages)

  "FourOhFour view" must {

    behave like normalPage(createView, "fourOhFour", hasSubheading = false)

    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(AddDetailsMessages.title)
    }

    "have the correct heading" in {
      document.title mustBe title(AddDetailsMessages.heading)
    }


    "have the correct start again href" in {
      document.select(Selectors.link).attr("href") mustBe "/check-employment-status-for-tax/redirect-to-disclaimer"
    }
  }


}
