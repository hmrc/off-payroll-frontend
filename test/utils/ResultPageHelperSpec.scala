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

import _root_.models.UserType._
import base.GuiceAppSpecBase
import models.sections.control.ChooseWhereWork.WorkerChooses
import models.sections.control.HowWorkIsDone.NoWorkerInputAllowed
import models.sections.control.MoveWorker.CanMoveWorkerWithPermission
import models.sections.financialRisk.CannotClaimAsExpense.WorkerUsedVehicle
import models.sections.financialRisk.HowWorkerIsPaid.Commission
import models.sections.partAndParcel.IdentifyToStakeholders.WorkForEndClient
import models.sections.personalService.ArrangedSubstitute.YesClientAgreed
import models.sections.setup.AboutYouAnswer
import models.sections.setup.WorkerType.LimitedCompany
import models.{Enumerable, UserAnswers}
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{AboutYouPage, ContractStartedPage, WorkerTypePage}
import viewmodels.AnswerRow

class ResultPageHelperSpec extends GuiceAppSpecBase with Enumerable.Implicits {

  lazy val workerRequest = workerFakeRequest
  lazy val hirerRequest = hirerFakeRequest

  ".cannotClaimAsExpense" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).cannotClaimAsExpense mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "if the user is of type Worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(CannotClaimAsExpensePage, 1, Seq(WorkerUsedVehicle))
          new ResultPageHelper(cacheMap).cannotClaimAsExpense(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$CannotClaimAsExpensePage.checkYourAnswersLabel",
              answers = Seq(AnswerRow(
                label = s"$Worker.$CannotClaimAsExpensePage.checkYourAnswersLabel",
                answer = s"$Worker.$CannotClaimAsExpensePage.$WorkerUsedVehicle",
                answerIsMessageKey = true
              ))
            ))
        }
      }

      "if the user is of type Hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(CannotClaimAsExpensePage, 1, Seq(WorkerUsedVehicle))
          new ResultPageHelper(cacheMap).cannotClaimAsExpense(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$CannotClaimAsExpensePage.checkYourAnswersLabel",
              answers = Seq(AnswerRow(
                label = s"$Hirer.$CannotClaimAsExpensePage.checkYourAnswersLabel",
                answer = s"$Hirer.$CannotClaimAsExpensePage.$WorkerUsedVehicle",
                answerIsMessageKey = true
              ))
            ))
        }
      }

      "if the user is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(CannotClaimAsExpensePage, 1, Seq(WorkerUsedVehicle))
          new ResultPageHelper(cacheMap).cannotClaimAsExpense mustBe
            Some(AnswerRow(
              label = s"$CannotClaimAsExpensePage.checkYourAnswersLabel",
              answers = Seq(AnswerRow(
                label = s"$CannotClaimAsExpensePage.checkYourAnswersLabel",
                answer = s"$CannotClaimAsExpensePage.$WorkerUsedVehicle",
                answerIsMessageKey = true
              ))
            ))
        }
      }
    }
  }

  ".officeHolder" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).officeHolder mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "if the user is of type Worker" should {

          "Return correctly formatted Worker answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1, true)
            new ResultPageHelper(cacheMap).officeHolder(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true
              ))
          }
        }

        "if the user type is Hirer" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1, true)
            new ResultPageHelper(cacheMap).officeHolder(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true
              ))
          }
        }

        "if the user type is not set" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1, true)
            new ResultPageHelper(cacheMap).officeHolder mustBe
              Some(AnswerRow(
                label = s"$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1,false)
          new ResultPageHelper(cacheMap).officeHolder mustBe
            Some(AnswerRow(
              label = s"$OfficeHolderPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".workerType" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).workerType mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WorkerTypePage, 1, LimitedCompany)
          new ResultPageHelper(cacheMap).workerType(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$WorkerTypePage.checkYourAnswersLabel",
              answer = s"$Worker.$WorkerTypePage.$LimitedCompany",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WorkerTypePage, 1, LimitedCompany)
          new ResultPageHelper(cacheMap).workerType(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$WorkerTypePage.checkYourAnswersLabel",
              answer = s"$Hirer.$WorkerTypePage.$LimitedCompany",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WorkerTypePage, 1, LimitedCompany)
          new ResultPageHelper(cacheMap).workerType(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$WorkerTypePage.checkYourAnswersLabel",
              answer = s"$WorkerTypePage.$LimitedCompany",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".aboutYou" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).aboutYou mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "Return correctly formatted answer row" in {
        val cacheMap = UserAnswers("id").set(AboutYouPage, 1, AboutYouAnswer.Worker)
        new ResultPageHelper(cacheMap).aboutYou mustBe
          Some(AnswerRow(
            label = s"$AboutYouPage.checkYourAnswersLabel",
            answer = s"$AboutYouPage.${AboutYouAnswer.Worker}",
            answerIsMessageKey = true
          ))
      }
    }
  }

  ".contractStarted" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).contractStarted mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ContractStartedPage, 1, true)
            new ResultPageHelper(cacheMap).contractStarted(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ContractStartedPage, 1, true)
            new ResultPageHelper(cacheMap).contractStarted(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true
              ))
          }
        }

        "the user type is not set" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ContractStartedPage, 1, true)
            new ResultPageHelper(cacheMap).contractStarted(messages, fakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ContractStartedPage, 1,false)
          new ResultPageHelper(cacheMap).contractStarted mustBe
            Some(AnswerRow(
              label = s"$ContractStartedPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".arrangedSubstitute" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).arrangedSubstitute mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, 1, YesClientAgreed)
          new ResultPageHelper(cacheMap).arrangedSubstitute(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"$Worker.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, 1, YesClientAgreed)
          new ResultPageHelper(cacheMap).arrangedSubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"$Hirer.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, 1, YesClientAgreed)
          new ResultPageHelper(cacheMap).arrangedSubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".benefits" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).benefits mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "if the user is of type Worker" should {

          "Return correctly formatted Worker answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, 1, true)
            new ResultPageHelper(cacheMap).benefits(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true
              ))
          }
        }

        "if the user type is Hirer" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, 1, true)
            new ResultPageHelper(cacheMap).benefits(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true
              ))
          }
        }

        "if the user type is not set" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, 1, true)
            new ResultPageHelper(cacheMap).benefits mustBe
              Some(AnswerRow(
                label = s"$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(BenefitsPage, 1,false)
          new ResultPageHelper(cacheMap).benefits mustBe
            Some(AnswerRow(
              label = s"$BenefitsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".chooseWhereWork" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).chooseWhereWork mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, 1, WorkerChooses)
          new ResultPageHelper(cacheMap).chooseWhereWork(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"$Worker.$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, 1, WorkerChooses)
          new ResultPageHelper(cacheMap).chooseWhereWork(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"$Hirer.$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, 1, WorkerChooses)
          new ResultPageHelper(cacheMap).chooseWhereWork(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".didPaySubstitute" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).didPaySubstitute mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, 1, true)
          new ResultPageHelper(cacheMap).didPaySubstitute(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, 1, true)
          new ResultPageHelper(cacheMap).didPaySubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, 1, true)
          new ResultPageHelper(cacheMap).didPaySubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".howWorkerIsPaid" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).howWorkerIsPaid mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, 1, Commission)
          new ResultPageHelper(cacheMap).howWorkerIsPaid(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"$Worker.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, 1, Commission)
          new ResultPageHelper(cacheMap).howWorkerIsPaid(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"$Hirer.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, 1, Commission)
          new ResultPageHelper(cacheMap).howWorkerIsPaid(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".howWorkIsDone" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).howWorkIsDone mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, 1, NoWorkerInputAllowed)
          new ResultPageHelper(cacheMap).howWorkIsDone(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"$Worker.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, 1, NoWorkerInputAllowed)
          new ResultPageHelper(cacheMap).howWorkIsDone(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"$Hirer.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, 1, NoWorkerInputAllowed)
          new ResultPageHelper(cacheMap).howWorkIsDone(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".identifyToStakeholders" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).identifyToStakeholders mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, 1, WorkForEndClient)
          new ResultPageHelper(cacheMap).identifyToStakeholders(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"$Worker.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, 1, WorkForEndClient)
          new ResultPageHelper(cacheMap).identifyToStakeholders(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"$Hirer.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, 1, WorkForEndClient)
          new ResultPageHelper(cacheMap).identifyToStakeholders(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".interactWithStakeholders" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).interactWithStakeholders mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(InteractWithStakeholdersPage, 1, true)
          new ResultPageHelper(cacheMap).interactWithStakeholders(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$InteractWithStakeholdersPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(InteractWithStakeholdersPage, 1, true)
          new ResultPageHelper(cacheMap).interactWithStakeholders(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$InteractWithStakeholdersPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(InteractWithStakeholdersPage, 1, true)
          new ResultPageHelper(cacheMap).interactWithStakeholders(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$InteractWithStakeholdersPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".lineManagerDuties" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).lineManagerDuties mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, 1, true)
          new ResultPageHelper(cacheMap).lineManagerDuties(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, 1, true)
          new ResultPageHelper(cacheMap).lineManagerDuties(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, 1, true)
          new ResultPageHelper(cacheMap).lineManagerDuties(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".moveWorker" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).moveWorker mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, 1, CanMoveWorkerWithPermission)
          new ResultPageHelper(cacheMap).moveWorker(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"$Worker.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, 1, CanMoveWorkerWithPermission)
          new ResultPageHelper(cacheMap).moveWorker(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"$Hirer.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, 1, CanMoveWorkerWithPermission)
          new ResultPageHelper(cacheMap).moveWorker(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".neededToPayHelper" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).neededToPayHelper mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, 1, true)
          new ResultPageHelper(cacheMap).neededToPayHelper(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, 1, true)
          new ResultPageHelper(cacheMap).neededToPayHelper(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, 1, true)
          new ResultPageHelper(cacheMap).neededToPayHelper(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".rejectSubstitute" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).rejectSubstitute mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, 1, true)
          new ResultPageHelper(cacheMap).rejectSubstitute(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = s"$Worker.$RejectSubstitutePage.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, 1, true)
          new ResultPageHelper(cacheMap).rejectSubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = s"$Hirer.$RejectSubstitutePage.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, 1, true)
          new ResultPageHelper(cacheMap).rejectSubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$RejectSubstitutePage.checkYourAnswersLabel",
              answer = s"$RejectSubstitutePage.yes",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }

  ".wouldWorkerPaySubstitute" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new ResultPageHelper(UserAnswers("id")).wouldWorkerPaySubstitute mustBe None
      }
    }

    "there is an answer in the cacheMap" should {

      "the user is of type worker" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, 1, true)
          new ResultPageHelper(cacheMap).wouldWorkerPaySubstitute(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, 1, true)
          new ResultPageHelper(cacheMap).wouldWorkerPaySubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, 1, true)
          new ResultPageHelper(cacheMap).wouldWorkerPaySubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true
            ))
        }
      }
    }
  }
}
