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

package forms.sections.financialRisk

import config.FrontendAppConfig
import forms.mappings.{Mappings, OptimisedErrorHandling}
import javax.inject.Inject
import models.requests.DataRequest
import models.sections.financialRisk.PutRightAtOwnCost
import play.api.data.Form

class PutRightAtOwnCostFormProvider @Inject() extends Mappings with OptimisedErrorHandling {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[PutRightAtOwnCost] =
    Form(
      "value" -> enumerable[PutRightAtOwnCost](tailoredErrMsgOptimised("putRightAtOwnCost.error.required"))
    )
}
