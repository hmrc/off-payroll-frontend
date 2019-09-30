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

import javax.inject.{Inject, Singleton}

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import controllers.routes._
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.setup.{routes => setupRoutes}
import models._
import models.sections.setup.{WhatDoYouWantToDo, WhatDoYouWantToFindOut, WhoAreYou}
import pages._
import pages.sections.setup._
import play.api.mvc.Call

//noinspection ScalaStyle
@Singleton
class SetupNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private val optimisedSetupRouteMap: Map[Page, UserAnswers => Call] = Map(
    //Initialisation Section
    IndexPage -> (_ => setupRoutes.AboutYourResultController.onPageLoad()),

    //Setup Section
    AboutYourResultPage -> (_ => setupRoutes.WhatDoYouWantToFindOutController.onPageLoad(NormalMode)),
    WhatDoYouWantToFindOutPage -> (_ => setupRoutes.WhoAreYouController.onPageLoad(NormalMode)),
    WhoAreYouPage -> (answers => (answers.getAnswer(WhatDoYouWantToFindOutPage), answers.getAnswer(WhoAreYouPage)) match {
      case (Some(WhatDoYouWantToFindOut.PAYE),_) | (Some(WhatDoYouWantToFindOut.IR35),Some(WhoAreYou.Client)) => setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
      case (Some(WhatDoYouWantToFindOut.IR35),Some(WhoAreYou.Worker)) => setupRoutes.WhatDoYouWantToDoController.onPageLoad(NormalMode)
      case (Some(WhatDoYouWantToFindOut.IR35),Some(WhoAreYou.Agency)) => setupRoutes.AgencyAdvisoryController.onPageLoad()
      case (None,_) => setupRoutes.WhatDoYouWantToFindOutController.onPageLoad(NormalMode)
      case (_,_) => setupRoutes.WhoAreYouController.onPageLoad(NormalMode)
    }),
    WhatDoYouWantToDoPage -> (answers => answers.getAnswer(WhatDoYouWantToDoPage) match {
      case Some(WhatDoYouWantToDo.MakeNewDetermination) => setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
      case Some(WhatDoYouWantToDo.CheckDetermination) => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      case None => setupRoutes.WhatDoYouWantToDoController.onPageLoad(NormalMode)
    }),
    AgencyAdvisoryPage -> (_ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)),
    WorkerUsingIntermediaryPage -> (answers => (answers.getAnswer(WorkerUsingIntermediaryPage),answers.getAnswer(WhatDoYouWantToFindOutPage)) match {
      case (Some(true),Some(WhatDoYouWantToFindOut.PAYE)) => setupRoutes.IntermediaryController.onPageLoad()
      case (Some(false),Some(WhatDoYouWantToFindOut.PAYE)) => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      case (Some(true),Some(WhatDoYouWantToFindOut.IR35)) => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      case (Some(false),Some(WhatDoYouWantToFindOut.IR35)) => setupRoutes.NoIntermediaryController.onPageLoad()
      case (_,None) => setupRoutes.WhatDoYouWantToFindOutController.onPageLoad(NormalMode)
      case (None,_) => setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
    }),
    ContractStartedPage -> (_ => exitRoutes.OfficeHolderController.onPageLoad(NormalMode))
  )

  private val setupRouteMap: Map[Page, UserAnswers => Call] = Map(

    //Initialisation Section
    IndexPage -> (_ => setupRoutes.AboutYouController.onPageLoad(NormalMode)),

    //Setup Section
    AboutYouPage -> (_ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)),
    ContractStartedPage -> (_ => setupRoutes.WorkerTypeController.onPageLoad(NormalMode)),
    WorkerTypePage -> (_ => exitRoutes.OfficeHolderController.onPageLoad(NormalMode))
  )

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = {
    val routing = if (isEnabled(OptimisedFlow)) optimisedSetupRouteMap else setupRouteMap
    routing.getOrElse(page, _ => IndexController.onPageLoad())
  }
}
