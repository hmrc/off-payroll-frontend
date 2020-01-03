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
