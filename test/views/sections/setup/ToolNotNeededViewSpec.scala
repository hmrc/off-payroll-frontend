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

import models.WhichDescribesYouAnswer
import models.WhichDescribesYouAnswer.{ClientIR35, ClientPAYE}
import views.behaviours.ViewBehaviours
import views.html.sections.setup.ToolNotNeededView

class ToolNotNeededViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "toolNotNeeded.client"

  val view = injector.instanceOf[ToolNotNeededView]

  def createView(clientType: WhichDescribesYouAnswer) = () => view(clientType)(fakeRequest, messages, frontendAppConfig)


  "ToolNotNeeded view" when {

    "IR35 user" must {
      behave like normalPage(createView(ClientIR35), messageKeyPrefix + ".ir35", hasSubheading = false)

      behave like pageWithBackLink(createView(ClientIR35))
    }

    "PAYE user" must {
      behave like normalPage(createView(ClientPAYE), messageKeyPrefix + ".paye", hasSubheading = false)

      behave like pageWithBackLink(createView(ClientPAYE))
    }

  }
}
