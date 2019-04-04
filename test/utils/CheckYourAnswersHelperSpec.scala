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

package utils

import base.SpecBase
import controllers.routes
import models.AboutYouAnswer.Worker
import models.CannotClaimAsExpense.WorkerUsedVehicle
import models.WorkerType.LimitedCompany
import models.{CheckMode, Enumerable, UserAnswers}
import pages._
import viewmodels.AnswerRow

class CheckYourAnswersHelperSpec extends SpecBase with Enumerable.Implicits {

  ".cannotClaimAsExpense" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).cannotClaimAsExpense mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "Return correctly formatted answer row" in {
        val cacheMap = UserAnswers("id").set(CannotClaimAsExpensePage, Seq(WorkerUsedVehicle))
        new CheckYourAnswersHelper(cacheMap).cannotClaimAsExpense mustBe
          Some(AnswerRow(
            label = "cannotClaimAsExpense.checkYourAnswersLabel",
            answer = Seq(s"cannotClaimAsExpense.$WorkerUsedVehicle"),
            answerIsMessageKey = true,
            changeUrl = routes.CannotClaimAsExpenseController.onPageLoad(CheckMode).url
          ))
      }
    }
  }

  ".officeHolder" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).officeHolder mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(OfficeHolderPage, true)
          new CheckYourAnswersHelper(cacheMap).officeHolder mustBe
            Some(AnswerRow(
              label = "officeHolder.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.OfficeHolderController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(OfficeHolderPage, false)
          new CheckYourAnswersHelper(cacheMap).officeHolder mustBe
            Some(AnswerRow(
              label = "officeHolder.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = routes.OfficeHolderController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".workerType" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).workerType mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "Return correctly formatted answer row" in {
        val cacheMap = UserAnswers("id").set(WorkerTypePage, LimitedCompany)
        new CheckYourAnswersHelper(cacheMap).workerType mustBe
          Some(AnswerRow(
            label = "workerType.checkYourAnswersLabel",
            answer = s"workerType.$LimitedCompany",
            answerIsMessageKey = true,
            changeUrl = routes.WorkerTypeController.onPageLoad(CheckMode).url
          ))
      }
    }
  }

  ".aboutYou" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).aboutYou mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "Return correctly formatted answer row" in {
        val cacheMap = UserAnswers("id").set(AboutYouPage, Worker)
        new CheckYourAnswersHelper(cacheMap).aboutYou mustBe
          Some(AnswerRow(
            label = "aboutYou.checkYourAnswersLabel",
            answer = s"aboutYou.$Worker",
            answerIsMessageKey = true,
            changeUrl = routes.AboutYouController.onPageLoad(CheckMode).url
          ))
      }
    }
  }

  ".contractStarted" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).contractStarted mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ContractStartedPage, true)
          new CheckYourAnswersHelper(cacheMap).contractStarted mustBe
            Some(AnswerRow(
              label = "contractStarted.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.ContractStartedController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ContractStartedPage, false)
          new CheckYourAnswersHelper(cacheMap).contractStarted mustBe
            Some(AnswerRow(
              label = "contractStarted.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = routes.ContractStartedController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

}
