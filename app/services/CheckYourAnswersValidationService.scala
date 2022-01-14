/*
 * Copyright 2022 HM Revenue & Customs
 *
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
import play.api.Logging

class CheckYourAnswersValidationService @Inject()(implicit val appConfig: FrontendAppConfig) extends CheckYourAnswersValidationServiceHelper with Logging {

  def isValid(implicit userAnswers: UserAnswers): Either[Set[QuestionPage[_]], Boolean] = {
    lazy val invalidPages = mandatoryPages.map(page =>
      (page, userAnswers.cacheMap.data.exists(_._1 == page.toString))
    ).collect {
      case (missingPage, false) => {
        logger.warn(s"[CheckYourAnswersValidationService][isValid] Missing Answers: $missingPage")
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
