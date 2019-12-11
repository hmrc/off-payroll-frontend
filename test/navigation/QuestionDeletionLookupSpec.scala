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

import base.GuiceAppSpecBase
import models._
import models.sections.personalService.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import models.sections.setup.{WhatDoYouWantToDo, WhatDoYouWantToFindOut, WhoAreYou}
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._

class QuestionDeletionLookupSpec extends GuiceAppSpecBase {

  val navigator = new QuestionDeletionLookup
  val userAnswers = UserAnswers("id")

  "QuestionDeletionLookup" when {

    "Office holder" must {
      "remove all further questions" in {
        val res = navigator.getPagesToRemove(OfficeHolderPage)(
          userAnswers.set(OfficeHolderPage, false))

        res mustBe List(
          ArrangedSubstitutePage, WouldWorkerPaySubstitutePage, RejectSubstitutePage, DidPaySubstitutePage, NeededToPayHelperPage,
          //Control
          ChooseWhereWorkPage, MoveWorkerPage, ScheduleOfWorkingHoursPage, HowWorkIsDonePage,
          //Financial Risk
          EquipmentExpensesPage, HowWorkerIsPaidPage, MaterialsPage, OtherExpensesPage, PutRightAtOwnCostPage, VehiclePage,
          //Part Parcel
          BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage,
          //Business On Own Account
          WorkerKnownPage, MultipleContractsPage, PermissionToWorkWithOthersPage, OwnershipRightsPage, RightsOfWorkPage, TransferOfRightsPage,
          PreviousContractPage, FollowOnContractPage, FirstContractPage, ExtendContractPage, MajorityOfWorkingTimePage, SimilarWorkOtherClientsPage
        )
      }
    }

    "Setup" must {

      "handle WhatDoYouWantToFindOutPage" when {

        "answer is anything, return expected pages" in {

          val res = navigator.getPagesToRemove(WhatDoYouWantToFindOutPage)(
            userAnswers.set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35))

          res mustBe List(WhoAreYouPage,WorkerUsingIntermediaryPage,WhatDoYouWantToDoPage)
        }
      }

      "handle WhoAreYouPage" when {

        "answer is Agent, return expected pages" in {

          val res = navigator.getPagesToRemove(WhoAreYouPage)(
            userAnswers.set(WhoAreYouPage, WhoAreYou.Agency))

          res mustBe List(WhatDoYouWantToDoPage,WorkerUsingIntermediaryPage)
        }

        "answer is Client, return expected pages" in {

          val res = navigator.getPagesToRemove(WhoAreYouPage)(
            userAnswers.set(WhoAreYouPage, WhoAreYou.Hirer))

          res mustBe List(WhatDoYouWantToDoPage)
        }

        "answer is Worker and PAYE, return expected pages" in {

          val res = navigator.getPagesToRemove(WhoAreYouPage)(
            userAnswers
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage,WhatDoYouWantToFindOut.PAYE)
          )

          res mustBe List(WhatDoYouWantToDoPage)
        }


        "answer is anything else, return no pages" in {

          val res = navigator.getPagesToRemove(WorkerUsingIntermediaryPage)(
            userAnswers.set(WhoAreYouPage, WhoAreYou.Worker))

          res mustBe List()
        }
      }

      "handle WhatDoYouWantToDoPage" when {

        "answer is Check, return expected pages" in {

          val res = navigator.getPagesToRemove(WhatDoYouWantToDoPage)(
            userAnswers.set(WhatDoYouWantToDoPage, WhatDoYouWantToDo.CheckDetermination))

          res mustBe List(WorkerUsingIntermediaryPage)
        }

        "answer is Make, return no pages" in {

          val res = navigator.getPagesToRemove(WhatDoYouWantToDoPage)(
            userAnswers.set(WhatDoYouWantToDoPage, WhatDoYouWantToDo.MakeNewDetermination))

          res mustBe List()
        }
      }

