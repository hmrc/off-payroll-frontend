/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package navigation

import base.GuiceAppSpecBase
import controllers.routes
import models._
import pages._

class CYANavigatorSpec extends GuiceAppSpecBase {

  val emptyUserAnswers = UserAnswers("id")
  val navigator = new CYANavigator

  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers) = navigator.nextPage(fromPage)(userAnswers)

  "CYANavigator" when {

    "go from the CheckYourAnswersPage to the ResultPage" in {


      nextPage(CheckYourAnswersPage) mustBe routes.ResultController.onPageLoad()
    }

    "go from the ResultPage" when {

      "normal flow is enabled" when {

        "and the ResultPage has the answer true, go to the AddReferenceDetailsPage" in {


          lazy val userAnswers = UserAnswers("id")
            .set(ResultPage, true)

          nextPage(ResultPage, userAnswers) mustBe routes.AddReferenceDetailsController.onPageLoad()
        }

        "and the ResultPage does NOT have the answer true, go to the AddReferenceDetailsPage" in {


          lazy val userAnswers = UserAnswers("id")
            .set(ResultPage, false)

          nextPage(ResultPage, userAnswers) mustBe routes.PrintPreviewController.onPageLoad()
        }
      }
    }

    "go from the AddReferenceDetailsPage" when {

      "AddReferenceDetailsPage answer is true go to the PDFPage" in {


        lazy val userAnswers = UserAnswers("id")
          .set(AddReferenceDetailsPage, true)

        nextPage(AddReferenceDetailsPage, userAnswers) mustBe routes.PDFController.onPageLoad(NormalMode)
      }

      "AddReferenceDetailsPage answer is false go to the FinishedCheckingPage" in {


        lazy val userAnswers = UserAnswers("id")
          .set(AddReferenceDetailsPage, false)

        nextPage(AddReferenceDetailsPage, userAnswers) mustBe routes.PrintPreviewController.onPageLoad()
      }
    }

    "go from the CustomisePDFPage to the FinishedCheckingPage" in {


      nextPage(CustomisePDFPage) mustBe routes.PrintPreviewController.onPageLoad()
    }
  }
}