/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

import base.GuiceAppSpecBase
import play.api.i18n.Messages

import scala.io.Source

class MessagesSpec extends GuiceAppSpecBase {

  val englishFileName = "conf/messages.en"
  val welshFileName = "conf/messages.cy"

  val expectedWelshFileName = "test/resources/welshMessages/messages.cy"

  val sanitize: Iterator[String] => List[String] = _.filterNot(_.isEmpty).filterNot(_.contains("#")).toList
  val getKey: String => String = _.split("=").head.trim

  lazy val expectedWelshMessages = sanitize(Source.fromFile(expectedWelshFileName).getLines)
  lazy val actualWelshMessages = sanitize(Source.fromFile(welshFileName).getLines)
  lazy val actualEnglishMessages = sanitize(Source.fromFile(englishFileName).getLines)

  lazy val englishKeys = sanitize(Source.fromFile(englishFileName).getLines map getKey)
  lazy val welshKeys = actualWelshMessages map getKey

  "Welsh file" should {

    "not contain duplicate keys" in {
      val differences = welshKeys.diff(welshKeys.distinct)
      assert(differences.isEmpty)
    }

    "for all English language keys" should {
      for (keyValue <- englishKeys) {
        s"contain the key '$keyValue'" in {
          assert(welshKeys.contains(keyValue))
        }
      }
    }

    "for the expected welsh messages" should {

      "have the same number of lines between the expected file and the actual file" in {
        expectedWelshMessages.length mustBe actualWelshMessages.length
      }

      expectedWelshMessages.foreach { expectedMsg =>
        s"expected message: '$expectedMsg' must exist in actual messages" in {
          actualWelshMessages.contains(expectedMsg) mustBe true
        }
      }
    }
  }

