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
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.setup.{routes => setupRoutes}
import models.AboutYouAnswer.Worker
import models.CannotClaimAsExpense.WorkerUsedVehicle
import models.{AdditionalPdfDetails, CheckMode, Timestamp}
import play.api.i18n.Messages
import play.api.mvc.Call
import play.twirl.api.Html
import viewmodels.{AnswerRow, AnswerSection}
import views.behaviours.ViewBehaviours

trait ResultViewFixture extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val postAction = Call(HttpMethods.POST.value, "/")

  val version = "1.0"

  val timestamp = Timestamp.timestamp

  val model = AdditionalPdfDetails(Some("Gerald"), Some("PBPlumbin"), Some("Plumber"), Some("Boiler man"))

  val answers = Seq(
    AnswerSection(Some(Messages("result.peopleInvolved.h2")), None, Seq(
      (AnswerRow(
        label = "aboutYou.checkYourAnswersLabel",
        answer = s"aboutYou.$Worker",
        answerIsMessageKey = true,
        changeUrl = setupRoutes.AboutYouController.onPageLoad(CheckMode).url
      ),None)
    )),
    AnswerSection(Some(Messages("result.workersDuties.h2")), whyResult = Some(Html(messages("result.officeHolderInsideIR35.whyResult.p1"))), Seq(
      (AnswerRow(
        label = "contractStarted.checkYourAnswersLabel",
        answer = "site.yes",
        answerIsMessageKey = true,
        changeUrl = setupRoutes.ContractStartedController.onPageLoad(CheckMode).url
      ),None)
    )),
    AnswerSection(Some(Messages("result.substitutesHelpers.h2")), whyResult = Some(Html(messages("result.substitutesAndHelpers.summary"))), Seq()),
    AnswerSection(Some(Messages("result.workArrangements.h2")), whyResult = Some(Html(messages("result.workArrangements.summary"))), Seq(
      (AnswerRow(
        label = "cannotClaimAsExpense.checkYourAnswersLabel",
        answer = Seq(s"cannotClaimAsExpense.$WorkerUsedVehicle"),
        answerIsMessageKey = true,
        changeUrl = financialRiskRoutes.CannotClaimAsExpenseController.onPageLoad(CheckMode).url
      ),None),
      (AnswerRow(
        label = "officeHolder.checkYourAnswersLabel",
        answer = "site.yes",
        answerIsMessageKey = true,
        changeUrl = exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url
      ),None)
    )),
    AnswerSection(Some(Messages("result.financialRisk.h2")), whyResult = Some(Html(messages("result.financialRisk.summary"))), Seq()),
    AnswerSection(Some(Messages("result.partAndParcel.h2")), whyResult = Some(Html(messages("result.partParcel.summary"))), Seq())
  )

}