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

package services

import models.{Answers, UserAnswers}
import pages._
import play.api.libs.json.{JsString, Json, Reads, Writes}
import models.requests.DataRequest
import play.api.mvc.AnyContent

import scala.annotation.tailrec
import scala.collection.immutable.Map

object CompareAnswerService {

  def constructAnswers[T](request: DataRequest[AnyContent], value: T,
                       page: QuestionPage[T])(implicit reads: Reads[T],writes: Writes[T],aWrites: Writes[Answers[T]],aReads: Reads[Answers[T]]): UserAnswers = {
    val answerNumber = request.userAnswers.size
    request.userAnswers.get(page) match {
      case None => request.userAnswers.set(page, answerNumber, value)
      case Some(answer) if answer.answer == value => request.userAnswers
      case Some(answer) => {
        //get all answers, sort them in the order they were answered in, find the answers that came after the current answer,
        // find what page they are associated with, then remove them
        val updatedAnswers = recursivelyClearQuestions(
          request.userAnswers.cacheMap.data.map(value => (value._1, (value._2 \ "answerNumber").get.as[Int])).toList.sortBy(_._2)
            .splitAt(answer.answerNumber)._2.map(_._1).map(pageName => questionToPage(pageName))
          , request.userAnswers)
        updatedAnswers.set(page, updatedAnswers.size, value)
      }
    }
  }

  @tailrec
  private def recursivelyClearQuestions(pages: List[QuestionPage[_]], userAnswers: UserAnswers): UserAnswers = {
    if(pages.isEmpty) userAnswers else {
      recursivelyClearQuestions(pages.tail,userAnswers.remove(pages.head))
    }
  }

  private lazy val questionToPage = Map(
    "aboutYou" -> AboutYouPage,
    "contractStarted" -> ContractStartedPage,
    "workerType" -> WorkerTypePage,
    "officeHolder" -> OfficeHolderPage,
    "arrangedSubstitute" -> ArrangedSubstitutePage,
    "didPaySubstitute" -> DidPaySubstitutePage,
    "wouldWorkerPaySubstitute" -> WouldWorkerPaySubstitutePage,
    "neededToPayHelper" -> NeededToPayHelperPage,
    "moveWorker" -> MoveWorkerPage,
    "rejectSubstitute" -> RejectSubstitutePage,
    "howWorkIsDone" -> HowWorkIsDonePage,
    "scheduleOfWorkingHours" -> ScheduleOfWorkingHoursPage,
    "chooseWhereWork" -> ChooseWhereWorkPage,
    "cannotClaimAsExpense" -> CannotClaimAsExpensePage,
    "howWorkerIsPaid" -> HowWorkerIsPaidPage,
    "putRightAtOwnCost" -> PutRightAtOwnCostPage,
    "benefits" -> BenefitsPage,
    "lineManagerDuties" -> LineManagerDutiesPage,
    "interactWithStakeholders" -> InteractWithStakeholdersPage,
    "identifyToStakeholders" -> IdentifyToStakeholdersPage,
    "customisePDF" -> CustomisePDFPage,
    "result" -> ResultPage
  )

}
