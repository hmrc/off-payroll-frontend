/*
 * Copyright 2022 HM Revenue & Customs
 *
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
