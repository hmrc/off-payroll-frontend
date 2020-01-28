/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.requests

import config.SessionKeys
import models.UserAnswers
import models.sections.setup.WhoAreYou
import play.api.mvc.{Request, WrappedRequest}
import utils.SessionUtils._

abstract class BaseDataRequest[A] (request: Request[A]) extends WrappedRequest[A](request) {
  val userType: Option[WhoAreYou] = request.session.getModel[WhoAreYou](SessionKeys.userType)
}

case class OptionalDataRequest[A] (request: Request[A],
                                   internalId: String,
                                   userAnswers: Option[UserAnswers]) extends BaseDataRequest(request) {

}

case class DataRequest[A] (request: Request[A],
                           internalId: String,
                           userAnswers: UserAnswers) extends BaseDataRequest(request)
