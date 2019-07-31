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

package navigation

import base.{GuiceAppSpecBase, SpecBase}
import config.featureSwitch.OptimisedFlow
import controllers.routes
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import controllers.sections.personalService.{routes => personalServiceRoutes}
import controllers.sections.setup.{routes => setupRoutes}
import models.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import models.WhichDescribesYouAnswer.{Agency, ClientIR35, ClientPAYE, WorkerIR35, WorkerPAYE, writes}
import models._
import pages._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.libs.json.Writes

class QuestionDeletionLookupSpec extends GuiceAppSpecBase {

  val navigator = new QuestionDeletionLookup
  val userAnswers = UserAnswers("id")

  "QuestionDeletionLookup" when {

    "Office holder" must {
      "remove all further questions" in {
        val res = navigator.getPagesToRemove(OfficeHolderPage)(
          userAnswers.set(OfficeHolderPage, 0, false))

        res mustBe List(
          ArrangedSubstitutePage, DidPaySubstitutePage, NeededToPayHelperPage, RejectSubstitutePage, WouldWorkerPaySubstitutePage,
          //Control
          ChooseWhereWorkPage, MoveWorkerPage, ScheduleOfWorkingHoursPage, HowWorkIsDonePage,
          //Financial Risk
          EquipmentExpensesPage, HowWorkerIsPaidPage, MaterialsPage, OtherExpensesPage, PutRightAtOwnCostPage, VehiclePage, CannotClaimAsExpensePage,
          //Part Parcel
          BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage, InteractWithStakeholdersPage
        )
      }
    }

    "Setup" must {
      "handle WorkerUsingIntermediaryPage" when {

        "answer is false, return expected pages" in {

          val res = navigator.getPagesToRemove(WorkerUsingIntermediaryPage)(
            userAnswers.set(WorkerUsingIntermediaryPage, 0, false))

          res mustBe List(IsWorkForPrivateSectorPage, TurnoverOverPage, EmployeesOverPage, BalanceSheetOverPage)
        }

        "answer is true, return expected pages" in {

          val res = navigator.getPagesToRemove(WorkerUsingIntermediaryPage)(
            userAnswers.set(WorkerUsingIntermediaryPage, 0, true))

          res mustBe List.empty
        }
      }

      "handle IsWorkForPrivateSectorPage" when {

        "answer is false, return expected pages" in {

          val res = navigator.getPagesToRemove(IsWorkForPrivateSectorPage)(
            userAnswers.set(IsWorkForPrivateSectorPage, 0, false))

          res mustBe List(TurnoverOverPage, EmployeesOverPage, BalanceSheetOverPage)
        }

        "answer is true, return expected pages" in {

          val res = navigator.getPagesToRemove(IsWorkForPrivateSectorPage)(
            userAnswers.set(IsWorkForPrivateSectorPage, 0, true))

          res mustBe List.empty
        }
      }

      "handle TurnoverOverPage" when {

        "return expected pages" in {

          val res = navigator.getPagesToRemove(TurnoverOverPage)(userAnswers)

          res mustBe List(EmployeesOverPage, BalanceSheetOverPage)
        }
      }

      "handle EmployeesOverPage" when {

        "return expected pages" in {

          val res = navigator.getPagesToRemove(EmployeesOverPage)(userAnswers)

          res mustBe List(BalanceSheetOverPage)
        }
      }

      "handle ContractStartedPage" when {

        "return expected pages" in {

          val res = navigator.getPagesToRemove(ContractStartedPage)(userAnswers)

          res mustBe List(ArrangedSubstitutePage, WouldWorkerPaySubstitutePage, RejectSubstitutePage, DidPaySubstitutePage, NeededToPayHelperPage)
        }
      }
    }

    "Personal Service" must {

      "handle ArrangedSubstitutePage" when {

        "answer is no, return expected pages" in {

          val res = navigator.getPagesToRemove(ArrangedSubstitutePage)(
            userAnswers.set(ArrangedSubstitutePage,0,No))

          res mustBe List(DidPaySubstitutePage, NeededToPayHelperPage)
        }

        "answer is yes client agreed, return expected pages" in {

          val res = navigator.getPagesToRemove(ArrangedSubstitutePage)(
            userAnswers.set(ArrangedSubstitutePage,0,YesClientAgreed))

          res mustBe List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, NeededToPayHelperPage)
        }

        "answer is yes client not agreed, return expected pages" in {

          val res = navigator.getPagesToRemove(ArrangedSubstitutePage)(
            userAnswers.set(ArrangedSubstitutePage,0,YesClientNotAgreed))

          res mustBe List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, DidPaySubstitutePage)
        }

        "answer is not there, return empty list" in {

          val res = navigator.getPagesToRemove(ArrangedSubstitutePage)(
            userAnswers)

          res mustBe List.empty
        }
      }

      "handle DidPaySubstitutePage" when {

        "answer is yes, return expected pages" in {

          val res = navigator.getPagesToRemove(DidPaySubstitutePage)(
            userAnswers.set(DidPaySubstitutePage,0,true))

          res mustBe List(NeededToPayHelperPage)
        }

        "answer is no, return empty list" in {

          val res = navigator.getPagesToRemove(DidPaySubstitutePage)(
            userAnswers.set(DidPaySubstitutePage,0,false))

          res mustBe List.empty
        }
      }

      "handle RejectSubstitutePage" when {

        "answer is yes, return expected pages" in {

          val res = navigator.getPagesToRemove(RejectSubstitutePage)(
            userAnswers.set(RejectSubstitutePage,0,true))

          res mustBe List(WouldWorkerPaySubstitutePage)
        }

        "answer is no, return empty list" in {

          val res = navigator.getPagesToRemove(RejectSubstitutePage)(
            userAnswers.set(RejectSubstitutePage,0,false))

          res mustBe List(NeededToPayHelperPage)
        }
      }

      "handle WouldWorkerPaySubstitutePage" when {

        "answer is yes, return expected pages" in {

          val res = navigator.getPagesToRemove(WouldWorkerPaySubstitutePage)(
            userAnswers.set(WouldWorkerPaySubstitutePage,0,true))

          res mustBe List(NeededToPayHelperPage)
        }

        "answer is no, return empty list" in {

          val res = navigator.getPagesToRemove(WouldWorkerPaySubstitutePage)(
            userAnswers.set(WouldWorkerPaySubstitutePage,0,false))

          res mustBe List.empty
        }
      }
    }

    "when called with a non existent page should handle an exception" in {

      val res = navigator.getPagesToRemove(IdentifyToStakeholdersPage)(userAnswers)

      res mustBe List.empty
    }
  }
}
