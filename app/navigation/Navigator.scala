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
import play.api.mvc.Call
import controllers.routes
import models.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import pages._
import models._

@Singleton
class Navigator @Inject()() {

  private val routeMap: Map[Page, UserAnswers => Call] = Map(

    //Initialisation Section
    IndexPage -> (_ => routes.AboutYouController.onPageLoad(NormalMode)),

    //Setup Section
    AboutYouPage -> (_ => routes.ContractStartedController.onPageLoad(NormalMode)),
    ContractStartedPage -> (_ => routes.WorkerTypeController.onPageLoad(NormalMode)),
    WorkerTypePage -> (_ => routes.OfficeHolderController.onPageLoad(NormalMode)),

    //Early Exit Section
    OfficeHolderPage -> (answers => answers.get(ContractStartedPage) match {
      case Some(Answers(true,_)) => routes.ArrangedSubstituteController.onPageLoad(NormalMode)
      case Some(_) => routes.RejectSubstituteController.onPageLoad(NormalMode)
      case _ => routes.ContractStartedController.onPageLoad(NormalMode)
    }),

    //Personal Service Section
    ArrangedSubstitutePage -> (answers =>
      answers.get(ArrangedSubstitutePage) match {
        case Some(Answers(YesClientAgreed,_)) => routes.DidPaySubstituteController.onPageLoad(NormalMode)
        case Some(Answers(YesClientNotAgreed,_)) => routes.NeededToPayHelperController.onPageLoad(NormalMode)
        case Some(Answers(No,_)) => routes.RejectSubstituteController.onPageLoad(NormalMode)
        case _ => routes.ArrangedSubstituteController.onPageLoad(NormalMode)
      }),
    DidPaySubstitutePage -> (_ => routes.NeededToPayHelperController.onPageLoad(NormalMode)),
    RejectSubstitutePage -> (answers =>
      (answers.get(ContractStartedPage), answers.get(RejectSubstitutePage)) match {
        case (Some(Answers(true,_)), Some(Answers(true,_))) => routes.NeededToPayHelperController.onPageLoad(NormalMode)
        case (_, Some(Answers(false,_))) => routes.WouldWorkerPaySubstituteController.onPageLoad(NormalMode)
        case (_, Some(Answers(true,_))) => routes.MoveWorkerController.onPageLoad(NormalMode)
        case (None, _) => routes.ContractStartedController.onPageLoad(NormalMode)
        case (_, None) => routes.RejectSubstituteController.onPageLoad(NormalMode)
      }),
    WouldWorkerPaySubstitutePage -> (answers =>
      answers.get(ContractStartedPage) match {
        case Some(Answers(true,_)) => routes.NeededToPayHelperController.onPageLoad(NormalMode)
        case Some(_) => routes.MoveWorkerController.onPageLoad(NormalMode)
        case _ => routes.ContractStartedController.onPageLoad(NormalMode)
    }),
    NeededToPayHelperPage -> (_ => routes.MoveWorkerController.onPageLoad(NormalMode)),

    //Control Section
    MoveWorkerPage -> (_ => routes.HowWorkIsDoneController.onPageLoad(NormalMode)),
    HowWorkIsDonePage -> (_ => routes.ScheduleOfWorkingHoursController.onPageLoad(NormalMode)),
    ScheduleOfWorkingHoursPage -> (_ => routes.ChooseWhereWorkController.onPageLoad(NormalMode)),
    ChooseWhereWorkPage -> (_ => routes.CannotClaimAsExpenseController.onPageLoad(NormalMode)),

    //Financial Risk Section
    CannotClaimAsExpensePage -> (_ => routes.HowWorkerIsPaidController.onPageLoad(NormalMode)),
    HowWorkerIsPaidPage -> (_ => routes.PutRightAtOwnCostController.onPageLoad(NormalMode)),
    PutRightAtOwnCostPage -> (_ => routes.BenefitsController.onPageLoad(NormalMode)),

    //Part and Parcel Section
    BenefitsPage -> (_ => routes.LineManagerDutiesController.onPageLoad(NormalMode)),
    LineManagerDutiesPage -> (_ => routes.InteractWithStakeholdersController.onPageLoad(NormalMode)),
    InteractWithStakeholdersPage -> (_ => routes.IdentifyToStakeholdersController.onPageLoad(NormalMode)),
    IdentifyToStakeholdersPage -> (_ => routes.ResultController.onPageLoad()),

    //Results Page
    ResultPage -> (_ => routes.PDFController.onPageLoad(NormalMode))
  )

  private val checkRouteMap: Map[Page, UserAnswers => Call] = Map()

  def nextPage(page: Page, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      routeMap.getOrElse(page, _ => routes.IndexController.onPageLoad())
    case CheckMode =>
      checkRouteMap.getOrElse(page, _ => routes.ResultController.onPageLoad())
  }
}
