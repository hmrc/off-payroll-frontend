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
import assets.messages.results.OfficeHolderMessages
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import forms.DeclarationFormProvider
import models.AboutYouAnswer.Worker
import models.UserType.Hirer
import play.api.libs.json.Json
import play.api.mvc.{Call, Request}
import views.ViewSpecBase
import views.html.results.inside.officeHolder.OfficeHolderPAYEView

class OfficeHolderPAYEViewSpec extends ViewSpecBase {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors {
    override val subheading = "p.font-large"
  }

  val form = new DeclarationFormProvider()()

  val view = injector.instanceOf[OfficeHolderPAYEView]

  val postAction = Call(HttpMethods.POST.value, "/")

  def createView(req: Request[_]) = view(form, postAction)(req, messages, frontendAppConfig)

  "The OfficeHolderPAYEView page" should {

    "If the UserType is Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createView(request))

      "Have the correct title" in {
        document.title mustBe title(OfficeHolderMessages.Worker.PAYE.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe OfficeHolderMessages.Worker.PAYE.heading
      }

      "Have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe OfficeHolderMessages.Worker.PAYE.subHeading
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.h2(1)).text mustBe OfficeHolderMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OfficeHolderMessages.Worker.PAYE.whyResult_p1
      }

      "Have the correct Do Next section" in {
        document.select(Selectors.h2(2)).text mustBe OfficeHolderMessages.doNextHeading
        document.select(Selectors.p(2)).text mustBe OfficeHolderMessages.Worker.PAYE.doNext_p1
      }

      "Have the correct Download section" in {
        document.select(Selectors.h2(3)).text mustBe OfficeHolderMessages.downloadHeading
        document.select(Selectors.p(3)).text mustBe OfficeHolderMessages.download_p1
      }
    }

    "If the UserType is Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createView(request))

      "Have the correct title" in {
        document.title mustBe title(OfficeHolderMessages.Hirer.PAYE.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe OfficeHolderMessages.Hirer.PAYE.heading
      }

      "Have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe OfficeHolderMessages.Hirer.PAYE.subHeading
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.h2(1)).text mustBe OfficeHolderMessages.whyResultHeading
        document.select(Selectors.p(1)).text mustBe OfficeHolderMessages.Hirer.PAYE.whyResult_p1
      }

      "Have the correct Do Next section" in {
        document.select(Selectors.h2(2)).text mustBe OfficeHolderMessages.doNextHeading
        document.select(Selectors.p(2)).text mustBe OfficeHolderMessages.Hirer.PAYE.doNext_p1
      }

      "Have the correct Download section" in {
        document.select(Selectors.h2(3)).text mustBe OfficeHolderMessages.downloadHeading
        document.select(Selectors.p(3)).text mustBe OfficeHolderMessages.download_p1
      }
    }

  }
}
