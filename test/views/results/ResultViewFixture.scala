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

package views.results

import akka.http.scaladsl.model.HttpMethods
import models.AboutYouAnswer.Worker
import models.AdditionalPdfDetails
import models.CannotClaimAsExpense.WorkerUsedVehicle
import play.api.i18n.Messages
import play.api.mvc.Call
import play.twirl.api.Html
import utils.FakeTimestamp
import viewmodels.{AnswerRow, AnswerSection}
import views.behaviours.ViewBehaviours

trait ResultViewFixture extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors {
    override val subheading = "p.font-large"
  }

  val postAction = Call(HttpMethods.POST.value, "/")

  val version = "1.0"

  val timestamp = FakeTimestamp.timestamp()

  val model = AdditionalPdfDetails(Some("Gerald"), Some("PBPlumbin"), Some("Plumber"), Some("Boiler man"))

  val answers = Seq(
    AnswerSection(Some(Messages("result.peopleInvolved.h2")), None, Seq(
      (AnswerRow(
        label = "aboutYou.checkYourAnswersLabel",
        answer = s"aboutYou.$Worker",
        answerIsMessageKey = true
      ),None)
    )),
    AnswerSection(Some(Messages("result.workersDuties.h2")), whyResult = Some(Html(messages("result.officeHolderInsideIR35.whyResult.p1"))), Seq(
      (AnswerRow(
        label = "contractStarted.checkYourAnswersLabel",
        answer = "site.yes",
        answerIsMessageKey = true
      ),None)
    )),
    AnswerSection(Some(Messages("result.substitutesHelpers.h2")), whyResult = Some(Html(messages("result.substitutesAndHelpers.summary"))), Seq()),
    AnswerSection(Some(Messages("result.workArrangements.h2")), whyResult = Some(Html(messages("result.workArrangements.summary"))), Seq(
      (AnswerRow(
        label = "cannotClaimAsExpense.checkYourAnswersLabel",
        answers = Seq(AnswerRow(
          label = "cannotClaimAsExpense.checkYourAnswersLabel",
          answer = s"cannotClaimAsExpense.$WorkerUsedVehicle",
          answerIsMessageKey = true
        ))
      ),None),
      (AnswerRow(
        label = "officeHolder.checkYourAnswersLabel",
        answer = "site.yes",
        answerIsMessageKey = true
      ),None)
    )),
    AnswerSection(Some(Messages("result.financialRisk.h2")), whyResult = Some(Html(messages("result.financialRisk.summary"))), Seq()),
    AnswerSection(Some(Messages("result.partAndParcel.h2")), whyResult = Some(Html(messages("result.partParcel.summary"))), Seq())
  )

}
