/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import javax.inject.Inject
import models.UserAnswers
import models.requests.DataRequest
import navigation.QuestionDeletionLookup
import pages._
import play.api.Logging
import play.api.libs.json.{Reads, Writes}

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext

class CompareAnswerService @Inject()(questionDeletionLookup: QuestionDeletionLookup) extends Logging {

  def constructAnswers[T](request: DataRequest[_], value: T,
                          page: QuestionPage[T])(implicit reads: Reads[T],writes: Writes[T],ec: ExecutionContext): UserAnswers = {
    request.userAnswers.get(page) match {
      case Some(answer) if answer == value => request.userAnswers
      case _ => {
        val userAnswers = request.userAnswers.set(page,value)
        val pagesToRemove = questionDeletionLookup.getPagesToRemove(page)(userAnswers)
        logger.debug(s"[CompareAnswerService][constructAnswers] Questions to be removed: \n$pagesToRemove")
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
