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

package handlers.mocks

import handlers.ErrorHandler
import org.scalamock.scalatest.MockFactory
import play.api.mvc.Request
import play.twirl.api.Html

trait MockErrorHandler extends MockFactory {

  val mockErrorHandler = mock[ErrorHandler]

  def mockStandardError(response: Html): Unit = {
    (mockErrorHandler.standardErrorTemplate(_: String, _: String, _: String)(_: Request[_]))
      .expects(*,*,*,*)
      .returns(response)
  }

  def mockBadRequest(response: Html): Unit = {
    (mockErrorHandler.badRequestTemplate(_: Request[_]))
      .expects(*)
      .returns(response)
  }

  def mockNotFound(response: Html): Unit = {
    (mockErrorHandler.notFoundTemplate(_: Request[_]))
      .expects(*)
      .returns(response)
  }

  def mockInternalServerError(response: Html): Unit = {
    (mockErrorHandler.internalServerErrorTemplate(_: Request[_]))
      .expects(*)
      .returns(response)
  }
}
