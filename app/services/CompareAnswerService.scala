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

package services

import javax.inject.Inject
import models.UserAnswers
import models.requests.DataRequest
import navigation.QuestionDeletionLookup
import pages._
import play.api.Logger
import play.api.libs.json.{Reads, Writes}

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext

class CompareAnswerService @Inject()(questionDeletionLookup: QuestionDeletionLookup) {

  def constructAnswers[T](request: DataRequest[_], value: T,
                          page: QuestionPage[T])(implicit reads: Reads[T],writes: Writes[T],ec: ExecutionContext): UserAnswers = {
    request.userAnswers.get(page) match {
      case Some(answer) if answer == value => request.userAnswers
      case _ => {
        val userAnswers = request.userAnswers.set(page,value)
        val pagesToRemove = questionDeletionLookup.getPagesToRemove(page)(userAnswers)
        Logger.debug(s"[CompareAnswerService][constructAnswers] Questions to be removed: \n$pagesToRemove")
        recursivelyClearQuestions(pagesToRemove, userAnswers)
      }
    }
  }

  @tailrec
  private def recursivelyClearQuestions(pages: List[QuestionPage[_]], userAnswers: UserAnswers): UserAnswers = {
    if(pages.isEmpty) userAnswers else {
      recursivelyClearQuestions(pages.tail,userAnswers.remove(pages.head))
    }
  }
}
