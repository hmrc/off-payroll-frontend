/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package services.mocks

import models.UserAnswers
import models.requests.DataRequest
import org.scalamock.scalatest.MockFactory
import pages.QuestionPage
import play.api.libs.json.{Reads, Writes}
import services.CompareAnswerService

import scala.concurrent.ExecutionContext

trait MockCompareAnswerService extends MockFactory {

  val mockCompareAnswerService = mock[CompareAnswerService]

  def mockConstructAnswers[T](dataRequest: DataRequest[_], dataType: T)(result: UserAnswers): Unit = {
    (mockCompareAnswerService.constructAnswers(_: DataRequest[_],_: T, _: QuestionPage[T])
    (_: Reads[T],_: Writes[T],_: ExecutionContext))
      .expects(*,*,*,*,*,*) //TODO: Verify the mocks, currently causes failures
      .returns(result)

  }
}
