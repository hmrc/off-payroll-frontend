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

import javax.inject.Inject

import connectors.DataCacheConnector
import models.{Answers, UserAnswers}
import pages._
import play.api.libs.json.{JsString, Json, Reads, Writes}
import models.requests.DataRequest
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import pages.sections.setup.{AboutYouPage, ContractStartedPage, WhichDescribesYouPage, WorkerTypePage}
import play.api.mvc.AnyContent

import scala.annotation.tailrec
import scala.collection.immutable.Map
import scala.concurrent.{ExecutionContext, Future}

class CompareAnswerService @Inject()(dataCacheConnector: DataCacheConnector) {

  def constructAnswers[T](request: DataRequest[AnyContent], value: T,
                       page: QuestionPage[T])(implicit reads: Reads[T],writes: Writes[T],
                                              aWrites: Writes[Answers[T]],aReads: Reads[Answers[T]],ec: ExecutionContext): Future[UserAnswers] = {
    val answerNumber = request.userAnswers.size
    request.userAnswers.get(page) match {
      case None => Future.successful(request.userAnswers.set(page, answerNumber, value))
      case Some(answer) if answer.answer == value => Future.successful(request.userAnswers)
      case Some(answer) => {
        //get all answers, sort them in the order they were answered in, find the answers that came after the current answer,
        // find what page they are associated with, then remove them
        val updatedAnswers = recursivelyClearQuestions(
          request.userAnswers.cacheMap.data.map(value => (value._1, (value._2 \ "answerNumber").get.as[Int])).toList.sortBy(_._2)
            .splitAt(answer.answerNumber)._2.map(_._1).map(pageName => questionToPage(pageName))
          , request.userAnswers)
        //if an answer is changed, the final decision will change too
        dataCacheConnector.clearDecision(request.userAnswers.cacheMap.id).map { _ =>
          updatedAnswers.set(page, updatedAnswers.size, value)
        }
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
    "whichDescribesYou" -> WhichDescribesYouPage,
    "contractStarted" -> ContractStartedPage,
    "businessSize" -> BusinessSizePage,
    "workerType" -> WorkerTypePage,
    "workerUsingIntermediary" -> WorkerUsingIntermediaryPage,
    "isWorkForPrivateSector" -> IsWorkForPrivateSectorPage,
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
