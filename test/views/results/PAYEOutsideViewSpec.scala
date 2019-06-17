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
import assets.messages.results.OutDecisionMessages
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import forms.DeclarationFormProvider
import models.UserType.{Hirer, Worker}
import play.api.libs.json.Json
import play.api.mvc.{Call, Request}
import views.ViewSpecBase
import views.html.results.PAYEOutView

class PAYEOutsideViewSpec extends ViewSpecBase {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors {
    override val subheading = "p.font-large"
  }

  val form = new DeclarationFormProvider()()

  val view = injector.instanceOf[PAYEOutView]

  val postAction = Call(HttpMethods.POST.value, "/")


  "The PAYEOutsideView page" should {

    "The UserType is a Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      def createView(req: Request[_]) = view(form, postAction,true,false,false)(req, messages, frontendAppConfig)
      lazy val document = asDocument(createView(request))

      "Have the correct title" in {
        document.title mustBe title(OutDecisionMessages.WorkerPAYE.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe OutDecisionMessages.WorkerPAYE.heading
      }

      "Have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe OutDecisionMessages.WorkerPAYE.subHeading
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP1
        document.select(Selectors.p(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP2
      }

      "Have the correct Do Next section" in {
        document.select(Selectors.h2(2)).text mustBe OutDecisionMessages.doNextHeading
        document.select(Selectors.p(3)).text mustBe OutDecisionMessages.WorkerPAYE.doNext
      }

      "Have the correct Download section" in {
        document.select(Selectors.h2(3)).text mustBe OutDecisionMessages.downloadHeading
        document.select(Selectors.p(4)).text mustBe OutDecisionMessages.download_p1
      }

      "Have the correct Why Result section for 1 reason" in {
        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP1
        document.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB1
        document.select(Selectors.p(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP2
      }

      "Have the correct Why Result section for 2 reasons" in {
        def createView2(req: Request[_]) = view(form, postAction,true,true,false)(req, messages, frontendAppConfig)
        lazy val document2 = asDocument(createView2(request))

        document2.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document2.select(Selectors.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP1
        document2.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB1
        document2.select(Selectors.bullet(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB2
        document2.select(Selectors.p(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP2
      }

      "Have the correct Why Result section for 3 reasons" in {
        def createView3(req: Request[_]) = view(form, postAction,true,true,true)(req, messages, frontendAppConfig)
        lazy val document3 = asDocument(createView3(request))

        document3.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document3.select(Selectors.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP1
        document3.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB1
        document3.select(Selectors.bullet(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB2
        document3.select(Selectors.bullet(3)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB3
        document3.select(Selectors.p(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP2
      }
    }

    "The UserType is a Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      def createView(req: Request[_]) = view(form, postAction,true,false,false)(req, messages, frontendAppConfig)
      lazy val document = asDocument(createView(request))

      "Have the correct title" in {
        document.title mustBe title(OutDecisionMessages.HirerPAYE.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe OutDecisionMessages.HirerPAYE.heading
      }

      "Have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe OutDecisionMessages.HirerPAYE.subHeading
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP1
      }

      "Have the correct Do Next section" in {
        document.select(Selectors.h2(2)).text mustBe OutDecisionMessages.doNextHeading
        document.select(Selectors.p(3)).text mustBe OutDecisionMessages.HirerPAYE.doNext
      }

      "Have the correct Download section" in {
        document.select(Selectors.h2(3)).text mustBe OutDecisionMessages.downloadHeading
        document.select(Selectors.p(4)).text mustBe OutDecisionMessages.download_p1
      }

      "Have the correct Why Result section for 1 reason" in {
        document.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP1
        document.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB1
        document.select(Selectors.p(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP2
      }

      "Have the correct Why Result section for 2 reasons" in {
        def createView2(req: Request[_]) = view(form, postAction,true,true,false)(req, messages, frontendAppConfig)
        lazy val document2 = asDocument(createView2(request))

        document2.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document2.select(Selectors.p(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP1
        document2.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB1
        document2.select(Selectors.bullet(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB2
        document2.select(Selectors.p(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP2
      }

      "Have the correct Why Result section for 3 reasons" in {
        def createView3(req: Request[_]) = view(form, postAction,true,true,true)(req, messages, frontendAppConfig)
        lazy val document3 = asDocument(createView3(request))

        document3.select(Selectors.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
        document3.select(Selectors.p(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP1
        document3.select(Selectors.bullet(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB1
        document3.select(Selectors.bullet(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB2
        document3.select(Selectors.bullet(3)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB3
        document3.select(Selectors.p(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP2
      }
    }
  }
}
