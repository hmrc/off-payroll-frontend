/*
 * Copyright 2020 HM Revenue & Customs
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

package services

import config.FrontendAppConfig
import javax.inject.Inject
import models._
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.setup._
import play.api.Logger

class CheckYourAnswersValidationService @Inject()(implicit val appConfig: FrontendAppConfig) extends CheckYourAnswersValidationServiceHelper {

  def isValid(implicit userAnswers: UserAnswers): Either[Set[QuestionPage[_]], Boolean] = {
    lazy val invalidPages = mandatoryPages.map(page =>
      (page, userAnswers.cacheMap.data.exists(_._1 == page.toString))
    ).collect {
      case (missingPage, false) => {
        Logger.warn(s"[CheckYourAnswersValidationService][isValid] Missing Answers: $missingPage")
        missingPage
      }
    }

    if (invalidPages.nonEmpty) Left(invalidPages) else Right(true)
  }

  private def mandatoryPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] =
    mandatorySetupPages ++
      mandatoryPersonalServicePages ++
      Set(

        //Early Exit
        OfficeHolderPage,

        //Control
        ChooseWhereWorkPage,
        HowWorkIsDonePage,
        MoveWorkerPage,
        ScheduleOfWorkingHoursPage,

        //Financial Risk
        EquipmentExpensesPage,
        MaterialsPage,
        OtherExpensesPage,
        VehiclePage,
        HowWorkerIsPaidPage,
        PutRightAtOwnCostPage,

        //Part and Parcel
        BenefitsPage,
        IdentifyToStakeholdersPage,
        LineManagerDutiesPage
      ) ++ mandatoryBusinessOnOwnAccountPages

  private def mandatorySetupPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] =
    intermediaryPage ++
      whatDoYouWantToDoPage ++
      Set(
        WhatDoYouWantToFindOutPage,
        WhoAreYouPage,
        ContractStartedPage
      )

  private def mandatoryPersonalServicePages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] =
    arrangedRejectedPages ++
      didPayRejectedNeededPages ++
      contractNeededPages ++
      contractNeededWouldPages ++
      neededPages

  private def mandatoryBusinessOnOwnAccountPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    workerKnownPage ++
      (if (userAnswers.get(WorkerKnownPage).fold(true)(x => x)) {
        permissionToWorkWithOtherClientsPage ++
          transferRightsPage ++
          rightsOfWorkPage ++
          firstContractPage ++
          extendContractPage ++
          followOnContractPage ++
          Set(
            MultipleContractsPage,
            SimilarWorkOtherClientsPage,
            OwnershipRightsPage,
            PreviousContractPage,
            MajorityOfWorkingTimePage
          )
      } else {
        Set()
      })
  }
}
