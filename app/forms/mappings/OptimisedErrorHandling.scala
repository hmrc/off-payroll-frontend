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

package forms.mappings

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import models.UserType.Worker
import models.requests.DataRequest

trait OptimisedErrorHandling extends FeatureSwitching {

  def tailoredErrMsg(key: String, optimisedPrefix: Boolean = false)(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): String =
      s"${request.userType.getOrElse(Worker)}${if(optimisedPrefix) ".optimised" else ""}.$key"

  def tailoredErrMsgOptimised(key: String)(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): String =
    tailoredErrMsg(key, optimisedPrefix = true)
}
