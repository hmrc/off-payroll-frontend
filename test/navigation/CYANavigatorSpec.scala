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

package navigation

import base.GuiceAppSpecBase
import config.featureSwitch.OptimisedFlow
import controllers.routes
import models._
import pages._

class CYANavigatorSpec extends GuiceAppSpecBase {

  val emptyUserAnswers = UserAnswers("id")
  val navigator = new CYANavigator

  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers) = navigator.nextPage(fromPage)(userAnswers)

  "CYANavigator" when {

    "go from the CheckYourAnswersPage to the ResultPage" in {

      enable(OptimisedFlow)
      nextPage(CheckYourAnswersPage) mustBe routes.ResultController.onPageLoad()
    }

    "go from the ResultPage" when {

      "optimised flow is enabled" when {

        "and the ResultPage has the answer true, go to the AddReferenceDetailsPage" in {

          enable(OptimisedFlow)
          lazy val userAnswers = UserAnswers("id")
            .set(ResultPage, true)

          nextPage(ResultPage, userAnswers) mustBe routes.AddReferenceDetailsController.onPageLoad()
        }

        "and the ResultPage does NOT have the answer true, go to the AddReferenceDetailsPage" in {

          enable(OptimisedFlow)
          lazy val userAnswers = UserAnswers("id")
            .set(ResultPage, false)

          nextPage(ResultPage, userAnswers) mustBe routes.PrintPreviewController.onPageLoad()
        }
      }

      "optimised flow is disabled go to the PDFPage" in {

        disable(OptimisedFlow)
        nextPage(ResultPage) mustBe routes.PDFController.onPageLoad(NormalMode)
      }
    }

    "go from the AddReferenceDetailsPage" when {

      "AddReferenceDetailsPage answer is true go to the PDFPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(AddReferenceDetailsPage, true)

        nextPage(AddReferenceDetailsPage, userAnswers) mustBe routes.PDFController.onPageLoad(NormalMode)
      }

      "AddReferenceDetailsPage answer is false go to the FinishedCheckingPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(AddReferenceDetailsPage, false)

        nextPage(AddReferenceDetailsPage, userAnswers) mustBe routes.PrintPreviewController.onPageLoad()
      }
    }

    "go from the CustomisePDFPage to the FinishedCheckingPage" in {

      enable(OptimisedFlow)
      nextPage(CustomisePDFPage) mustBe routes.PrintPreviewController.onPageLoad()
    }
  }
}