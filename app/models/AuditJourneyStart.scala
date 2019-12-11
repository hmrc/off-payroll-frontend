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




