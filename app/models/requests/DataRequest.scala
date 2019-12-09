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