      "handle ContractStartedPage" when {

        "return expected pages" in {

          val res = navigator.getPagesToRemove(ContractStartedPage)(userAnswers)

          res mustBe List(ArrangedSubstitutePage, WouldWorkerPaySubstitutePage, RejectSubstitutePage, DidPaySubstitutePage, NeededToPayHelperPage, WorkerKnownPage, MultipleContractsPage, PermissionToWorkWithOthersPage, OwnershipRightsPage, RightsOfWorkPage, TransferOfRightsPage,
            PreviousContractPage, FollowOnContractPage, FirstContractPage, ExtendContractPage, MajorityOfWorkingTimePage, SimilarWorkOtherClientsPage)
        }
      }
    }

    "Personal Service" must {

      "handle ArrangedSubstitutePage" when {

        "answer is no, return expected pages" in {

          val res = navigator.getPagesToRemove(ArrangedSubstitutePage)(
            userAnswers.set(ArrangedSubstitutePage,No))

          res mustBe List(DidPaySubstitutePage, NeededToPayHelperPage)
        }

        "answer is yes client agreed, return expected pages" in {

          val res = navigator.getPagesToRemove(ArrangedSubstitutePage)(
            userAnswers.set(ArrangedSubstitutePage,YesClientAgreed))

          res mustBe List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, NeededToPayHelperPage)
        }

        "answer is yes client not agreed, return expected pages" in {

          val res = navigator.getPagesToRemove(ArrangedSubstitutePage)(
            userAnswers.set(ArrangedSubstitutePage,YesClientNotAgreed))

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
            userAnswers.set(DidPaySubstitutePage,true))

          res mustBe List(NeededToPayHelperPage)
        }

        "answer is no, return empty list" in {

          val res = navigator.getPagesToRemove(DidPaySubstitutePage)(
            userAnswers.set(DidPaySubstitutePage,false))

          res mustBe List.empty
        }
      }

      "handle RejectSubstitutePage" when {

        "answer is yes, return expected pages" in {

          val res = navigator.getPagesToRemove(RejectSubstitutePage)(
            userAnswers.set(RejectSubstitutePage,true))

          res mustBe List(WouldWorkerPaySubstitutePage)
        }

        "answer is no, return empty list" in {

          val res = navigator.getPagesToRemove(RejectSubstitutePage)(
            userAnswers.set(RejectSubstitutePage,false))

          res mustBe List(NeededToPayHelperPage)
        }
      }

      "handle WouldWorkerPaySubstitutePage" when {

        "answer is yes, return expected pages" in {

          val res = navigator.getPagesToRemove(WouldWorkerPaySubstitutePage)(
            userAnswers.set(WouldWorkerPaySubstitutePage,true))

          res mustBe List(NeededToPayHelperPage)
        }

        "answer is no, return empty list" in {

          val res = navigator.getPagesToRemove(WouldWorkerPaySubstitutePage)(
            userAnswers.set(WouldWorkerPaySubstitutePage,false))

          res mustBe List.empty
        }
      }
    }

    "BoOA section" must {

      "handle WorkerKnownPage" when {

        "answer is false, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(WorkerKnownPage, false)

          val res = navigator.getPagesToRemove(WorkerKnownPage)(userAnswers)

          res mustBe List(MultipleContractsPage, PermissionToWorkWithOthersPage, OwnershipRightsPage, RightsOfWorkPage, TransferOfRightsPage,
            PreviousContractPage, FollowOnContractPage, FirstContractPage, ExtendContractPage, MajorityOfWorkingTimePage, SimilarWorkOtherClientsPage)
        }

        "answer is true, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(WorkerKnownPage, true)

          val res = navigator.getPagesToRemove(WorkerKnownPage)(userAnswers)

          res mustBe List.empty
        }
      }

      "handle MultipleContractsPage" when {

        "answer is true, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(MultipleContractsPage, true)

          val res = navigator.getPagesToRemove(MultipleContractsPage)(userAnswers)

          res mustBe List(PermissionToWorkWithOthersPage)
        }

        "answer is false, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(MultipleContractsPage, false)


          val res = navigator.getPagesToRemove(MultipleContractsPage)(userAnswers)

          res mustBe List.empty
        }
      }

      "handle OwnershipRightsPage" when {

        "answer is false, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(OwnershipRightsPage, false)

          val res = navigator.getPagesToRemove(OwnershipRightsPage)(userAnswers)

          res mustBe List(RightsOfWorkPage, TransferOfRightsPage)
        }

        "answer is true, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(OwnershipRightsPage, true)


          val res = navigator.getPagesToRemove(OwnershipRightsPage)(userAnswers)

          res mustBe List.empty
        }
      }

      "handle RightsOfWorkPage" when {

        "answer is true, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(RightsOfWorkPage, true)

          val res = navigator.getPagesToRemove(RightsOfWorkPage)(userAnswers)

          res mustBe List(TransferOfRightsPage)
        }

        "answer is false, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(RightsOfWorkPage, false)


          val res = navigator.getPagesToRemove(RightsOfWorkPage)(userAnswers)

          res mustBe List.empty
        }
      }

      "handle PreviousContractPage" when {

        "answer is false, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(PreviousContractPage, false)

          val res = navigator.getPagesToRemove(PreviousContractPage)(userAnswers)

          res mustBe List(FollowOnContractPage)
        }

        "answer is true, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(PreviousContractPage, true)


          val res = navigator.getPagesToRemove(PreviousContractPage)(userAnswers)

          res mustBe List.empty
        }
      }

      "handle FollowOnContractPage" when {

        "answer is true, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(FollowOnContractPage, true)

          val res = navigator.getPagesToRemove(FollowOnContractPage)(userAnswers)

          res mustBe List(ExtendContractPage)
        }

        "answer is false, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(FollowOnContractPage, false)


          val res = navigator.getPagesToRemove(FollowOnContractPage)(userAnswers)

          res mustBe List.empty
        }
      }

      "handle FirstContractPage" when {

        "answer is true, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(FirstContractPage, true)

          val res = navigator.getPagesToRemove(FirstContractPage)(userAnswers)

          res mustBe List(ExtendContractPage)
        }

        "answer is false, return expected pages" in {

          val userAnswers = UserAnswers("id")
            .set(FirstContractPage, false)


          val res = navigator.getPagesToRemove(FirstContractPage)(userAnswers)

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
