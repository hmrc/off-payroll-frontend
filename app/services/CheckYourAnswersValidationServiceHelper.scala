/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package services

import models.UserAnswers
import models.sections.personalService.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import models.sections.setup.WhatDoYouWantToDo.MakeNewDetermination
import models.sections.setup.WhatDoYouWantToFindOut
import models.sections.setup.WhoAreYou.{Hirer, Worker}
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.personalService._
import pages.sections.setup._

trait CheckYourAnswersValidationServiceHelper {

  // Setup Section Mandatory Page Logic
  // ==================================
  def intermediaryPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.get(WhoAreYouPage), userAnswers.get(WhatDoYouWantToDoPage)) match {
      case (Some(Hirer), _) | (_, Some(MakeNewDetermination)) => Set(WorkerUsingIntermediaryPage)
      case _ => Set()
    }
  }

  def whatDoYouWantToDoPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.get(WhatDoYouWantToFindOutPage), userAnswers.get(WhoAreYouPage)) match {
      case (Some(WhatDoYouWantToFindOut.IR35), Some(Worker)) => Set(WhatDoYouWantToDoPage)
      case _ => Set()
    }
  }

  // Personal Service Section Mandatory Page Logic
  // =============================================
  def arrangedRejectedPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.get(ContractStartedPage) match {
      case Some(true) => Set(ArrangedSubstitutePage)
      case Some(false) => Set(RejectSubstitutePage)
      case _ => Set()
    }
  }

  def didPayRejectedNeededPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.get(ArrangedSubstitutePage) match {
      case Some(YesClientAgreed) => Set(DidPaySubstitutePage)
      case Some(No) => Set(RejectSubstitutePage)
      case Some(YesClientNotAgreed) => Set(NeededToPayHelperPage)
      case _ => Set()
    }
  }

  def contractNeededPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.get(WouldWorkerPaySubstitutePage), userAnswers.get(ContractStartedPage)) match {
      case (Some(false), Some(true)) => Set(NeededToPayHelperPage)
      case _ => Set()
    }
  }

  def contractNeededWouldPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.get(RejectSubstitutePage), userAnswers.get(ContractStartedPage)) match {
      case (Some(true), Some(true)) => Set(NeededToPayHelperPage)
      case (Some(false), _) => Set(WouldWorkerPaySubstitutePage)
      case _ => Set()
    }
  }

  def neededPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.get(DidPaySubstitutePage) match {
      case Some(false) => Set(NeededToPayHelperPage)
      case _ => Set()
    }
  }


  // Business On Own Account Mandatory Page logic
  // ============================================
  def workerKnownPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.get(WhoAreYouPage), userAnswers.get(ContractStartedPage)) match {
      case (Some(Hirer), Some(false)) => Set(WorkerKnownPage)
      case _ => Set()
    }
  }

  def permissionToWorkWithOtherClientsPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.get(MultipleContractsPage) match {
      case Some(false) => Set(PermissionToWorkWithOthersPage)
      case _ => Set()
    }
  }

  def rightsOfWorkPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.get(OwnershipRightsPage) match {
      case Some(true) => Set(RightsOfWorkPage)
      case _ => Set()
    }
  }

  def transferRightsPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.get(RightsOfWorkPage) match {
      case Some(false) => Set(TransferOfRightsPage)
      case _ => Set()
    }
  }

  def followOnContractPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.get(PreviousContractPage) match {
      case Some(true) => Set(FollowOnContractPage)
      case _ => Set()
    }
  }

  def firstContractPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.get(PreviousContractPage), userAnswers.get(FollowOnContractPage)) match {
      case (Some(false), _) | (_, Some(false)) => Set(FirstContractPage)
      case _ => Set()
    }
  }

  def extendContractPage(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.get(FirstContractPage) match {
      case Some(false) => Set(ExtendContractPage)
      case _ => Set()
    }
  }
}