  "English file" should {

    "not contain duplicate keys" in {
      val differences = englishKeys.diff(englishKeys.distinct)
      assert(differences.isEmpty)
    }

    "for all Welsh language keys" should {
      for (keyValue <- welshKeys) {
        s"contain the key '$keyValue'" in {
          assert(englishKeys.contains(keyValue))
        }
      }
    }

    "contain the correct messages for the Change Link Contexts (to support Accessibility Software)" in {
      Messages("whatDoYouWantToFindOut.changeLinkContext") mustBe "what you want to find out"
      Messages("whoAreYou.changeLinkContext") mustBe "who you are"
      Messages("whatDoYouWantToDo.changeLinkContext") mustBe "what you want to do"
      Messages("worker.workerUsingIntermediary.changeLinkContext") mustBe "if you provide your services through a limited company, partnership or unincorporated association"
      Messages("hirer.workerUsingIntermediary.changeLinkContext") mustBe "if you the worker provides their services through a limited company, partnership or unincorporated association"
      Messages("worker.contractStarted.changeLinkContext") mustBe "if you have already started working for this client"
      Messages("hirer.contractStarted.changeLinkContext") mustBe "if the worker has already started working for your organisation"
      Messages("worker.officeHolder.changeLinkContext") mustBe "if you will be an ‘Office Holder’"
      Messages("hirer.officeHolder.changeLinkContext") mustBe "if the worker will be an ‘Office Holder’"
      Messages("worker.arrangedSubstitute.changeLinkContext") mustBe "if you have ever sent a substitute to do your work"
      Messages("hirer.arrangedSubstitute.changeLinkContext") mustBe "if the worker has ever sent a substitute to do their work"
      Messages("worker.didPaySubstitute.changeLinkContext") mustBe "if you had to pay your substitute"
      Messages("hirer.didPaySubstitute.changeLinkContext") mustBe "if the worker paid their substitute"
      Messages("worker.rejectSubstitute.changeLinkContext") mustBe "if your client has the right to reject a substitute"
      Messages("hirer.rejectSubstitute.changeLinkContext") mustBe "if you have the right to reject a substitute"
      Messages("worker.wouldWorkerPaySubstitute.changeLinkContext") mustBe "if you would have to pay your substitute"
      Messages("hirer.wouldWorkerPaySubstitute.changeLinkContext") mustBe "if the worker has to pay their substitute"
      Messages("worker.neededToPayHelper.changeLinkContext") mustBe "if you have paid another person to do a significant amount of this work"
      Messages("hirer.neededToPayHelper.changeLinkContext") mustBe "if the worker paid another person to do a significant amount of this work"
      Messages("worker.moveWorker.changeLinkContext") mustBe "if your client has the right to move you from the task you originally agreed to do"
      Messages("hirer.moveWorker.changeLinkContext") mustBe "if your organisation has the right to move the worker from the task they originally agreed to do"
      Messages("worker.howWorkIsDone.changeLinkContext") mustBe "if your client has the right to decide how the work is done"
      Messages("hirer.howWorkIsDone.changeLinkContext") mustBe "if your organisation has the right to decide how the work is done"
      Messages("worker.scheduleOfWorkingHours.changeLinkContext") mustBe "if your client has the right to decide your working hours"
      Messages("hirer.scheduleOfWorkingHours.changeLinkContext") mustBe "if your organisation has the right to decide the worker’s working hours"
      Messages("worker.chooseWhereWork.changeLinkContext") mustBe "if your client has the right to decide where you do the work"
      Messages("hirer.chooseWhereWork.changeLinkContext") mustBe "if your organisation has the right to decide where the worker does the work"
      Messages("worker.equipmentExpenses.changeLinkContext") mustBe "if you will have to buy equipment before your client pays you"
      Messages("hirer.equipmentExpenses.changeLinkContext") mustBe "if the worker will have to buy equipment before your organisation pays them"
      Messages("worker.vehicle.changeLinkContext") mustBe "if you will have to fund any vehicle costs before your client pays you"
      Messages("hirer.vehicle.changeLinkContext") mustBe "if the worker will have to fund any vehicle costs before your organisation pays them"
      Messages("worker.materials.changeLinkContext") mustBe "if you will have to buy materials before your client pays you"
      Messages("hirer.materials.changeLinkContext") mustBe "if the worker will have to buy materials before your organisation pays them"
      Messages("worker.otherExpenses.changeLinkContext") mustBe "if you will have to fund any other costs before your client pays you"
      Messages("hirer.otherExpenses.changeLinkContext") mustBe "if the worker will have to fund any other costs before your organisation pays them"
      Messages("worker.howWorkerIsPaid.changeLinkContext") mustBe "how you will be paid for this work"
      Messages("hirer.howWorkerIsPaid.changeLinkContext") mustBe "how the worker will be paid for this work"
      Messages("worker.putRightAtOwnCost.changeLinkContext") mustBe "if you would have to put the work right if your client was not happy with it"
      Messages("hirer.putRightAtOwnCost.changeLinkContext") mustBe "if the worker would have to put the work right if your organisation was not happy with it"
      Messages("worker.benefits.changeLinkContext") mustBe "if your client will provide you with paid-for corporate benefits"
      Messages("hirer.benefits.changeLinkContext") mustBe "if you will provide the worker with paid-for corporate benefits"
      Messages("worker.lineManagerDuties.changeLinkContext") mustBe "if you will have any management responsibilities for your client"
      Messages("hirer.lineManagerDuties.changeLinkContext") mustBe "if the worker will have any management responsibilities for your organisation"
      Messages("worker.identifyToStakeholders.changeLinkContext") mustBe "how you would introduce yourself to your client’s consumers or suppliers"
      Messages("hirer.identifyToStakeholders.changeLinkContext") mustBe "how the worker would introduce themselves to your consumers or suppliers"
      Messages("hirer.workerKnown.changeLinkContext") mustBe "if you know who will do this work"
      Messages("worker.multipleContracts.changeLinkContext") mustBe "if this contract stops you from doing similar work for other clients"
      Messages("hirer.multipleContracts.changeLinkContext") mustBe "if the contract stops the worker from doing similar work for other organisations"
      Messages("worker.permissionToWorkWithOthers.changeLinkContext") mustBe "if you are required to ask permission to work for other clients"
      Messages("hirer.permissionToWorkWithOthers.changeLinkContext") mustBe "if the worker is required to ask permission to work for other organisations"
      Messages("worker.ownershipRights.changeLinkContext") mustBe "if there are any ownership rights relating to this contract"
      Messages("hirer.ownershipRights.changeLinkContext") mustBe "if there are any ownership rights relating to this contract"
      Messages("worker.rightsOfWork.changeLinkContext") mustBe "if the contract states the rights to this work belong to your client"
      Messages("hirer.rightsOfWork.changeLinkContext") mustBe "if the contract states the rights to this work belong to your organisation"
      Messages("worker.transferOfRights.changeLinkContext") mustBe "if the contract gives your client the option to buy the rights for a separate fee"
      Messages("hirer.transferOfRights.changeLinkContext") mustBe "if the contract gives your organisation the option to buy the rights for a separate fee"
      Messages("worker.previousContract.changeLinkContext") mustBe "if you have had a previous contract with this client"
      Messages("hirer.previousContract.changeLinkContext") mustBe "if the worker had a previous contract with your organisation"
      Messages("worker.followOnContract.changeLinkContext") mustBe "if this contract will start immediately after the previous one ended"
      Messages("hirer.followOnContract.changeLinkContext") mustBe "if this contract will start immediately after the previous one ended"
      Messages("worker.firstContract.changeLinkContext") mustBe "if the current contract the first in a series of contracts agreed with this client"
      Messages("hirer.firstContract.changeLinkContext") mustBe "if the current contract is the first in a series of contracts agreed with your organisation"
      Messages("worker.extendContract.changeLinkContext") mustBe "if the current contract allows for it to be extended"
      Messages("hirer.extendContract.changeLinkContext") mustBe "if the current contract allows for it to be extended"
      Messages("worker.majorityOfWorkingTime.changeLinkContext") mustBe "if this work will take up the majority of your available working time"
      Messages("hirer.majorityOfWorkingTime.changeLinkContext") mustBe "if this work will take up the majority of the worker’s available working time"
      Messages("worker.similarWorkOtherClients.changeLinkContext") mustBe "if you have done any self-employed work of a similar nature for other clients in the last 12 months"
      Messages("hirer.similarWorkOtherClients.changeLinkContext") mustBe "if the worker has done any self-employed work of a similar nature for other clients in the last 12 months"
    }
  }

  "Both files" should {

    "contain the same keys" in {
      assert(englishKeys.diff(welshKeys).isEmpty)
    }
  }
}
