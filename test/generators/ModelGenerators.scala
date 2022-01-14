/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package generators

import models._
import models.sections.businessOnOwnAccount.{ExclusiveContract, MultipleEngagements, SeriesOfContracts, TransferRights}
import models.sections.control.{ChooseWhereWork, HowWorkIsDone, MoveWorker, ScheduleOfWorkingHours}
import models.sections.financialRisk.{HowWorkerIsPaid, PutRightAtOwnCost}
import models.sections.partAndParcel.IdentifyToStakeholders
import models.sections.personalService.ArrangedSubstitute
import models.sections.setup.WhatDoYouWantToFindOut
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import viewmodels.SingleAnswerRow

trait ModelGenerators {

  implicit lazy val whatDoYouWantToFindOut: Arbitrary[WhatDoYouWantToFindOut] =
    Arbitrary {
      Gen.oneOf(WhatDoYouWantToFindOut.values)
    }

  implicit lazy val arbitrarySingleAnswerRowModel: Arbitrary[SingleAnswerRow] =
    Arbitrary {
      for {
        label <- arbitrary[String]
        answer <- arbitrary[String]
        isMessageKey <- arbitrary[Boolean]
        changeUrl <- arbitrary[Option[String]]
        changeLinkContext <- arbitrary[Option[String]]
      } yield SingleAnswerRow(label, answer, isMessageKey, changeUrl, changeLinkContext)
    }

  implicit lazy val arbitraryAdditionalPdfDetails: Arbitrary[AdditionalPdfDetails] =
    Arbitrary {
      for {
        name <- arbitrary[Option[String]]
        client <- arbitrary[Option[String]]
        job <- arbitrary[Option[String]]
        reference <- arbitrary[Option[String]]
      } yield AdditionalPdfDetails(name, client, job, reference)
    }

  implicit lazy val arbitraryMoveWorker: Arbitrary[MoveWorker] =
    Arbitrary {
      Gen.oneOf(MoveWorker.values)
    }

  implicit lazy val arbitraryHowWorkIsDone: Arbitrary[HowWorkIsDone] =
    Arbitrary {
      Gen.oneOf(HowWorkIsDone.values)
    }

  implicit lazy val arbitraryScheduleOfWorkingHours: Arbitrary[ScheduleOfWorkingHours] =
    Arbitrary {
      Gen.oneOf(ScheduleOfWorkingHours.values)
    }

  implicit lazy val arbitraryChooseWhereWork: Arbitrary[ChooseWhereWork] =
    Arbitrary {
      Gen.oneOf(ChooseWhereWork.values)
    }

  implicit lazy val arbitraryHowWorkerIsPaid: Arbitrary[HowWorkerIsPaid] =
    Arbitrary {
      Gen.oneOf(HowWorkerIsPaid.values)
    }

  implicit lazy val arbitraryPutRightAtOwnCost: Arbitrary[PutRightAtOwnCost] =
    Arbitrary {
      Gen.oneOf(PutRightAtOwnCost.values)
    }

  implicit lazy val arbitraryIdentifyToStakeholders: Arbitrary[IdentifyToStakeholders] =
    Arbitrary {
      Gen.oneOf(IdentifyToStakeholders.values)
    }

  implicit lazy val arbitraryArrangedSubstitute: Arbitrary[ArrangedSubstitute] =
    Arbitrary {
      Gen.oneOf(ArrangedSubstitute.values)
    }

  implicit lazy val arbitraryExclusiveContract: Arbitrary[ExclusiveContract] =
    Arbitrary {
      Gen.oneOf(ExclusiveContract.values)
    }

  implicit lazy val arbitrarySeriesOfContracts: Arbitrary[SeriesOfContracts] =
    Arbitrary {
      Gen.oneOf(SeriesOfContracts.values)
    }

  implicit lazy val arbitraryTransferRights: Arbitrary[TransferRights] =
    Arbitrary {
      Gen.oneOf(TransferRights.values)
    }

  implicit lazy val arbitraryMultipleEngagements: Arbitrary[MultipleEngagements] =
    Arbitrary {
      Gen.oneOf(MultipleEngagements.values)
    }

}
