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
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import viewmodels.AnswerRow

class CheckYourAnswersHelperSpec extends GuiceAppSpecBase with Enumerable.Implicits {


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
            new CheckYourAnswersHelper(cacheMap).officeHolder(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"$Worker.$OfficeHolderPage.changeLinkContext")
              ))
          }
        }

        "if the user type is Hirer" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).officeHolder(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"$Hirer.$OfficeHolderPage.changeLinkContext")
              ))
          }
        }

        "if the user type is agency" should {

          "Return correctly formatted Worker answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).officeHolder(messages, agencyFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"$Worker.$OfficeHolderPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(OfficeHolderPage, 1,false)
          new CheckYourAnswersHelper(cacheMap).officeHolder(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$OfficeHolderPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$OfficeHolderPage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).arrangedSubstitute(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"$Worker.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(ArrangedSubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.$ArrangedSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, 1, YesClientAgreed)
          new CheckYourAnswersHelper(cacheMap).arrangedSubstitute(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"$Hirer.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(ArrangedSubstitutePage).url),
              changeContextMsgKey = Some(s"$Hirer.$ArrangedSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, 1, YesClientAgreed)
          new CheckYourAnswersHelper(cacheMap).arrangedSubstitute(messages, agencyFakeDataRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"$Worker.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(ArrangedSubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.$ArrangedSubstitutePage.changeLinkContext")
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
            new CheckYourAnswersHelper(cacheMap).benefits(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"$Worker.$BenefitsPage.changeLinkContext")
              ))
          }
        }

        "if the user type is Hirer" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).benefits(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"$Hirer.$BenefitsPage.changeLinkContext")
              ))
          }
        }

        "if the user type is agency" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).benefits(messages, agencyFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"$Worker.$BenefitsPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(BenefitsPage, 1,false)
          new CheckYourAnswersHelper(cacheMap).benefits(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$BenefitsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$BenefitsPage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).chooseWhereWork(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"$Worker.$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$ChooseWhereWorkPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, 1, WorkerChooses)
          new CheckYourAnswersHelper(cacheMap).chooseWhereWork(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"$Hirer.$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.$ChooseWhereWorkPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, 1, WorkerChooses)
          new CheckYourAnswersHelper(cacheMap).chooseWhereWork(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"$Worker.$ChooseWhereWorkPage.$WorkerChooses",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$ChooseWhereWorkPage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).didPaySubstitute(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(DidPaySubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.$DidPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).didPaySubstitute(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(DidPaySubstitutePage).url),
              changeContextMsgKey = Some(s"$Hirer.$DidPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).didPaySubstitute(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(DidPaySubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.$DidPaySubstitutePage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).howWorkerIsPaid(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"$Worker.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = Some(financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$HowWorkerIsPaidPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, 1, Commission)
          new CheckYourAnswersHelper(cacheMap).howWorkerIsPaid(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"$Hirer.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = Some(financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.$HowWorkerIsPaidPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, 1, Commission)
          new CheckYourAnswersHelper(cacheMap).howWorkerIsPaid(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"$Worker.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = Some(financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$HowWorkerIsPaidPage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).howWorkIsDone(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"$Worker.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$HowWorkIsDonePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, 1, NoWorkerInputAllowed)
          new CheckYourAnswersHelper(cacheMap).howWorkIsDone(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"$Hirer.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.$HowWorkIsDonePage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, 1, NoWorkerInputAllowed)
          new CheckYourAnswersHelper(cacheMap).howWorkIsDone(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"$Worker.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$HowWorkIsDonePage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).identifyToStakeholders(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"$Worker.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$IdentifyToStakeholdersPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, 1, WorkForEndClient)
          new CheckYourAnswersHelper(cacheMap).identifyToStakeholders(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"$Hirer.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.$IdentifyToStakeholdersPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, 1, WorkForEndClient)
          new CheckYourAnswersHelper(cacheMap).identifyToStakeholders(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"$Worker.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$IdentifyToStakeholdersPage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).lineManagerDuties(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$LineManagerDutiesPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).lineManagerDuties(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.$LineManagerDutiesPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).lineManagerDuties(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$LineManagerDutiesPage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).moveWorker(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"$Worker.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$MoveWorkerPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, 1, CanMoveWorkerWithPermission)
          new CheckYourAnswersHelper(cacheMap).moveWorker(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"$Hirer.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Hirer.$MoveWorkerPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, 1, CanMoveWorkerWithPermission)
          new CheckYourAnswersHelper(cacheMap).moveWorker(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"$Worker.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"$Worker.$MoveWorkerPage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).neededToPayHelper(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(NeededToPayHelperPage).url),
              changeContextMsgKey = Some(s"$Worker.$NeededToPayHelperPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).neededToPayHelper(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(NeededToPayHelperPage).url),
              changeContextMsgKey = Some(s"$Hirer.$NeededToPayHelperPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, 1, true)
          new CheckYourAnswersHelper(cacheMap).neededToPayHelper(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(NeededToPayHelperPage).url),
              changeContextMsgKey = Some(s"$Worker.$NeededToPayHelperPage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).rejectSubstitute(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(RejectSubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.$RejectSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).rejectSubstitute(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(RejectSubstitutePage).url),
              changeContextMsgKey = Some(s"$Hirer.$RejectSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).rejectSubstitute(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(RejectSubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.$RejectSubstitutePage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).wouldWorkerPaySubstitute(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(WouldWorkerPaySubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.$WouldWorkerPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).wouldWorkerPaySubstitute(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Hirer.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(WouldWorkerPaySubstitutePage).url),
              changeContextMsgKey = Some(s"$Hirer.$WouldWorkerPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, 1, true)
          new CheckYourAnswersHelper(cacheMap).wouldWorkerPaySubstitute(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(WouldWorkerPaySubstitutePage).url),
              changeContextMsgKey = Some(s"$Worker.$WouldWorkerPaySubstitutePage.changeLinkContext")
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
          new CheckYourAnswersHelper(cacheMap).whatDoYouWantToFindOut(messages, workerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).whatDoYouWantToFindOut(messages, workerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).whatDoYouWantToDo(messages, workerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).whatDoYouWantToDo(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).whoAreYou(messages, workerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).whoAreYou(messages, workerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).whoAreYou(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).contractStarted(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Worker.$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"$Worker.$ContractStartedPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ContractStartedPage, 1, true)
            new CheckYourAnswersHelper(cacheMap).contractStarted(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"$Hirer.$ContractStartedPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ContractStartedPage, 1,false)
          new CheckYourAnswersHelper(cacheMap).contractStarted(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$ContractStartedPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"$Worker.$ContractStartedPage.changeLinkContext")
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
            new CheckYourAnswersHelper(cacheMap).workerUsingIntermediary(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).workerUsingIntermediary(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"$Hirer.$WorkerUsingIntermediaryPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"$Hirer.$WorkerUsingIntermediaryPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WorkerUsingIntermediaryPage, 1,false)
          new CheckYourAnswersHelper(cacheMap).workerUsingIntermediary(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"$Worker.$WorkerUsingIntermediaryPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"$Worker.$WorkerUsingIntermediaryPage.changeLinkContext")
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
            new CheckYourAnswersHelper(cacheMap).workerKnown(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).workerKnown(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).workerKnown(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).multipleContracts(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).multipleContracts(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).multipleContracts(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).permissionToWorkWithOthers(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).permissionToWorkWithOthers(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).permissionToWorkWithOthers(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).ownershipRights(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).ownershipRights(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).ownershipRights(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).rightsOfWork(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).rightsOfWork(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).rightsOfWork(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).transferOfRights(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).transferOfRights(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).transferOfRights(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).previousContract(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).previousContract(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).previousContract(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).followOnContract(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).followOnContract(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).followOnContract(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).firstContract(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).firstContract(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).firstContract(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).extendContract(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).extendContract(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).extendContract(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).majorityOfWorkingTime(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).majorityOfWorkingTime(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).majorityOfWorkingTime(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).similarWorkOtherClients(messages, workerFakeRequest, frontendAppConfig) mustBe
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
            new CheckYourAnswersHelper(cacheMap).similarWorkOtherClients(messages, hirerFakeRequest, frontendAppConfig) mustBe
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
          new CheckYourAnswersHelper(cacheMap).similarWorkOtherClients(messages, workerFakeRequest, frontendAppConfig) mustBe
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
