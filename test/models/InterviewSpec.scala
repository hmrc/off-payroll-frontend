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

package models

import base.SpecBase
import models.AboutYouAnswer.Worker
import models.ArrangedSubstitue.YesClientAgreed
import models.ChooseWhereWork.Workeragreewithothers
import models.HowWorkIsDone.Workerfollowstrictemployeeprocedures
import models.HowWorkerIsPaid.Commission
import models.IdentifyToStakeholders.WorkAsIndependent
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.CannotBeCorrected
import models.ScheduleOfWorkingHours.Workeragreeschedule
import models.WorkerType.SoleTrader
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class InterviewSpec extends SpecBase {

  "Interview" must {

    "serialise to JSON correctly" when {

      "the maximum model is supplied" in {

        val model = Interview(
          correlationId = "id",
          endUserRole = Some(Worker),
          hasContractStarted = Some(true),
          provideServices = Some(SoleTrader),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(Workerfollowstrictemployeeprocedures),
          whenWorkHasToBeDone =Some(Workeragreeschedule),
          workerDecideWhere =Some(Workeragreewithothers),
          workerProvidedMaterials =Some(false),
          workerProvidedEquipment =Some(false),
          workerUsedVehicle =Some(true),
          workerHadOtherExpenses =Some(true),
          expensesAreNotRelevantForRole =Some(false),
          workerMainIncome =Some(Commission),
          paidForSubstandardWork =Some(CannotBeCorrected),
          workerReceivesBenefits =Some(false),
          workerAsLineManager =Some(false),
          contactWithEngagerCustomer =Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        )

        val expected = Json.obj(
          "version"-> "1.5.0-final",
          "correlationID"-> "12345",
          "interview"-> Json.obj(
            "setup"-> Json.obj(
            "endUserRole"-> "personDoingWork",
            "hasContractStarted"-> "No",
            "provideServices"-> "soleTrader"
          ),
            "exit"-> Json.obj(
            "officeHolder"-> "No"
          ),
            "personalService"-> Json.obj(
            "workerSentActualSubstitute"-> "yesClientAgreed",
            "workerPayActualSubstitute"-> "No",
            "possibleSubstituteRejection"-> "wouldReject",
            "possibleSubstituteWorkerPay"-> "Yes",
            "wouldWorkerPayHelper"-> "Yes"
          ),
            "control"-> Json.obj(
            "engagerMovingWorker"-> "canMoveWorkerWithPermission",
            "workerDecidingHowWorkIsDone"-> "workerAgreeWithOthers",
            "whenWorkHasToBeDone"-> "workerAgreeSchedule",
            "workerDecideWhere"-> "workerChooses"
          ),
            "financialRisk"-> Json.obj(
            "workerProvidedMaterials"-> "No",
            "workerProvidedEquipment"-> "No",
            "workerUsedVehicle"-> "No",
            "workerHadOtherExpenses"-> "No",
            "expensesAreNotRelevantForRole"-> "No",
            "workerMainIncome"-> "incomeCalendarPeriods",
            "paidForSubstandardWork"-> "asPartOfUsualRateInWorkingHours"
          ),
            "partAndParcel"-> Json.obj(
            "workerReceivesBenefits"-> "Yes",
            "workerAsLineManager"-> "Yes",
            "contactWithEngagerCustomer"-> "No",
            "workerRepresentsEngagerBusiness"-> "workAsIndependent"
          )
          )
        )

        val actual = Json.toJson(model)

        actual mustBe expected
      }
    }
  }
}
