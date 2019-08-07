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

package navigation

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import controllers.routes._
import javax.inject.{Inject, Singleton}
import models._
import pages._
import play.api.mvc.Call

@Singleton
class CYANavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private val routeMap:  Map[Page, UserAnswers => Call] = Map(
    CheckYourAnswersPage -> (_ => ResultController.onPageLoad()),
    ResultPage -> { answer =>
      if(isEnabled(OptimisedFlow)) {
        answer.get(ResultPage) match {
          case Some(Answers(true, _)) => AddReferenceDetailsController.onPageLoad()
          case _ => FinishedCheckingController.onPageLoad()
        }
      } else {
        PDFController.onPageLoad(NormalMode)
      }
    },
    AddReferenceDetailsPage -> {
      answer =>
        answer.get(AddReferenceDetailsPage) match {
          case Some(Answers(true, _)) => PDFController.onPageLoad(NormalMode)
          case _ => FinishedCheckingController.onPageLoad()
        }
    },
    CustomisePDFPage -> (_ => FinishedCheckingController.onPageLoad())
  )

  override def nextPage(page: Page, mode: Mode = NormalMode): UserAnswers => Call = routeMap.getOrElse(page, _ => IndexController.onPageLoad())

}
