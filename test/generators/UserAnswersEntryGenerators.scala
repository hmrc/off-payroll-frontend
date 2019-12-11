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

package generators

import models.sections.control.{ChooseWhereWork, HowWorkIsDone, MoveWorker, ScheduleOfWorkingHours}
import models.sections.financialRisk.{HowWorkerIsPaid, PutRightAtOwnCost}
import models.sections.partAndParcel.IdentifyToStakeholders
import models.sections.personalService.ArrangedSubstitute
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{ContractStartedPage, WhatDoYouWantToFindOutPage}
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {


  implicit lazy val whatDoYouWantToFindOutUserAnswersEntry: Arbitrary[(WhatDoYouWantToFindOutPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatDoYouWantToFindOutPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitrarySimilarWorkOtherClientsUserAnswersEntry: Arbitrary[(SimilarWorkOtherClientsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[SimilarWorkOtherClientsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryOwnershipRightsUserAnswersEntry: Arbitrary[(OwnershipRightsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[OwnershipRightsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWorkerKnownUserAnswersEntry: Arbitrary[(WorkerKnownPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WorkerKnownPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRightsOfWorkUserAnswersEntry: Arbitrary[(RightsOfWorkPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[RightsOfWorkPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryExtendContractUserAnswersEntry: Arbitrary[(ExtendContractPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ExtendContractPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryMajorityOfWorkingTimeUserAnswersEntry: Arbitrary[(MajorityOfWorkingTimePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[MajorityOfWorkingTimePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryFollowOnContractUserAnswersEntry: Arbitrary[(FollowOnContractPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[FollowOnContractPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPreviousContractUserAnswersEntry: Arbitrary[(PreviousContractPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PreviousContractPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPermissionToWorkWithOthersUserAnswersEntry: Arbitrary[(PermissionToWorkWithOthersPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PermissionToWorkWithOthersPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryMultipleContractsUserAnswersEntry: Arbitrary[(MultipleContractsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[MultipleContractsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryFirstContractUserAnswersEntry: Arbitrary[(FirstContractPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[FirstContractPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryTransferOfRightsUserAnswersEntry: Arbitrary[(TransferOfRightsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[TransferOfRightsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryEquipmentExpensesUserAnswersEntry: Arbitrary[(EquipmentExpensesPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[EquipmentExpensesPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryOtherExpensesUserAnswersEntry: Arbitrary[(OtherExpensesPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[OtherExpensesPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryVehicleUserAnswersEntry: Arbitrary[(VehiclePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[VehiclePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryMaterialsUserAnswersEntry: Arbitrary[(MaterialsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[MaterialsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCustomisePDFUserAnswersEntry: Arbitrary[(CustomisePDFPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CustomisePDFPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDidPaySubstituteUserAnswersEntry: Arbitrary[(DidPaySubstitutePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DidPaySubstitutePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRejectSubstituteUserAnswersEntry: Arbitrary[(RejectSubstitutePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RejectSubstitutePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWouldWorkerPaySubstituteUserAnswersEntry: Arbitrary[(WouldWorkerPaySubstitutePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WouldWorkerPaySubstitutePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNeededToPayHelperUserAnswersEntry: Arbitrary[(NeededToPayHelperPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[NeededToPayHelperPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryMoveWorkerUserAnswersEntry: Arbitrary[(MoveWorkerPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[MoveWorkerPage.type]
        value <- arbitrary[MoveWorker].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryHowWorkIsDoneUserAnswersEntry: Arbitrary[(HowWorkIsDonePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[HowWorkIsDonePage.type]
        value <- arbitrary[HowWorkIsDone].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryScheduleOfWorkingHoursUserAnswersEntry: Arbitrary[(ScheduleOfWorkingHoursPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ScheduleOfWorkingHoursPage.type]
        value <- arbitrary[ScheduleOfWorkingHours].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryChooseWhereWorkUserAnswersEntry: Arbitrary[(ChooseWhereWorkPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ChooseWhereWorkPage.type]
        value <- arbitrary[ChooseWhereWork].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryHowWorkerIsPaidUserAnswersEntry: Arbitrary[(HowWorkerIsPaidPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[HowWorkerIsPaidPage.type]
        value <- arbitrary[HowWorkerIsPaid].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPutRightAtOwnCostUserAnswersEntry: Arbitrary[(PutRightAtOwnCostPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PutRightAtOwnCostPage.type]
        value <- arbitrary[PutRightAtOwnCost].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryBenefitsUserAnswersEntry: Arbitrary[(BenefitsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[BenefitsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryLineManagerDutiesUserAnswersEntry: Arbitrary[(LineManagerDutiesPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[LineManagerDutiesPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryIdentifyToStakeholdersUserAnswersEntry: Arbitrary[(IdentifyToStakeholdersPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[IdentifyToStakeholdersPage.type]
        value <- arbitrary[IdentifyToStakeholders].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryArrangedSubstituteUserAnswersEntry: Arbitrary[(ArrangedSubstitutePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ArrangedSubstitutePage.type]
        value <- arbitrary[ArrangedSubstitute].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryOfficeHolderUserAnswersEntry: Arbitrary[(OfficeHolderPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[OfficeHolderPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryContractStartedUserAnswersEntry: Arbitrary[(ContractStartedPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ContractStartedPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }
}
