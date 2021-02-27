/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package generators

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{ContractStartedPage, WhatDoYouWantToFindOutPage}
import play.api.libs.json.JsValue
import uk.gov.hmrc.http.cache.client.CacheMap

trait CacheMapGenerator {
  self: Generators =>

  val generators: Seq[Gen[(Page, JsValue)]] =
    arbitrary[(SimilarWorkOtherClientsPage.type, JsValue)] ::
    arbitrary[(OwnershipRightsPage.type, JsValue)] ::
    arbitrary[(WorkerKnownPage.type, JsValue)] ::
    arbitrary[(RightsOfWorkPage.type, JsValue)] ::
    arbitrary[(ExtendContractPage.type, JsValue)] ::
    arbitrary[(MajorityOfWorkingTimePage.type, JsValue)] ::
    arbitrary[(FollowOnContractPage.type, JsValue)] ::
    arbitrary[(PreviousContractPage.type, JsValue)] ::
    arbitrary[(PermissionToWorkWithOthersPage.type, JsValue)] ::
    arbitrary[(MultipleContractsPage.type, JsValue)] ::
    arbitrary[(FirstContractPage.type, JsValue)] ::
    arbitrary[(TransferOfRightsPage.type, JsValue)] ::
    arbitrary[(EquipmentExpensesPage.type, JsValue)] ::
    arbitrary[(OtherExpensesPage.type, JsValue)] ::
    arbitrary[(VehiclePage.type, JsValue)] ::
    arbitrary[(MaterialsPage.type, JsValue)] ::
    arbitrary[(CustomisePDFPage.type, JsValue)] ::
    arbitrary[(DidPaySubstitutePage.type, JsValue)] ::
    arbitrary[(RejectSubstitutePage.type, JsValue)] ::
    arbitrary[(WouldWorkerPaySubstitutePage.type, JsValue)] ::
    arbitrary[(NeededToPayHelperPage.type, JsValue)] ::
    arbitrary[(MoveWorkerPage.type, JsValue)] ::
    arbitrary[(HowWorkIsDonePage.type, JsValue)] ::
    arbitrary[(ScheduleOfWorkingHoursPage.type, JsValue)] ::
    arbitrary[(ChooseWhereWorkPage.type, JsValue)] ::
    arbitrary[(HowWorkerIsPaidPage.type, JsValue)] ::
    arbitrary[(PutRightAtOwnCostPage.type, JsValue)] ::
    arbitrary[(BenefitsPage.type, JsValue)] ::
    arbitrary[(LineManagerDutiesPage.type, JsValue)] ::
    arbitrary[(IdentifyToStakeholdersPage.type, JsValue)] ::
    arbitrary[(ArrangedSubstitutePage.type, JsValue)] ::
    arbitrary[(OfficeHolderPage.type, JsValue)] ::
    arbitrary[(ContractStartedPage.type, JsValue)] ::
    arbitrary[(WhatDoYouWantToFindOutPage.type, JsValue)] ::
  Nil

  implicit lazy val arbitraryCacheMap: Arbitrary[CacheMap] =
    Arbitrary {
      for {
        cacheId <- nonEmptyString
        data    <- generators match {
          case Nil => Gen.const(Map[Page, JsValue]())
          case _   => Gen.mapOf(oneOf(generators))
        }
      } yield CacheMap(
        cacheId,
        data.map {
          case (k, v) => ( k.toString, v )
        }
      )
    }
}
