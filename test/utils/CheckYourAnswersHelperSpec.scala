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
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, true)
            new CheckYourAnswersHelper(cacheMap).officeHolder(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"worker.$OfficeHolderPage.changeLinkContext")
              ))
          }
        }

        "if the user type is Hirer" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, true)
            new CheckYourAnswersHelper(cacheMap).officeHolder(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"hirer.$OfficeHolderPage.changeLinkContext")
              ))
          }
        }

        "if the user type is agency" should {

          "Return correctly formatted Worker answer row" in {
            val cacheMap = UserAnswers("id").set(OfficeHolderPage, true)
            new CheckYourAnswersHelper(cacheMap).officeHolder(messages, agencyFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$OfficeHolderPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"worker.$OfficeHolderPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(OfficeHolderPage,false)
          new CheckYourAnswersHelper(cacheMap).officeHolder(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$OfficeHolderPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$OfficeHolderPage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, YesClientAgreed)
          new CheckYourAnswersHelper(cacheMap).arrangedSubstitute(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"worker.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(ArrangedSubstitutePage).url),
              changeContextMsgKey = Some(s"worker.$ArrangedSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, YesClientAgreed)
          new CheckYourAnswersHelper(cacheMap).arrangedSubstitute(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"hirer.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"hirer.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(ArrangedSubstitutePage).url),
              changeContextMsgKey = Some(s"hirer.$ArrangedSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ArrangedSubstitutePage, YesClientAgreed)
          new CheckYourAnswersHelper(cacheMap).arrangedSubstitute(messages, agencyFakeDataRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$ArrangedSubstitutePage.checkYourAnswersLabel",
              answer = s"worker.$ArrangedSubstitutePage.$YesClientAgreed",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(ArrangedSubstitutePage).url),
              changeContextMsgKey = Some(s"worker.$ArrangedSubstitutePage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(BenefitsPage, true)
            new CheckYourAnswersHelper(cacheMap).benefits(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"worker.$BenefitsPage.changeLinkContext")
              ))
          }
        }

        "if the user type is Hirer" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, true)
            new CheckYourAnswersHelper(cacheMap).benefits(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"hirer.$BenefitsPage.changeLinkContext")
              ))
          }
        }

        "if the user type is agency" should {

          "Return correctly formatted Hirer answer row" in {
            val cacheMap = UserAnswers("id").set(BenefitsPage, true)
            new CheckYourAnswersHelper(cacheMap).benefits(messages, agencyFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$BenefitsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
                changeContextMsgKey = Some(s"worker.$BenefitsPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(BenefitsPage,false)
          new CheckYourAnswersHelper(cacheMap).benefits(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$BenefitsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$BenefitsPage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, WorkerChooses)
          new CheckYourAnswersHelper(cacheMap).chooseWhereWork(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"worker.$ChooseWhereWorkPage.workerChooses",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$ChooseWhereWorkPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, WorkerChooses)
          new CheckYourAnswersHelper(cacheMap).chooseWhereWork(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"hirer.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"hirer.$ChooseWhereWorkPage.workerChooses",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"hirer.$ChooseWhereWorkPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ChooseWhereWorkPage, WorkerChooses)
          new CheckYourAnswersHelper(cacheMap).chooseWhereWork(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$ChooseWhereWorkPage.checkYourAnswersLabel",
              answer = s"worker.$ChooseWhereWorkPage.workerChooses",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$ChooseWhereWorkPage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, true)
          new CheckYourAnswersHelper(cacheMap).didPaySubstitute(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(DidPaySubstitutePage).url),
              changeContextMsgKey = Some(s"worker.$DidPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, true)
          new CheckYourAnswersHelper(cacheMap).didPaySubstitute(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"hirer.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(DidPaySubstitutePage).url),
              changeContextMsgKey = Some(s"hirer.$DidPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(DidPaySubstitutePage, true)
          new CheckYourAnswersHelper(cacheMap).didPaySubstitute(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$DidPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(DidPaySubstitutePage).url),
              changeContextMsgKey = Some(s"worker.$DidPaySubstitutePage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, Commission)
          new CheckYourAnswersHelper(cacheMap).howWorkerIsPaid(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"worker.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = Some(financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$HowWorkerIsPaidPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, Commission)
          new CheckYourAnswersHelper(cacheMap).howWorkerIsPaid(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"hirer.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"hirer.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = Some(financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"hirer.$HowWorkerIsPaidPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkerIsPaidPage, Commission)
          new CheckYourAnswersHelper(cacheMap).howWorkerIsPaid(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$HowWorkerIsPaidPage.checkYourAnswersLabel",
              answer = s"worker.$HowWorkerIsPaidPage.$Commission",
              answerIsMessageKey = true,
              changeUrl = Some(financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$HowWorkerIsPaidPage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, NoWorkerInputAllowed)
          new CheckYourAnswersHelper(cacheMap).howWorkIsDone(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"worker.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$HowWorkIsDonePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, NoWorkerInputAllowed)
          new CheckYourAnswersHelper(cacheMap).howWorkIsDone(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"hirer.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"hirer.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"hirer.$HowWorkIsDonePage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(HowWorkIsDonePage, NoWorkerInputAllowed)
          new CheckYourAnswersHelper(cacheMap).howWorkIsDone(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$HowWorkIsDonePage.checkYourAnswersLabel",
              answer = s"worker.$HowWorkIsDonePage.$NoWorkerInputAllowed",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$HowWorkIsDonePage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, WorkForEndClient)
          new CheckYourAnswersHelper(cacheMap).identifyToStakeholders(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"worker.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$IdentifyToStakeholdersPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, WorkForEndClient)
          new CheckYourAnswersHelper(cacheMap).identifyToStakeholders(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"hirer.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"hirer.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"hirer.$IdentifyToStakeholdersPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(IdentifyToStakeholdersPage, WorkForEndClient)
          new CheckYourAnswersHelper(cacheMap).identifyToStakeholders(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$IdentifyToStakeholdersPage.checkYourAnswersLabel",
              answer = s"worker.$IdentifyToStakeholdersPage.$WorkForEndClient",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$IdentifyToStakeholdersPage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, true)
          new CheckYourAnswersHelper(cacheMap).lineManagerDuties(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$LineManagerDutiesPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, true)
          new CheckYourAnswersHelper(cacheMap).lineManagerDuties(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"hirer.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"hirer.$LineManagerDutiesPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(LineManagerDutiesPage, true)
          new CheckYourAnswersHelper(cacheMap).lineManagerDuties(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$LineManagerDutiesPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$LineManagerDutiesPage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, CanMoveWorkerWithPermission)
          new CheckYourAnswersHelper(cacheMap).moveWorker(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"worker.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$MoveWorkerPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, CanMoveWorkerWithPermission)
          new CheckYourAnswersHelper(cacheMap).moveWorker(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"hirer.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"hirer.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"hirer.$MoveWorkerPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MoveWorkerPage, CanMoveWorkerWithPermission)
          new CheckYourAnswersHelper(cacheMap).moveWorker(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$MoveWorkerPage.checkYourAnswersLabel",
              answer = s"worker.$MoveWorkerPage.$CanMoveWorkerWithPermission",
              answerIsMessageKey = true,
              changeUrl = Some(controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url),
              changeContextMsgKey = Some(s"worker.$MoveWorkerPage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, true)
          new CheckYourAnswersHelper(cacheMap).neededToPayHelper(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(NeededToPayHelperPage).url),
              changeContextMsgKey = Some(s"worker.$NeededToPayHelperPage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, true)
          new CheckYourAnswersHelper(cacheMap).neededToPayHelper(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"hirer.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(NeededToPayHelperPage).url),
              changeContextMsgKey = Some(s"hirer.$NeededToPayHelperPage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(NeededToPayHelperPage, true)
          new CheckYourAnswersHelper(cacheMap).neededToPayHelper(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$NeededToPayHelperPage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(NeededToPayHelperPage).url),
              changeContextMsgKey = Some(s"worker.$NeededToPayHelperPage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, true)
          new CheckYourAnswersHelper(cacheMap).rejectSubstitute(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(RejectSubstitutePage).url),
              changeContextMsgKey = Some(s"worker.$RejectSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, true)
          new CheckYourAnswersHelper(cacheMap).rejectSubstitute(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"hirer.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(RejectSubstitutePage).url),
              changeContextMsgKey = Some(s"hirer.$RejectSubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RejectSubstitutePage, true)
          new CheckYourAnswersHelper(cacheMap).rejectSubstitute(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$RejectSubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(RejectSubstitutePage).url),
              changeContextMsgKey = Some(s"worker.$RejectSubstitutePage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, true)
          new CheckYourAnswersHelper(cacheMap).wouldWorkerPaySubstitute(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(WouldWorkerPaySubstitutePage).url),
              changeContextMsgKey = Some(s"worker.$WouldWorkerPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type hirer" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, true)
          new CheckYourAnswersHelper(cacheMap).wouldWorkerPaySubstitute(messages, hirerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"hirer.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(WouldWorkerPaySubstitutePage).url),
              changeContextMsgKey = Some(s"hirer.$WouldWorkerPaySubstitutePage.changeLinkContext")
            ))
        }
      }

      "the user is of type agency" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WouldWorkerPaySubstitutePage, true)
          new CheckYourAnswersHelper(cacheMap).wouldWorkerPaySubstitute(messages, agencyFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$WouldWorkerPaySubstitutePage.checkYourAnswersLabel",
              answer = "site.yes",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.PersonalServiceSectionChangeWarningController.onPageLoad(WouldWorkerPaySubstitutePage).url),
              changeContextMsgKey = Some(s"worker.$WouldWorkerPaySubstitutePage.changeLinkContext")
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
          val cacheMap = UserAnswers("id").set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
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
          val cacheMap = UserAnswers("id").set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
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
          val cacheMap = UserAnswers("id").set(WhatDoYouWantToDoPage, MakeNewDetermination)
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
          val cacheMap = UserAnswers("id").set(WhatDoYouWantToDoPage, CheckDetermination)
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
            val cacheMap = UserAnswers("id").set(WhoAreYouPage, WhoAreYou.Worker)
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
          val cacheMap = UserAnswers("id").set(WhoAreYouPage, WhoAreYou.Hirer)
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
          val cacheMap = UserAnswers("id").set(WhoAreYouPage, WhoAreYou.Agency)
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
            val cacheMap = UserAnswers("id").set(ContractStartedPage, true)
            new CheckYourAnswersHelper(cacheMap).contractStarted(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"worker.$ContractStartedPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ContractStartedPage, true)
            new CheckYourAnswersHelper(cacheMap).contractStarted(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$ContractStartedPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"hirer.$ContractStartedPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ContractStartedPage,false)
          new CheckYourAnswersHelper(cacheMap).contractStarted(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$ContractStartedPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"worker.$ContractStartedPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(WorkerUsingIntermediaryPage, true)
            new CheckYourAnswersHelper(cacheMap).workerUsingIntermediary(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$WorkerUsingIntermediaryPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"worker.$WorkerUsingIntermediaryPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(WorkerUsingIntermediaryPage, true)
            new CheckYourAnswersHelper(cacheMap).workerUsingIntermediary(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$WorkerUsingIntermediaryPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
                changeContextMsgKey = Some(s"hirer.$WorkerUsingIntermediaryPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WorkerUsingIntermediaryPage,false)
          new CheckYourAnswersHelper(cacheMap).workerUsingIntermediary(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$WorkerUsingIntermediaryPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url),
              changeContextMsgKey = Some(s"worker.$WorkerUsingIntermediaryPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(WorkerKnownPage, true)
            new CheckYourAnswersHelper(cacheMap).workerKnown(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$WorkerKnownPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(WorkerKnownPage).url),
                changeContextMsgKey = Some(s"worker.$WorkerKnownPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(WorkerKnownPage, true)
            new CheckYourAnswersHelper(cacheMap).workerKnown(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$WorkerKnownPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(WorkerKnownPage).url),
                changeContextMsgKey = Some(s"hirer.$WorkerKnownPage.changeLinkContext")
              ))
          }
        }
      }


      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(WorkerKnownPage, false)
          new CheckYourAnswersHelper(cacheMap).workerKnown(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$WorkerKnownPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(WorkerKnownPage).url),
              changeContextMsgKey = Some(s"worker.$WorkerKnownPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(MultipleContractsPage, true)
            new CheckYourAnswersHelper(cacheMap).multipleContracts(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$MultipleContractsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MultipleContractsPage).url),
                changeContextMsgKey = Some(s"worker.$MultipleContractsPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(MultipleContractsPage, true)
            new CheckYourAnswersHelper(cacheMap).multipleContracts(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$MultipleContractsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MultipleContractsPage).url),
                changeContextMsgKey = Some(s"hirer.$MultipleContractsPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MultipleContractsPage, false)
          new CheckYourAnswersHelper(cacheMap).multipleContracts(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$MultipleContractsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MultipleContractsPage).url),
              changeContextMsgKey = Some(s"worker.$MultipleContractsPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(PermissionToWorkWithOthersPage, true)
            new CheckYourAnswersHelper(cacheMap).permissionToWorkWithOthers(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$PermissionToWorkWithOthersPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PermissionToWorkWithOthersPage).url),
                changeContextMsgKey = Some(s"worker.$PermissionToWorkWithOthersPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(PermissionToWorkWithOthersPage, true)
            new CheckYourAnswersHelper(cacheMap).permissionToWorkWithOthers(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$PermissionToWorkWithOthersPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PermissionToWorkWithOthersPage).url),
                changeContextMsgKey = Some(s"hirer.$PermissionToWorkWithOthersPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(PermissionToWorkWithOthersPage, false)
          new CheckYourAnswersHelper(cacheMap).permissionToWorkWithOthers(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$PermissionToWorkWithOthersPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PermissionToWorkWithOthersPage).url),
              changeContextMsgKey = Some(s"worker.$PermissionToWorkWithOthersPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(OwnershipRightsPage, true)
            new CheckYourAnswersHelper(cacheMap).ownershipRights(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$OwnershipRightsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(OwnershipRightsPage).url),
                changeContextMsgKey = Some(s"worker.$OwnershipRightsPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(OwnershipRightsPage, true)
            new CheckYourAnswersHelper(cacheMap).ownershipRights(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$OwnershipRightsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(OwnershipRightsPage).url),
                changeContextMsgKey = Some(s"hirer.$OwnershipRightsPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(OwnershipRightsPage, false)
          new CheckYourAnswersHelper(cacheMap).ownershipRights(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$OwnershipRightsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(OwnershipRightsPage).url),
              changeContextMsgKey = Some(s"worker.$OwnershipRightsPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(RightsOfWorkPage, true)
            new CheckYourAnswersHelper(cacheMap).rightsOfWork(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$RightsOfWorkPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(RightsOfWorkPage).url),
                changeContextMsgKey = Some(s"worker.$RightsOfWorkPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(RightsOfWorkPage, true)
            new CheckYourAnswersHelper(cacheMap).rightsOfWork(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$RightsOfWorkPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(RightsOfWorkPage).url),
                changeContextMsgKey = Some(s"hirer.$RightsOfWorkPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(RightsOfWorkPage, false)
          new CheckYourAnswersHelper(cacheMap).rightsOfWork(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$RightsOfWorkPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(RightsOfWorkPage).url),
              changeContextMsgKey = Some(s"worker.$RightsOfWorkPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(TransferOfRightsPage, true)
            new CheckYourAnswersHelper(cacheMap).transferOfRights(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$TransferOfRightsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(TransferOfRightsPage).url),
                changeContextMsgKey = Some(s"worker.$TransferOfRightsPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(TransferOfRightsPage, true)
            new CheckYourAnswersHelper(cacheMap).transferOfRights(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$TransferOfRightsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(TransferOfRightsPage).url),
                changeContextMsgKey = Some(s"hirer.$TransferOfRightsPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(TransferOfRightsPage, false)
          new CheckYourAnswersHelper(cacheMap).transferOfRights(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$TransferOfRightsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(TransferOfRightsPage).url),
              changeContextMsgKey = Some(s"worker.$TransferOfRightsPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(PreviousContractPage, true)
            new CheckYourAnswersHelper(cacheMap).previousContract(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$PreviousContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PreviousContractPage).url),
                changeContextMsgKey = Some(s"worker.$PreviousContractPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(PreviousContractPage, true)
            new CheckYourAnswersHelper(cacheMap).previousContract(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$PreviousContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PreviousContractPage).url),
                changeContextMsgKey = Some(s"hirer.$PreviousContractPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(PreviousContractPage, false)
          new CheckYourAnswersHelper(cacheMap).previousContract(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$PreviousContractPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PreviousContractPage).url),
              changeContextMsgKey = Some(s"worker.$PreviousContractPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(FollowOnContractPage, true)
            new CheckYourAnswersHelper(cacheMap).followOnContract(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$FollowOnContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FollowOnContractPage).url),
                changeContextMsgKey = Some(s"worker.$FollowOnContractPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(FollowOnContractPage, true)
            new CheckYourAnswersHelper(cacheMap).followOnContract(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$FollowOnContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FollowOnContractPage).url),
                changeContextMsgKey = Some(s"hirer.$FollowOnContractPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(FollowOnContractPage, false)
          new CheckYourAnswersHelper(cacheMap).followOnContract(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$FollowOnContractPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FollowOnContractPage).url),
              changeContextMsgKey = Some(s"worker.$FollowOnContractPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(FirstContractPage, true)
            new CheckYourAnswersHelper(cacheMap).firstContract(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$FirstContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FirstContractPage).url),
                changeContextMsgKey = Some(s"worker.$FirstContractPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(FirstContractPage, true)
            new CheckYourAnswersHelper(cacheMap).firstContract(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$FirstContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FirstContractPage).url),
                changeContextMsgKey = Some(s"hirer.$FirstContractPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(FirstContractPage, false)
          new CheckYourAnswersHelper(cacheMap).firstContract(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$FirstContractPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FirstContractPage).url),
              changeContextMsgKey = Some(s"worker.$FirstContractPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(ExtendContractPage, true)
            new CheckYourAnswersHelper(cacheMap).extendContract(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$ExtendContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(ExtendContractPage).url),
                changeContextMsgKey = Some(s"worker.$ExtendContractPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(ExtendContractPage, true)
            new CheckYourAnswersHelper(cacheMap).extendContract(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$ExtendContractPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(ExtendContractPage).url),
                changeContextMsgKey = Some(s"hirer.$ExtendContractPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(ExtendContractPage, false)
          new CheckYourAnswersHelper(cacheMap).extendContract(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$ExtendContractPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(ExtendContractPage).url),
              changeContextMsgKey = Some(s"worker.$ExtendContractPage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(MajorityOfWorkingTimePage, true)
            new CheckYourAnswersHelper(cacheMap).majorityOfWorkingTime(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$MajorityOfWorkingTimePage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MajorityOfWorkingTimePage).url),
                changeContextMsgKey = Some(s"worker.$MajorityOfWorkingTimePage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(MajorityOfWorkingTimePage, true)
            new CheckYourAnswersHelper(cacheMap).majorityOfWorkingTime(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$MajorityOfWorkingTimePage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MajorityOfWorkingTimePage).url),
                changeContextMsgKey = Some(s"hirer.$MajorityOfWorkingTimePage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(MajorityOfWorkingTimePage, false)
          new CheckYourAnswersHelper(cacheMap).majorityOfWorkingTime(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$MajorityOfWorkingTimePage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MajorityOfWorkingTimePage).url),
              changeContextMsgKey = Some(s"worker.$MajorityOfWorkingTimePage.changeLinkContext")
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
            val cacheMap = UserAnswers("id").set(SimilarWorkOtherClientsPage, true)
            new CheckYourAnswersHelper(cacheMap).similarWorkOtherClients(messages, workerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"worker.$SimilarWorkOtherClientsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(SimilarWorkOtherClientsPage).url),
                changeContextMsgKey = Some(s"worker.$SimilarWorkOtherClientsPage.changeLinkContext")
              ))
          }
        }

        "the user type is of Hirer" should {

          "Return correctly formatted answer row" in {
            val cacheMap = UserAnswers("id").set(SimilarWorkOtherClientsPage, true)
            new CheckYourAnswersHelper(cacheMap).similarWorkOtherClients(messages, hirerFakeRequest, frontendAppConfig) mustBe
              Some(AnswerRow(
                label = s"hirer.$SimilarWorkOtherClientsPage.checkYourAnswersLabel",
                answer = "site.yes",
                answerIsMessageKey = true,
                changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(SimilarWorkOtherClientsPage).url),
                changeContextMsgKey = Some(s"hirer.$SimilarWorkOtherClientsPage.changeLinkContext")
              ))
          }
        }
      }

      "the answer is no" should {

        "Return correctly formatted answer row" in {
          val cacheMap = UserAnswers("id").set(SimilarWorkOtherClientsPage, false)
          new CheckYourAnswersHelper(cacheMap).similarWorkOtherClients(messages, workerFakeRequest, frontendAppConfig) mustBe
            Some(AnswerRow(
              label = s"worker.$SimilarWorkOtherClientsPage.checkYourAnswersLabel",
              answer = "site.no",
              answerIsMessageKey = true,
              changeUrl = Some(routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(SimilarWorkOtherClientsPage).url),
              changeContextMsgKey = Some(s"worker.$SimilarWorkOtherClientsPage.changeLinkContext")
            ))
        }
      }
    }
  }
}
