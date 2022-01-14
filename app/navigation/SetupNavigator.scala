/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package navigation

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.routes._
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.setup.{routes => setupRoutes}
import javax.inject.{Inject, Singleton}
import models._
import models.sections.setup.{WhatDoYouWantToDo, WhatDoYouWantToFindOut, WhoAreYou}
import pages._
import pages.sections.setup._
import play.api.mvc.Call

//noinspection ScalaStyle
@Singleton
class SetupNavigator @Inject()(implicit appConfig: FrontendAppConfig) extends Navigator with FeatureSwitching {

  private val setupRouteMap: Map[Page, UserAnswers => Call] = Map(
    //Initialisation Section
    IndexPage -> (_ => setupRoutes.AboutYourResultController.onPageLoad),

    //Setup Section
    AboutYourResultPage -> (_ => setupRoutes.WhatDoYouWantToFindOutController.onPageLoad(NormalMode)),
    WhatDoYouWantToFindOutPage -> (_ => setupRoutes.WhoAreYouController.onPageLoad(NormalMode)),
    WhoAreYouPage -> (answers => (answers.get(WhatDoYouWantToFindOutPage), answers.get(WhoAreYouPage)) match {
      case (Some(WhatDoYouWantToFindOut.PAYE),_) | (Some(WhatDoYouWantToFindOut.IR35),Some(WhoAreYou.Hirer)) =>
        setupRoutes.WorkerUsingIntermediaryController.onPageLoad(NormalMode)
      case (Some(WhatDoYouWantToFindOut.IR35),Some(WhoAreYou.Worker)) => setupRoutes.WhatDoYouWantToDoController.onPageLoad(NormalMode)
      case (Some(WhatDoYouWantToFindOut.IR35),Some(WhoAreYou.Agency)) => setupRoutes.AgencyAdvisoryController.onPageLoad()
      case (None,_) => setupRoutes.WhatDoYouWantToFindOutController.onPageLoad(NormalMode)
      case (_,_) => setupRoutes.WhoAreYouController.onPageLoad(NormalMode)
    }),
    WhatDoYouWantToDoPage -> (answers => answers.get(WhatDoYouWantToDoPage) match {
      case Some(WhatDoYouWantToDo.MakeNewDetermination) => setupRoutes.WorkerUsingIntermediaryController.onPageLoad(NormalMode)
      case Some(WhatDoYouWantToDo.CheckDetermination) => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      case None => setupRoutes.WhatDoYouWantToDoController.onPageLoad(NormalMode)
    }),
    AgencyAdvisoryPage -> (_ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)),
    WorkerUsingIntermediaryPage -> (answers => (answers.get(WorkerUsingIntermediaryPage),answers.get(WhatDoYouWantToFindOutPage)) match {
      case (Some(true),Some(WhatDoYouWantToFindOut.PAYE)) => setupRoutes.IntermediaryController.onPageLoad()
      case (Some(false),Some(WhatDoYouWantToFindOut.PAYE)) => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      case (Some(true),Some(WhatDoYouWantToFindOut.IR35)) => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      case (Some(false),Some(WhatDoYouWantToFindOut.IR35)) => setupRoutes.NoIntermediaryController.onPageLoad()
      case (_,None) => setupRoutes.WhatDoYouWantToFindOutController.onPageLoad(NormalMode)
      case (None,_) => setupRoutes.WorkerUsingIntermediaryController.onPageLoad(NormalMode)
    }),
    ContractStartedPage -> (_ => exitRoutes.OfficeHolderController.onPageLoad(NormalMode))
  )

  override def nextPage(page: Page, mode: Mode): UserAnswers => Call = {
    setupRouteMap.getOrElse(page, _ => IndexController.onPageLoad)
  }
}
