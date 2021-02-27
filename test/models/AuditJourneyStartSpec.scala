/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models

import base.GuiceAppSpecBase
import models.sections.setup.WhatDoYouWantToDo.MakeNewDetermination
import models.sections.setup.{WhatDoYouWantToFindOut, WhoAreYou}
import pages._
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
