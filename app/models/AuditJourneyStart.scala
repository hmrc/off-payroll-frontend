/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import pages._
import pages.sections.setup._
import play.api.Logger
import play.api.libs.json.{Json, Reads, Writes}
import utils.JsonObjectSugar

case class AuditJourneyStart(userAnswers: UserAnswers)

object AuditJourneyStart extends JsonObjectSugar {

  implicit val writes: Writes[AuditJourneyStart] = Writes { implicit model =>
    val json = jsonObjNoNulls(
      "correlationId" -> model.userAnswers.cacheMap.id,
      "setup" -> jsonObjNoNulls(
        answerFor(WhatDoYouWantToFindOutPage),
        answerFor(WhoAreYouPage),
        answerFor(WhatDoYouWantToDoPage),
        answerFor(WorkerUsingIntermediaryPage),
        answerFor(ContractStartedPage)
      )
    )
    Logger.debug(s"[AuditJourneyStart][JsonWrites] Audit Detail Json: $json")
    json
  }

  private def answerFor[A](page: QuestionPage[A])(implicit auditModel: AuditJourneyStart, reads: Reads[A], writes: Writes[A]): (String, Json.JsValueWrapper) =
    page.toString -> Json.toJson(auditModel.userAnswers.get(page))

}




