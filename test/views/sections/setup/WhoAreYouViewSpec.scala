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

package views.sections.setup

import assets.messages.WhoAreYouMessages
import config.featureSwitch.FeatureSwitching
import forms.sections.setup.WhoAreYouFormProvider
import models.NormalMode
import play.api.mvc.Call
import views.behaviours.ViewBehaviours
import views.html.sections.setup.WhoAreYouView

class WhoAreYouViewSpec extends ViewBehaviours with FeatureSwitching {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "whoAreYou"

  val form = new WhoAreYouFormProvider()()(fakeDataRequest)

  val view = injector.instanceOf[WhoAreYouView]

  val postAction = Call("POST", "/foo")

  def createViewWithAgency = () => view(postAction, form, NormalMode, showAgency = true)(fakeRequest, messages, frontendAppConfig)
  def createViewWithoutAgency = () => view(postAction, form, NormalMode, showAgency = false)(fakeRequest, messages, frontendAppConfig)

  "WhatDoYouWantToFindOut view" must {

    behave like normalPage(createViewWithAgency, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createViewWithAgency)

    lazy val document = asDocument(createViewWithAgency())

    "have the correct title" in {
      document.title mustBe title(WhoAreYouMessages.title, Some(WhoAreYouMessages.subHeading))
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe WhoAreYouMessages.heading
    }

    "have the correct subheading" in {
      document.select(Selectors.subheading).text mustBe WhoAreYouMessages.subHeading
    }

    "when showAgency is true" should {

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe WhoAreYouMessages.worker
        document.select(Selectors.multichoice(2)).text mustBe WhoAreYouMessages.hirer
        document.select(Selectors.multichoice(3)).text mustBe WhoAreYouMessages.agency
      }
    }

    "when showAgency is false" should {

      lazy val document = asDocument(createViewWithoutAgency())

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe WhoAreYouMessages.worker
        document.select(Selectors.multichoice(2)).text mustBe WhoAreYouMessages.hirer
        document.select(Selectors.multichoice(3)).isEmpty mustBe true
      }
    }
  }
}
