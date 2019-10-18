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
import controllers.routes
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import models._
import models.sections.control.ChooseWhereWork.WorkerChooses
import models.sections.control.HowWorkIsDone.NoWorkerInputAllowed
import models.sections.control.MoveWorker.CanMoveWorkerWithPermission
import models.sections.financialRisk.HowWorkerIsPaid.Commission
import models.sections.partAndParcel.IdentifyToStakeholders.WorkForEndClient
import models.sections.personalService.ArrangedSubstitute.YesClientAgreed
import models.sections.setup.WhatDoYouWantToDo.{CheckDetermination, MakeNewDetermination}
import models.sections.setup.{WhatDoYouWantToFindOut, WhoAreYou}
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import viewmodels.AnswerRow

class CheckYourAnswersHelperSpec extends GuiceAppSpecBase with Enumerable.Implicits {

  lazy val workerRequest = workerFakeRequest
  lazy val hirerRequest = hirerFakeRequest

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
                label = s"$Worker.optimised.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"$Worker.optimised.$OfficeHolderPage.changeLinkContext")
              ))
          }
        }

        "if the user type is Hirer" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).officeHolder(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.optimised.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"$Hirer.optimised.$OfficeHolderPage.changeLinkContext")
              ))
          }
        }

        "if the user type is not set" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).officeHolder mustBe
              Some(AnswerRow(
                label = s"optimised.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"optimised.$OfficeHolderPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1,false)
          new CheckYourAnswersHelper(cacheMap).officeHolder mustBe
            Some(AnswerRow(
              label = s"optimised.$OfficeHolderPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"optimised.$OfficeHolderPage.changeLinkContext")
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
              label = s"$Worker.optimised.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"$Worker.optimised.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(ArrangedSubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$ArrangedSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, 1, YesClientAgreed)
          new CheckYourAnswersHelper(cacheMap).arrangedSubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"$Hirer.optimised.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(ArrangedSubstitutePage).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$ArrangedSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, 1, YesClientAgreed)
          new CheckYourAnswersHelper(cacheMap).arrangedSubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"optimised.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(ArrangedSubstitutePage).url),
              changeContextMsgKey = Some(s"optimised.$ArrangedSubstitutePage.changeLinkContext")
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
                label = s"$Worker.optimised.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"$Worker.optimised.$BenefitsPage.changeLinkContext")
              ))
          }
        }

        "if the user type is Hirer" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).benefits(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.optimised.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"$Hirer.optimised.$BenefitsPage.changeLinkContext")
              ))
          }
        }

        "if the user type is not set" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).benefits mustBe
              Some(AnswerRow(
                label = s"optimised.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"optimised.$BenefitsPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(BenefitsPage, 1,false)
          new CheckYourAnswersHelper(cacheMap).benefits mustBe
            Some(AnswerRow(
              label = s"optimised.$BenefitsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"optimised.$BenefitsPage.changeLinkContext")
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
              label = s"$Worker.optimised.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"$Worker.optimised.$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$ChooseWhereWorkPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, 1, WorkerChooses)
          new CheckYourAnswersHelper(cacheMap).chooseWhereWork(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"$Hirer.optimised.$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$ChooseWhereWorkPage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, 1, WorkerChooses)
          new CheckYourAnswersHelper(cacheMap).chooseWhereWork(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"optimised.$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"optimised.$ChooseWhereWorkPage.changeLinkContext")
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
              label = s"$Worker.optimised.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(DidPaySubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$DidPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).didPaySubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(DidPaySubstitutePage).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$DidPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).didPaySubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(DidPaySubstitutePage).url),
              changeContextMsgKey = Some(s"optimised.$DidPaySubstitutePage.changeLinkContext")
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
              label = s"$Worker.optimised.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"$Worker.optimised.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = Some(financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$HowWorkerIsPaidPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, 1, Commission)
          new CheckYourAnswersHelper(cacheMap).howWorkerIsPaid(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"$Hirer.optimised.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = Some(financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$HowWorkerIsPaidPage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, 1, Commission)
          new CheckYourAnswersHelper(cacheMap).howWorkerIsPaid(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"optimised.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = Some(financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"optimised.$HowWorkerIsPaidPage.changeLinkContext")
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
              label = s"$Worker.optimised.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"$Worker.optimised.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$HowWorkIsDonePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, 1, NoWorkerInputAllowed)
          new CheckYourAnswersHelper(cacheMap).howWorkIsDone(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"$Hirer.optimised.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$HowWorkIsDonePage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, 1, NoWorkerInputAllowed)
          new CheckYourAnswersHelper(cacheMap).howWorkIsDone(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"optimised.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"optimised.$HowWorkIsDonePage.changeLinkContext")
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
              label = s"$Worker.optimised.$InteractWithStakeholdersPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.InteractWithStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$InteractWithStakeholdersPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(InteractWithStakeholdersPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).interactWithStakeholders(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$InteractWithStakeholdersPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.InteractWithStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$InteractWithStakeholdersPage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(InteractWithStakeholdersPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).interactWithStakeholders(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$InteractWithStakeholdersPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.InteractWithStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"optimised.$InteractWithStakeholdersPage.changeLinkContext")
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
              label = s"$Worker.optimised.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"$Worker.optimised.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$IdentifyToStakeholdersPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, 1, WorkForEndClient)
          new CheckYourAnswersHelper(cacheMap).identifyToStakeholders(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"$Hirer.optimised.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$IdentifyToStakeholdersPage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, 1, WorkForEndClient)
          new CheckYourAnswersHelper(cacheMap).identifyToStakeholders(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"optimised.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"optimised.$IdentifyToStakeholdersPage.changeLinkContext")
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
              label = s"$Worker.optimised.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$LineManagerDutiesPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).lineManagerDuties(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$LineManagerDutiesPage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).lineManagerDuties(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"optimised.$LineManagerDutiesPage.changeLinkContext")
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
              label = s"$Worker.optimised.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"$Worker.optimised.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$MoveWorkerPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, 1, CanMoveWorkerWithPermission)
          new CheckYourAnswersHelper(cacheMap).moveWorker(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"$Hirer.optimised.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$MoveWorkerPage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, 1, CanMoveWorkerWithPermission)
          new CheckYourAnswersHelper(cacheMap).moveWorker(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"optimised.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"optimised.$MoveWorkerPage.changeLinkContext")
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
              label = s"$Worker.optimised.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(NeededToPayHelperPage).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$NeededToPayHelperPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).neededToPayHelper(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(NeededToPayHelperPage).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$NeededToPayHelperPage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).neededToPayHelper(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(NeededToPayHelperPage).url),
              changeContextMsgKey = Some(s"optimised.$NeededToPayHelperPage.changeLinkContext")
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
              label = s"$Worker.optimised.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(RejectSubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$RejectSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).rejectSubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(RejectSubstitutePage).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$RejectSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).rejectSubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(RejectSubstitutePage).url),
              changeContextMsgKey = Some(s"optimised.$RejectSubstitutePage.changeLinkContext")
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
              label = s"$Worker.optimised.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(WouldWorkerPaySubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.optimised.$WouldWorkerPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).wouldWorkerPaySubstitute(messages, hirerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.optimised.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(WouldWorkerPaySubstitutePage).url),
              changeContextMsgKey = Some(s"$Hirer.optimised.$WouldWorkerPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type is not set" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).wouldWorkerPaySubstitute(messages, fakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"optimised.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(WouldWorkerPaySubstitutePage).url),
              changeContextMsgKey = Some(s"optimised.$WouldWorkerPaySubstitutePage.changeLinkContext")
            ))
        }
      }
    }
  }


  //SETUP

  ".whatDoYouWantToFindOut" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).whatDoYouWantToFindOut mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is PAYE" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WhatDoYouWantToFindOutPage, 1, WhatDoYouWantToFindOut.PAYE)
          new CheckYourAnswersHelper(cacheMap).whatDoYouWantToFindOut(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$WhatDoYouWantToFindOutPage.checkYourAnswersLabel",
              answer = s"$WhatDoYouWantToFindOutPage.${WhatDoYouWantToFindOut.PAYE}",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"$WhatDoYouWantToFindOutPage.changeLinkContext")
            ))
        }
      }

      "the answer is IR35" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WhatDoYouWantToFindOutPage, 1, WhatDoYouWantToFindOut.IR35)
          new CheckYourAnswersHelper(cacheMap).whatDoYouWantToFindOut(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$WhatDoYouWantToFindOutPage.checkYourAnswersLabel",
              answer = s"$WhatDoYouWantToFindOutPage.${WhatDoYouWantToFindOut.IR35}",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"$WhatDoYouWantToFindOutPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".whatDoYouWantToDo" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).whatDoYouWantToDo mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is MakeNewDetermination" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WhatDoYouWantToDoPage, 1, MakeNewDetermination)
          new CheckYourAnswersHelper(cacheMap).whatDoYouWantToDo(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$WhatDoYouWantToDoPage.checkYourAnswersLabel",
              answer = s"$WhatDoYouWantToDoPage.$MakeNewDetermination",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"$WhatDoYouWantToDoPage.changeLinkContext")
            ))
        }
      }

      "the answer is CheckDetermination" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WhatDoYouWantToDoPage, 1, CheckDetermination)
          new CheckYourAnswersHelper(cacheMap).whatDoYouWantToDo(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$WhatDoYouWantToDoPage.checkYourAnswersLabel",
              answer = s"$WhatDoYouWantToDoPage.$CheckDetermination",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"$WhatDoYouWantToDoPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".whoAreYou" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).whoAreYou mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(WhoAreYouPage, 1, WhoAreYou.Worker)
            new CheckYourAnswersHelper(cacheMap).whoAreYou(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$WhoAreYouPage.checkYourAnswersLabel",
                answer = s"whoAreYou.personDoingWork",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"$WhoAreYouPage.changeLinkContext")
              ))
          }
      }

      "the user type is of Hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WhoAreYouPage, 1, WhoAreYou.Client)
          new CheckYourAnswersHelper(cacheMap).whoAreYou(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$WhoAreYouPage.checkYourAnswersLabel",
              answer = s"whoAreYou.endClient",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"$WhoAreYouPage.changeLinkContext")
            ))
        }
      }

      "the user type is of Agent" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WhoAreYouPage, 1, WhoAreYou.Agency)
          new CheckYourAnswersHelper(cacheMap).whoAreYou(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$WhoAreYouPage.checkYourAnswersLabel",
              answer = s"whoAreYou.placingAgency",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"$WhoAreYouPage.changeLinkContext")
            ))
        }
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
                label = s"$Worker.optimised.$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"$Worker.optimised.$ContractStartedPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ContractStartedPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).contractStarted(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.optimised.$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"$Hirer.optimised.$ContractStartedPage.changeLinkContext")
              ))
          }
        }

        "the user type is not set" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ContractStartedPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).contractStarted(messages, fakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"optimised.$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"optimised.$ContractStartedPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ContractStartedPage, 1,false)
          new CheckYourAnswersHelper(cacheMap).contractStarted mustBe
            Some(AnswerRow(
              label = s"optimised.$ContractStartedPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"optimised.$ContractStartedPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".workerUsingIntermediary" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).workerUsingIntermediary mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(WorkerUsingIntermediaryPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).workerUsingIntermediary(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$WorkerUsingIntermediaryPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"$Worker.$WorkerUsingIntermediaryPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(WorkerUsingIntermediaryPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).workerUsingIntermediary(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$WorkerUsingIntermediaryPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"$Hirer.$WorkerUsingIntermediaryPage.changeLinkContext")
              ))
          }
        }

        "the user type is not set" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(WorkerUsingIntermediaryPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).workerUsingIntermediary(messages, fakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$WorkerUsingIntermediaryPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"$WorkerUsingIntermediaryPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WorkerUsingIntermediaryPage, 1,false)
          new CheckYourAnswersHelper(cacheMap).workerUsingIntermediary mustBe
            Some(AnswerRow(
              label = s"$WorkerUsingIntermediaryPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"$WorkerUsingIntermediaryPage.changeLinkContext")
            ))
        }
      }
    }
  }

  //Business On Own Account
  ".workerKnown" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).workerKnown mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(WorkerKnownPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).workerKnown(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$WorkerKnownPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(WorkerKnownPage).url),
                changeContextMsgKey = Some(s"$Worker.$WorkerKnownPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(WorkerKnownPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).workerKnown(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$WorkerKnownPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(WorkerKnownPage).url),
                changeContextMsgKey = Some(s"$Hirer.$WorkerKnownPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WorkerKnownPage, 1, false)
          new CheckYourAnswersHelper(cacheMap).workerKnown(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$WorkerKnownPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(WorkerKnownPage).url),
              changeContextMsgKey = Some(s"$Worker.$WorkerKnownPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".multipleContracts" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).multipleContracts mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(MultipleContractsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).multipleContracts(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$MultipleContractsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MultipleContractsPage).url),
                changeContextMsgKey = Some(s"$Worker.$MultipleContractsPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(MultipleContractsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).multipleContracts(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$MultipleContractsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MultipleContractsPage).url),
                changeContextMsgKey = Some(s"$Hirer.$MultipleContractsPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MultipleContractsPage, 1, false)
          new CheckYourAnswersHelper(cacheMap).multipleContracts(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$MultipleContractsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MultipleContractsPage).url),
              changeContextMsgKey = Some(s"$Worker.$MultipleContractsPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".permissionToWorkWithOthers" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).permissionToWorkWithOthers mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(PermissionToWorkWithOthersPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).permissionToWorkWithOthers(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$PermissionToWorkWithOthersPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PermissionToWorkWithOthersPage).url),
                changeContextMsgKey = Some(s"$Worker.$PermissionToWorkWithOthersPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(PermissionToWorkWithOthersPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).permissionToWorkWithOthers(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$PermissionToWorkWithOthersPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PermissionToWorkWithOthersPage).url),
                changeContextMsgKey = Some(s"$Hirer.$PermissionToWorkWithOthersPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(PermissionToWorkWithOthersPage, 1, false)
          new CheckYourAnswersHelper(cacheMap).permissionToWorkWithOthers(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$PermissionToWorkWithOthersPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PermissionToWorkWithOthersPage).url),
              changeContextMsgKey = Some(s"$Worker.$PermissionToWorkWithOthersPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".ownershipRights" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).ownershipRights mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(OwnershipRightsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).ownershipRights(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$OwnershipRightsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(OwnershipRightsPage).url),
                changeContextMsgKey = Some(s"$Worker.$OwnershipRightsPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(OwnershipRightsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).ownershipRights(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$OwnershipRightsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(OwnershipRightsPage).url),
                changeContextMsgKey = Some(s"$Hirer.$OwnershipRightsPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(OwnershipRightsPage, 1, false)
          new CheckYourAnswersHelper(cacheMap).ownershipRights(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$OwnershipRightsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(OwnershipRightsPage).url),
              changeContextMsgKey = Some(s"$Worker.$OwnershipRightsPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".rightsOfWork" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).rightsOfWork mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(RightsOfWorkPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).rightsOfWork(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$RightsOfWorkPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(RightsOfWorkPage).url),
                changeContextMsgKey = Some(s"$Worker.$RightsOfWorkPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(RightsOfWorkPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).rightsOfWork(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$RightsOfWorkPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(RightsOfWorkPage).url),
                changeContextMsgKey = Some(s"$Hirer.$RightsOfWorkPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RightsOfWorkPage, 1, false)
          new CheckYourAnswersHelper(cacheMap).rightsOfWork(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$RightsOfWorkPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(RightsOfWorkPage).url),
              changeContextMsgKey = Some(s"$Worker.$RightsOfWorkPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".transferOfRights" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).transferOfRights mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(TransferOfRightsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).transferOfRights(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$TransferOfRightsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(TransferOfRightsPage).url),
                changeContextMsgKey = Some(s"$Worker.$TransferOfRightsPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(TransferOfRightsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).transferOfRights(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$TransferOfRightsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(TransferOfRightsPage).url),
                changeContextMsgKey = Some(s"$Hirer.$TransferOfRightsPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(TransferOfRightsPage, 1, false)
          new CheckYourAnswersHelper(cacheMap).transferOfRights(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$TransferOfRightsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(TransferOfRightsPage).url),
              changeContextMsgKey = Some(s"$Worker.$TransferOfRightsPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".previousContract" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).previousContract mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(PreviousContractPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).previousContract(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$PreviousContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PreviousContractPage).url),
                changeContextMsgKey = Some(s"$Worker.$PreviousContractPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(PreviousContractPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).previousContract(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$PreviousContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PreviousContractPage).url),
                changeContextMsgKey = Some(s"$Hirer.$PreviousContractPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(PreviousContractPage, 1, false)
          new CheckYourAnswersHelper(cacheMap).previousContract(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$PreviousContractPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PreviousContractPage).url),
              changeContextMsgKey = Some(s"$Worker.$PreviousContractPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".followOnContract" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).followOnContract mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(FollowOnContractPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).followOnContract(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$FollowOnContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FollowOnContractPage).url),
                changeContextMsgKey = Some(s"$Worker.$FollowOnContractPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(FollowOnContractPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).followOnContract(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$FollowOnContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FollowOnContractPage).url),
                changeContextMsgKey = Some(s"$Hirer.$FollowOnContractPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(FollowOnContractPage, 1, false)
          new CheckYourAnswersHelper(cacheMap).followOnContract(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$FollowOnContractPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FollowOnContractPage).url),
              changeContextMsgKey = Some(s"$Worker.$FollowOnContractPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".firstContract" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).firstContract mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(FirstContractPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).firstContract(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$FirstContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FirstContractPage).url),
                changeContextMsgKey = Some(s"$Worker.$FirstContractPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(FirstContractPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).firstContract(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$FirstContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FirstContractPage).url),
                changeContextMsgKey = Some(s"$Hirer.$FirstContractPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(FirstContractPage, 1, false)
          new CheckYourAnswersHelper(cacheMap).firstContract(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$FirstContractPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FirstContractPage).url),
              changeContextMsgKey = Some(s"$Worker.$FirstContractPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".extendContract" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).extendContract mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ExtendContractPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).extendContract(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$ExtendContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(ExtendContractPage).url),
                changeContextMsgKey = Some(s"$Worker.$ExtendContractPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ExtendContractPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).extendContract(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$ExtendContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(ExtendContractPage).url),
                changeContextMsgKey = Some(s"$Hirer.$ExtendContractPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ExtendContractPage, 1, false)
          new CheckYourAnswersHelper(cacheMap).extendContract(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$ExtendContractPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(ExtendContractPage).url),
              changeContextMsgKey = Some(s"$Worker.$ExtendContractPage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".majorityOfWorkingTime" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).majorityOfWorkingTime mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(MajorityOfWorkingTimePage, 1, true)
            new CheckYourAnswersHelper(cacheMap).majorityOfWorkingTime(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$MajorityOfWorkingTimePage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MajorityOfWorkingTimePage).url),
                changeContextMsgKey = Some(s"$Worker.$MajorityOfWorkingTimePage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(MajorityOfWorkingTimePage, 1, true)
            new CheckYourAnswersHelper(cacheMap).majorityOfWorkingTime(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$MajorityOfWorkingTimePage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MajorityOfWorkingTimePage).url),
                changeContextMsgKey = Some(s"$Hirer.$MajorityOfWorkingTimePage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MajorityOfWorkingTimePage, 1, false)
          new CheckYourAnswersHelper(cacheMap).majorityOfWorkingTime(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$MajorityOfWorkingTimePage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MajorityOfWorkingTimePage).url),
              changeContextMsgKey = Some(s"$Worker.$MajorityOfWorkingTimePage.changeLinkContext")
            ))
        }
      }
    }
  }

  ".similarWorkOtherClients" when {

    "there is no answer in the cacheMap" should {

      "Return None" in {
        new CheckYourAnswersHelper(UserAnswers("id")).similarWorkOtherClients mustBe None
      }
    }

    "there is an answer in the cacheMap" when {

      "the answer is yes" should {

        "the user type is of Worker" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(SimilarWorkOtherClientsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).similarWorkOtherClients(messages, workerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$SimilarWorkOtherClientsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(SimilarWorkOtherClientsPage).url),
                changeContextMsgKey = Some(s"$Worker.$SimilarWorkOtherClientsPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(SimilarWorkOtherClientsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).similarWorkOtherClients(messages, hirerRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$SimilarWorkOtherClientsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(SimilarWorkOtherClientsPage).url),
                changeContextMsgKey = Some(s"$Hirer.$SimilarWorkOtherClientsPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(SimilarWorkOtherClientsPage, 1, false)
          new CheckYourAnswersHelper(cacheMap).similarWorkOtherClients(messages, workerRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$SimilarWorkOtherClientsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(SimilarWorkOtherClientsPage).url),
              changeContextMsgKey = Some(s"$Worker.$SimilarWorkOtherClientsPage.changeLinkContext")
            ))
        }
      }
    }
  }
}
