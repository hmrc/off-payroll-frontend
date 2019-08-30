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

import assets.messages.{BusinessOnOwnAccountSectionChangeWarningMessages, PersonalServiceSectionChangeWarningMessages}
import play.api.mvc.Call
import views.behaviours.ViewBehaviours
import views.html.{BusinessOnOwnAccountSectionChangeWarningView, PersonalServiceSectionChangeWarningView}

class BusinessOnOwnAccountSectionChangeWarningSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "businessOnOwnAccountSectionSectionChangeWarning"

  val view = injector.instanceOf[BusinessOnOwnAccountSectionChangeWarningView]

  def continueCall = Call("GET", "/foo")

  def createView = () => view(continueCall)(fakeRequest, messages, frontendAppConfig)

  "BusinessOnOwnAccountSectionChangeWarning view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(BusinessOnOwnAccountSectionChangeWarningMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe BusinessOnOwnAccountSectionChangeWarningMessages.heading
    }
  }
}
