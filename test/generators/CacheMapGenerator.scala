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

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{AboutYouPage, ContractStartedPage, WorkerTypePage}
import play.api.libs.json.JsValue
import uk.gov.hmrc.http.cache.client.CacheMap

trait CacheMapGenerator {
  self: Generators =>

  val generators: Seq[Gen[(Page, JsValue)]] =
    arbitrary[(WorkerKnownPage.type, JsValue)] ::
    arbitrary[(RightsOfWorkPage.type, JsValue)] ::
    arbitrary[(FinanciallyDependentPage.type, JsValue)] ::
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
    arbitrary[(EmployeesOverPage.type, JsValue)] ::
    arbitrary[(BalanceSheetOverPage.type, JsValue)] ::
    arbitrary[(TurnoverOverPage.type, JsValue)] ::
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
    arbitrary[(InteractWithStakeholdersPage.type, JsValue)] ::
    arbitrary[(IdentifyToStakeholdersPage.type, JsValue)] ::
    arbitrary[(ArrangedSubstitutePage.type, JsValue)] ::
    arbitrary[(CannotClaimAsExpensePage.type, JsValue)] ::
    arbitrary[(OfficeHolderPage.type, JsValue)] ::
    arbitrary[(WorkerTypePage.type, JsValue)] ::
    arbitrary[(ContractStartedPage.type, JsValue)] ::
    arbitrary[(AboutYouPage.type, JsValue)] ::
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
