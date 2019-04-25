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

import models.UserAnswers
import pages.{AboutYouPage, QuestionPage, _}
import play.api.libs.json.Reads
import play.api.libs.json.Writes
import models.requests.DataRequest
import play.api.mvc.AnyContent

trait CompareAnswerService[T] {

  lazy val pageOrder = Seq(
    AboutYouPage,
    ContractStartedPage,
    WorkerTypePage,
    OfficeHolderPage,
    ArrangedSubstitutePage,
    RejectSubstitutePage,
    WouldWorkerPaySubstitutePage,
    NeededToPayHelperPage,
    MoveWorkerPage,
    HowWorkIsDonePage,
    ScheduleOfWorkingHoursPage,
    ChooseWhereWorkPage,
    CannotClaimAsExpensePage,
    HowWorkerIsPaidPage,
    PutRightAtOwnCostPage,
    BenefitsPage,
    LineManagerDutiesPage,
    InteractWithStakeholdersPage,
    IdentifyToStakeholdersPage
  )

  def getPagesToClear(currentPage: QuestionPage[T]): Seq[Page] = {
    pageOrder.splitAt(pageOrder.indexOf(currentPage))._2
  }

  def constructAnswers(request: DataRequest[AnyContent], value: T,
                       page: QuestionPage[T])(implicit reads: Reads[T],writes: Writes[T]): UserAnswers = {
    val previousAnswer = request.userAnswers.get(page)(reads)
    val updatedAnswer = request.userAnswers.set(page, value)
    if(previousAnswer.fold(false){ answer => answer == updatedAnswer}){
      println("1")
      request.userAnswers
    } else {
      println("2")
      val removedPages = recursivelyClearQuestions(getPagesToClear(page),request.userAnswers)
      removedPages.set(page,updatedAnswer.get(page).get)
    }
  }

  def recursivelyClearQuestions(pages: Seq[Page], userAnswers: UserAnswers): UserAnswers = {
    if(pages.isEmpty) userAnswers else {
      val updatedAnswers = UserAnswers(userAnswers.cacheMap copy (data = userAnswers.cacheMap.data - pages.head))
      recursivelyClearQuestions(pages.tail,updatedAnswers)
    }
  }

}
