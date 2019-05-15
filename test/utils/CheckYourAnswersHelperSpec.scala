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
import config.SessionKeys
import controllers.routes
import _root_.models.UserType._
import models.ArrangedSubstitute.YesClientAgreed
import models.CannotClaimAsExpense.WorkerUsedVehicle
import models.ChooseWhereWork.WorkerChooses
import models.HowWorkIsDone.NoWorkerInputAllowed
import models.HowWorkerIsPaid.Commission
import models.IdentifyToStakeholders.WorkForEndClient
import models.MoveWorker.CanMoveWorkerWithPermission
import models.WorkerType.LimitedCompany
import models.{AboutYouAnswer, CheckMode, Enumerable, UserAnswers}
import pages._
import play.api.libs.json.Json
import viewmodels.AnswerRow

class CheckYourAnswersHelperSpec extends SpecBase with Enumerable.Implicits {

  lazy val workerRequest = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
  lazy val hirerRequest = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)

  ".cannotClaimAsExpense" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).cannotClaimAsExpense mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "if the user is of type Worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(CannotClaimAsExpensePage, 1, Seq(WorkerUsedVehicle))
          new CheckYourAnswersHelper(cacheMap).cannotClaimAsExpense(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$CannotClaimAsExpensePage.checkYourAnswersLabel",
              answer = Seq(s"$Worker.$CannotClaimAsExpensePage.$WorkerUsedVehicle"),
              answerIsMessageKey = true,
              changeUrl = routes.CannotClaimAsExpenseController.onPageLoad(CheckMode).url
            ))
        }
      }

      "if the user is of type Hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(CannotClaimAsExpensePage, 1, Seq(WorkerUsedVehicle))
          new CheckYourAnswersHelper(cacheMap).cannotClaimAsExpense(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$CannotClaimAsExpensePage.checkYourAnswersLabel",
              answer = Seq(s"$Hirer.$CannotClaimAsExpensePage.$WorkerUsedVehicle"),
              answerIsMessageKey = true,
              changeUrl = routes.CannotClaimAsExpenseController.onPageLoad(CheckMode).url
            ))
        }
      }

      "if the user is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(CannotClaimAsExpensePage, 1, Seq(WorkerUsedVehicle))
          new CheckYourAnswersHelper(cacheMap).cannotClaimAsExpense mustBe
            Some(AnswerRow(
              label = s"$CannotClaimAsExpensePage.checkYourAnswersLabel",
              answer = Seq(s"$CannotClaimAsExpensePage.$WorkerUsedVehicle"),
              answerIsMessageKey = true,
              changeUrl = routes.CannotClaimAsExpenseController.onPageLoad(CheckMode).url
            ))
        }
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

        "if the user is of type Worker" should {

          "Return correctly formatted Worker answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).officeHolder(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = routes.OfficeHolderController.onPageLoad(CheckMode).url
              ))
          }
        }

        "if the user type is Hirer" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).officeHolder(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = routes.OfficeHolderController.onPageLoad(CheckMode).url
              ))
          }
        }

        "if the user type is not set" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).officeHolder mustBe
              Some(AnswerRow(
                label = s"$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = routes.OfficeHolderController.onPageLoad(CheckMode).url
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1,false)
          new CheckYourAnswersHelper(cacheMap).officeHolder mustBe
            Some(AnswerRow(
              label = s"$OfficeHolderPage.checkYourAnswersLabel",
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

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WorkerTypePage, 1, LimitedCompany)
          new CheckYourAnswersHelper(cacheMap).workerType(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$WorkerTypePage.checkYourAnswersLabel",
              answer = s"$Worker.$WorkerTypePage.$LimitedCompany",
              answerIsMessageKey = true,
              changeUrl = routes.WorkerTypeController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WorkerTypePage, 1, LimitedCompany)
          new CheckYourAnswersHelper(cacheMap).workerType(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$WorkerTypePage.checkYourAnswersLabel",
              answer = s"$Hirer.$WorkerTypePage.$LimitedCompany",
              answerIsMessageKey = true,
              changeUrl = routes.WorkerTypeController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WorkerTypePage, 1, LimitedCompany)
          new CheckYourAnswersHelper(cacheMap).workerType(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$WorkerTypePage.checkYourAnswersLabel",
              answer = s"$WorkerTypePage.$LimitedCompany",
              answerIsMessageKey = true,
              changeUrl = routes.WorkerTypeController.onPageLoad(CheckMode).url
            ))
        }
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
        val cacheMap = UserAnswers("id").set(AboutYouPage, 1, AboutYouAnswer.Worker)
        new CheckYourAnswersHelper(cacheMap).aboutYou mustBe
          Some(AnswerRow(
            label = s"$AboutYouPage.checkYourAnswersLabel",
            answer = s"$AboutYouPage.${AboutYouAnswer.Worker}",
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

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ContractStartedPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).contractStarted(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = routes.ContractStartedController.onPageLoad(CheckMode).url
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ContractStartedPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).contractStarted(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = routes.ContractStartedController.onPageLoad(CheckMode).url
              ))
          }
        }

        "the user type is not set" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ContractStartedPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).contractStarted(messages, fakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = routes.ContractStartedController.onPageLoad(CheckMode).url
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ContractStartedPage, 1,false)
          new CheckYourAnswersHelper(cacheMap).contractStarted mustBe
            Some(AnswerRow(
              label = s"$ContractStartedPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = routes.ContractStartedController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".arrangedSubstitute" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).arrangedSubstitute mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, 1, YesClientAgreed)
          new CheckYourAnswersHelper(cacheMap).arrangedSubstitute(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"$Worker.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = routes.ArrangedSubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, 1, YesClientAgreed)
          new CheckYourAnswersHelper(cacheMap).arrangedSubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"$Hirer.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = routes.ArrangedSubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, 1, YesClientAgreed)
          new CheckYourAnswersHelper(cacheMap).arrangedSubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = routes.ArrangedSubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".benefits" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).benefits mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "if the user is of type Worker" should {

          "Return correctly formatted Worker answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).benefits(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = routes.BenefitsController.onPageLoad(CheckMode).url
              ))
          }
        }

        "if the user type is Hirer" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).benefits(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = routes.BenefitsController.onPageLoad(CheckMode).url
              ))
          }
        }

        "if the user type is not set" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).benefits mustBe
              Some(AnswerRow(
                label = s"$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = routes.BenefitsController.onPageLoad(CheckMode).url
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(BenefitsPage, 1,false)
          new CheckYourAnswersHelper(cacheMap).benefits mustBe
            Some(AnswerRow(
              label = s"$BenefitsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = routes.BenefitsController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".chooseWhereWork" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).chooseWhereWork mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, 1, WorkerChooses)
          new CheckYourAnswersHelper(cacheMap).chooseWhereWork(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"$Worker.$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true,
              changeUrl = routes.ChooseWhereWorkController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, 1, WorkerChooses)
          new CheckYourAnswersHelper(cacheMap).chooseWhereWork(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"$Hirer.$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true,
              changeUrl = routes.ChooseWhereWorkController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, 1, WorkerChooses)
          new CheckYourAnswersHelper(cacheMap).chooseWhereWork(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true,
              changeUrl = routes.ChooseWhereWorkController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".didPaySubstitute" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).didPaySubstitute mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).didPaySubstitute(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.DidPaySubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).didPaySubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.DidPaySubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).didPaySubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.DidPaySubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".howWorkerIsPaid" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).howWorkerIsPaid mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, 1, Commission)
          new CheckYourAnswersHelper(cacheMap).howWorkerIsPaid(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"$Worker.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = routes.HowWorkerIsPaidController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, 1, Commission)
          new CheckYourAnswersHelper(cacheMap).howWorkerIsPaid(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"$Hirer.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = routes.HowWorkerIsPaidController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, 1, Commission)
          new CheckYourAnswersHelper(cacheMap).howWorkerIsPaid(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = routes.HowWorkerIsPaidController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".howWorkIsDone" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).howWorkIsDone mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, 1, NoWorkerInputAllowed)
          new CheckYourAnswersHelper(cacheMap).howWorkIsDone(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"$Worker.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = routes.HowWorkIsDoneController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, 1, NoWorkerInputAllowed)
          new CheckYourAnswersHelper(cacheMap).howWorkIsDone(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"$Hirer.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = routes.HowWorkIsDoneController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, 1, NoWorkerInputAllowed)
          new CheckYourAnswersHelper(cacheMap).howWorkIsDone(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = routes.HowWorkIsDoneController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".identifyToStakeholders" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).identifyToStakeholders mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, 1, WorkForEndClient)
          new CheckYourAnswersHelper(cacheMap).identifyToStakeholders(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"$Worker.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = routes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, 1, WorkForEndClient)
          new CheckYourAnswersHelper(cacheMap).identifyToStakeholders(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"$Hirer.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = routes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, 1, WorkForEndClient)
          new CheckYourAnswersHelper(cacheMap).identifyToStakeholders(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = routes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".interactWithStakeholders" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).interactWithStakeholders mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(InteractWithStakeholdersPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).interactWithStakeholders(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$InteractWithStakeholdersPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.InteractWithStakeholdersController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(InteractWithStakeholdersPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).interactWithStakeholders(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$InteractWithStakeholdersPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.InteractWithStakeholdersController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(InteractWithStakeholdersPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).interactWithStakeholders(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$InteractWithStakeholdersPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.InteractWithStakeholdersController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".lineManagerDuties" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).lineManagerDuties mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).lineManagerDuties(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.LineManagerDutiesController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).lineManagerDuties(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.LineManagerDutiesController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).lineManagerDuties(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.LineManagerDutiesController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".moveWorker" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).moveWorker mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, 1, CanMoveWorkerWithPermission)
          new CheckYourAnswersHelper(cacheMap).moveWorker(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"$Worker.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = routes.MoveWorkerController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, 1, CanMoveWorkerWithPermission)
          new CheckYourAnswersHelper(cacheMap).moveWorker(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"$Hirer.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = routes.MoveWorkerController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, 1, CanMoveWorkerWithPermission)
          new CheckYourAnswersHelper(cacheMap).moveWorker(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = routes.MoveWorkerController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".neededToPayHelper" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).neededToPayHelper mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).neededToPayHelper(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.NeededToPayHelperController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).neededToPayHelper(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.NeededToPayHelperController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).neededToPayHelper(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.NeededToPayHelperController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".rejectSubstitute" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).rejectSubstitute mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).rejectSubstitute(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = s"$Worker.$RejectSubstitutePage.yes",
              answerIsMessageKey = true,
              changeUrl = routes.RejectSubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).rejectSubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = s"$Hirer.$RejectSubstitutePage.yes",
              answerIsMessageKey = true,
              changeUrl = routes.RejectSubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).rejectSubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$RejectSubstitutePage.checkYourAnswersLabel",
              answer = s"$RejectSubstitutePage.yes",
              answerIsMessageKey = true,
              changeUrl = routes.RejectSubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

  ".wouldWorkerPaySubstitute" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).wouldWorkerPaySubstitute mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).wouldWorkerPaySubstitute(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.WouldWorkerPaySubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).wouldWorkerPaySubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.WouldWorkerPaySubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).wouldWorkerPaySubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = routes.WouldWorkerPaySubstituteController.onPageLoad(CheckMode).url
            ))
        }
      }
    }
  }

}
