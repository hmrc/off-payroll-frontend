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

package views.results

import akka.http.scaladsl.model.HttpMethods
import assets.messages.results.{InDecisionMessages, UndeterminedDecisionMessages}
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import forms.DeclarationFormProvider
import models.UserType.Hirer
import play.api.libs.json.Json
import play.api.mvc.{Call, Request}
import views.ViewSpecBase
import views.html.results.HirerIR35UndeterminedView

class HirerIR35UndeterminedViewSpec extends ViewSpecBase {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors {
    override val subheading = "p.font-large"
  }

  val form = new DeclarationFormProvider()()

  "The Hirer IR35 Undetermined page" should {

    val view = injector.instanceOf[HirerIR35UndeterminedView]
    val postAction = Call(HttpMethods.POST.value, "/")
    lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
    def createPublicView(req: Request[_],public: Boolean) = view(form, postAction, !public)(req, messages, frontendAppConfig)
    def document(public: Boolean = true) = asDocument(createPublicView(request,public))

    "Have the correct title" in {
      document().title mustBe title(UndeterminedDecisionMessages.HirerIR35.title)
    }

    "Have the correct heading" in {
      document().select(Selectors.heading).text mustBe UndeterminedDecisionMessages.HirerIR35.heading
    }

    "Have the correct subheading" in {
      document().select(Selectors.subheading).text mustBe UndeterminedDecisionMessages.HirerIR35.subHeading
    }

    "Have the correct Why Result section" in {
      document().select(Selectors.h2(1)).text mustBe InDecisionMessages.whyResultHeading
      document().select(Selectors.p(1)).text mustBe UndeterminedDecisionMessages.HirerIR35.whyResult
    }

    "Have the correct Do Next public section" in {
      document().select(Selectors.h2(2)).text mustBe InDecisionMessages.doNextHeading
      document().select(Selectors.p(2)).text mustBe UndeterminedDecisionMessages.HirerIR35.doNextPublic
    }

    "Have the correct Do Next private section" in {
      document().select(Selectors.h2(2)).text mustBe InDecisionMessages.doNextHeading
      document(public = false).select(Selectors.p(2)).text mustBe UndeterminedDecisionMessages.HirerIR35.doNextPrivate
    }

  }
}
