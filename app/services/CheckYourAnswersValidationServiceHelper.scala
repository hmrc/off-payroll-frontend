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

package services

import models.UserAnswers
import models.sections.personalService.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import models.sections.setup.WhatDoYouWantToDo.MakeNewDetermination
import models.sections.setup.WhatDoYouWantToFindOut
import models.sections.setup.WhoAreYou.{Client, Worker}
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.personalService._
import pages.sections.setup._

trait CheckYourAnswersValidationServiceHelper {

  // Setup Section Mandatory Page Logic
  // ==================================
  def intermediaryPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.getAnswer(WhoAreYouPage), userAnswers.getAnswer(WhatDoYouWantToDoPage)) match {
      case (Some(Client), _) | (_, Some(MakeNewDetermination)) => Set(WorkerUsingIntermediaryPage)
      case _ => Set()
    }
  }

  def whatDoYouWantToDoPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.getAnswer(WhatDoYouWantToFindOutPage), userAnswers.getAnswer(WhoAreYouPage)) match {
      case (Some(WhatDoYouWantToFindOut.IR35), Some(Worker)) => Set(WhatDoYouWantToDoPage)
      case _ => Set()
    }
  }

  // Personal Service Section Mandatory Page Logic
  // =============================================
  def arrangedRejectedPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(ContractStartedPage) match {
      case Some(true) => Set(ArrangedSubstitutePage)
      case Some(false) => Set(RejectSubstitutePage)
      case _ => Set()
    }
  }

  def didPayRejectedNeededPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(ArrangedSubstitutePage) match {
      case Some(YesClientAgreed) => Set(DidPaySubstitutePage)
      case Some(No) => Set(RejectSubstitutePage)
      case Some(YesClientNotAgreed) => Set(NeededToPayHelperPage)
      case _ => Set()
    }
  }

  def contractNeededPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.getAnswer(WouldWorkerPaySubstitutePage), userAnswers.getAnswer(ContractStartedPage)) match {
      case (Some(false), Some(true)) => Set(NeededToPayHelperPage)
      case _ => Set()
    }
  }

  def contractNeededWouldPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.getAnswer(RejectSubstitutePage), userAnswers.getAnswer(ContractStartedPage)) match {
      case (Some(true), Some(true)) => Set(NeededToPayHelperPage)
      case (Some(false), _) => Set(WouldWorkerPaySubstitutePage)
      case _ => Set()
    }
  }

  def neededPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(DidPaySubstitutePage) match {
      case Some(false) => Set(NeededToPayHelperPage)
      case _ => Set()
    }
  }


  // Business On Own Account Mandatory Page logic
  // ============================================
  def workerKnownPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.getAnswer(WhoAreYouPage), userAnswers.getAnswer(ContractStartedPage)) match {
      case (Some(Client), Some(false)) => Set(WorkerKnownPage)
      case _ => Set()
    }
  }

  def permissionToWorkWithOtherClientsPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(MultipleContractsPage) match {
      case Some(false) => Set(PermissionToWorkWithOthersPage)
      case _ => Set()
    }
  }

  def rightsOfWorkPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(OwnershipRightsPage) match {
      case Some(true) => Set(RightsOfWorkPage)
      case _ => Set()
    }
  }

  def transferRightsPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(RightsOfWorkPage) match {
      case Some(false) => Set(TransferOfRightsPage)
      case _ => Set()
    }
  }

  def followOnContractPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(PreviousContractPage) match {
      case Some(true) => Set(FollowOnContractPage)
      case _ => Set()
    }
  }

  def firstContractPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.getAnswer(PreviousContractPage), userAnswers.getAnswer(FollowOnContractPage)) match {
      case (Some(false), _) | (_, Some(false)) => Set(FirstContractPage)
      case _ => Set()
    }
  }

  def extendContractPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(FirstContractPage) match {
      case Some(false) => Set(ExtendContractPage)
      case _ => Set()
    }
  }
}
