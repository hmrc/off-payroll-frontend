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

import assets.messages.results.OfficeHolderMessages
import config.SessionKeys
import models.AboutYouAnswer.Worker
import models.UserAnswers
import models.UserType.Hirer
import models.requests.DataRequest
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import views.html.results.inside.officeHolder.OfficeHolderIR35View

class OfficeHolderIR35ViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[OfficeHolderIR35View]

  def createView(req: DataRequest[_], isPrivateSector: Boolean = false): HtmlFormat.Appendable =
    view( postAction, isPrivateSector)(req, messages, frontendAppConfig, testNoPdfResultDetails)

  "The OfficeHolderIR35View page" should {

    "If the UserType is Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))
      lazy val document = asDocument(createView(dataRequest))

      "Have the correct title" in {
        document.title mustBe title(OfficeHolderMessages.Worker.IR35.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe OfficeHolderMessages.Worker.IR35.heading
      }

      "Have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe OfficeHolderMessages.Worker.IR35.subHeading
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.WhyResult.h2(1)).text mustBe OfficeHolderMessages.whyResultHeading
        document.select(Selectors.WhyResult.p(1)).text mustBe OfficeHolderMessages.Worker.IR35.whyResult_p1
      }

      "For a Public Sector contract" should {

        "Have the correct Do Next section which" in {
          document.select(Selectors.DoNext.h2(1)).text mustBe OfficeHolderMessages.doNextHeading
          document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Worker.IR35.doNext_public_p1
        }
      }

      "For a Private Sector contract" should {

        lazy val document = asDocument(createView(dataRequest, isPrivateSector = true))

        "Have the correct Do Next section which" in {
          document.select(Selectors.DoNext.h2(1)).text mustBe OfficeHolderMessages.doNextHeading
          document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Worker.IR35.doNext_private_p1
        }
      }

      "Have the correct Download section" in {
        document.select(Selectors.Download.h2(1)).text mustBe OfficeHolderMessages.downloadHeading
        document.select(Selectors.Download.p(1)).text mustBe OfficeHolderMessages.download_p1
      }
    }

    "If the UserType is Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))
      lazy val document = asDocument(createView(dataRequest))

      "Have the correct title" in {
        document.title mustBe title(OfficeHolderMessages.Hirer.IR35.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe OfficeHolderMessages.Hirer.IR35.heading
      }

      "Have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe OfficeHolderMessages.Hirer.IR35.subHeading
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.WhyResult.h2(1)).text mustBe OfficeHolderMessages.whyResultHeading
        document.select(Selectors.WhyResult.p(1)).text mustBe OfficeHolderMessages.Hirer.IR35.whyResult_p1
      }

      "For a Public Sector contract" should {

        "Have the correct Do Next section which" in {
          document.select(Selectors.DoNext.h2(1)).text mustBe OfficeHolderMessages.doNextHeading
          document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Hirer.IR35.doNext_public_p1
          document.select(Selectors.DoNext.p(2)).text mustBe OfficeHolderMessages.Hirer.IR35.doNext_public_p2
        }
      }

      "For a Private Sector contract" should {

        lazy val document = asDocument(createView(dataRequest, isPrivateSector = true))

        "Have the correct Do Next section which" in {
          document.select(Selectors.DoNext.h2(1)).text mustBe OfficeHolderMessages.doNextHeading
          document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Hirer.IR35.doNext_private_p1
          document.select(Selectors.DoNext.p(2)).text mustBe OfficeHolderMessages.Hirer.IR35.doNext_private_p2
        }
      }

      "Have the correct Download section" in {
        document.select(Selectors.Download.h2(1)).text mustBe OfficeHolderMessages.downloadHeading
        document.select(Selectors.Download.p(1)).text mustBe OfficeHolderMessages.download_p1
      }
    }
  }
}
