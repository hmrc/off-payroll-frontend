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

import models.requests.DataRequest
import models.{Answers, UserAnswers}
import navigation.QuestionDeletionLookup
import pages._
import play.api.Logger
import play.api.libs.json.{Reads, Writes}

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext

class CompareAnswerService @Inject()(questionDeletionLookup: QuestionDeletionLookup) {

  def constructAnswers[T](request: DataRequest[_], value: T,
                          page: QuestionPage[T])(implicit reads: Reads[T],writes: Writes[T],
                                                         aWrites: Writes[Answers[T]],aReads: Reads[Answers[T]],ec: ExecutionContext): UserAnswers = {
    val answerNumber = request.userAnswers.size
    request.userAnswers.get(page) match {
      case None => request.userAnswers.set(page, answerNumber, value)
      case Some(answer) => {
        //get all answers, sort them in the order they were answered in, find the answers that came after the current answer,
        // find what page they are associated with, then remove them
        val updatedAnswers = recursivelyClearQuestions(
          request.userAnswers.cacheMap.data.map(value => (value._1, (value._2 \ "answerNumber").get.as[Int])).toList.sortBy(_._2)
            .splitAt(answer.answerNumber)._2.map(_._1).map(pageName => Page.questionToPage(pageName))
          , request.userAnswers)
        updatedAnswers.set(page, updatedAnswers.size, value)
      }
    }
  }

  def optimisedConstructAnswers[T](request: DataRequest[_], value: T,
                          page: QuestionPage[T])(implicit reads: Reads[T],writes: Writes[T],
                                                 aWrites: Writes[Answers[T]],aReads: Reads[Answers[T]],ec: ExecutionContext): UserAnswers = {
    request.userAnswers.get(page) match {
      case Some(answer) if answer.answer == value => request.userAnswers
      case _ => {
        val userAnswers = request.userAnswers.set(page,0,value)
        val pagesToRemove = questionDeletionLookup.getPagesToRemove(page)(userAnswers)
        Logger.debug(s"[CompareAnswerService][optimisedConstructAnswers] Questions to be removed: \n$pagesToRemove")
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
