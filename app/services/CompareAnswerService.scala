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

import scala.collection.immutable.Map

trait CompareAnswerService[T] {

  def getPagesToClear(currentPage: QuestionPage[T],allPages: List[String]): List[QuestionPage[_]] = {
    val questionsToPages: List[QuestionPage[_]] = allPages.map(page => CompareAnswerService.questionToPage(page))
    val currentPageIndex = questionsToPages.indexOf(currentPage)
    val toBeRemoved = questionsToPages.splitAt(currentPageIndex)._2
    toBeRemoved
  }

  def constructAnswers(request: DataRequest[AnyContent], value: T,
                       page: QuestionPage[T])(implicit reads: Reads[T],writes: Writes[T],aWrites: Writes[Answers[T]],aReads: Reads[Answers[T]]): UserAnswers = {
    val answerNumber = request.userAnswers.size
    request.userAnswers.get(page) match {
      case None =>
        println("***************************************************************************")
        println("adding new answer")
        println(page.toString)
        println(answerNumber)
        println("***************************************************************************")
        request.userAnswers.set(page, answerNumber, value)
      case Some(answer) if answer.answer == value =>
        println("***************************************************************************")
        println("unchanged")
        println(answerNumber)
        println("***************************************************************************")
        request.userAnswers
      case Some(answer) => {
        println("***************************************************************************")
        println("changing answer")
        println(page.toString)
        println(answerNumber)
        println("***************************************************************************")

        val allAnswers = request.userAnswers.cacheMap.data
        val allAnswersInOrder = allAnswers.map(value => (value._1, (value._2 \ "answerNumber").get.as[Int])).toList.sortBy(result => result._2)
        val pagesToRemove = allAnswersInOrder.splitAt(answer.answerNumber)._2.map(_._1)
        val removedPages = recursivelyClearQuestions(pagesToRemove.map(pageName => CompareAnswerService.questionToPage(pageName)), request.userAnswers)
        val updatedAnswerNumber = removedPages.size
        removedPages.set(page, updatedAnswerNumber, value)
      }
    }
  }

  def recursivelyClearQuestions(pages: List[QuestionPage[_]], userAnswers: UserAnswers): UserAnswers = {
    if(pages.isEmpty) userAnswers else {
      recursivelyClearQuestions(pages.tail,userAnswers.remove(pages.head))
    }
  }

}

object CompareAnswerService {

  lazy val questionToPage = Map(
    "aboutYou" -> AboutYouPage,
    "contract_started" -> ContractStartedPage,
    "workerType" -> WorkerTypePage,
    "officeHolder" -> OfficeHolderPage,
    "arrangedSubstitute" -> ArrangedSubstitutePage,
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
    "identifyToStakeholders" -> IdentifyToStakeholdersPage
  )
}
