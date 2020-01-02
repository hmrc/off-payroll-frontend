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

package models

import base.GuiceAppSpecBase
import models.sections.control.{ChooseWhereWork, HowWorkIsDone, MoveWorker, ScheduleOfWorkingHours}
import models.sections.financialRisk.{HowWorkerIsPaid, PutRightAtOwnCost}
import models.sections.partAndParcel.IdentifyToStakeholders
import models.sections.personalService.ArrangedSubstitute
import models.sections.setup.WhatDoYouWantToDo.MakeNewDetermination
import models.sections.setup.{WhatDoYouWantToFindOut, WhoAreYou}
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json.{Json, Writes}

import scala.language.implicitConversions

class AuditJourneyStartSpec extends GuiceAppSpecBase {

  implicit def toJsonTuple[A](x: (QuestionPage[A], A))(implicit writes: Writes[A]): (String, JsValueWrapper) = x._1.toString -> Json.toJson(x._2)

  "AuditJourneyStart" must {

    "correctly write the appropriate JSON model" when {

      "all answers are provided" in {

        val userAnswers = UserAnswers("id")
          .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
          .set(WhoAreYouPage, WhoAreYou.Worker)
          .set(WhatDoYouWantToDoPage, MakeNewDetermination)
          .set(WorkerUsingIntermediaryPage, true)
          .set(ContractStartedPage, true)


        val actual = Json.toJson(AuditJourneyStart(userAnswers))

        val expected = Json.obj(
          "correlationId" -> "id",
          "setup" -> Json.obj(
            WhatDoYouWantToFindOutPage -> WhatDoYouWantToFindOut.IR35,
            WhoAreYouPage -> WhoAreYou.Worker,
            WhatDoYouWantToDoPage -> MakeNewDetermination,
            WorkerUsingIntermediaryPage -> true,
            ContractStartedPage -> true
          )
        )

        actual mustBe expected
      }

      "minimum answers are provided" in {

        val userAnswers = UserAnswers("id")
          .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
          .set(WhoAreYouPage, WhoAreYou.Hirer)
          .set(ContractStartedPage, true)

        val actual = Json.toJson(AuditJourneyStart(userAnswers))

        val expected = Json.obj(
          "correlationId" -> "id",
          "setup" -> Json.obj(
            WhatDoYouWantToFindOutPage -> WhatDoYouWantToFindOut.PAYE,
            WhoAreYouPage -> WhoAreYou.Hirer,
            ContractStartedPage -> true
          )
        )

        actual mustBe expected
      }
    }
  }
}
