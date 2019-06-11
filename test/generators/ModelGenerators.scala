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

package generators

import models._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import viewmodels.SingleAnswerRow

trait ModelGenerators {

  implicit lazy val arbitraryAboutYou: Arbitrary[AboutYouAnswer] =
    Arbitrary {
      Gen.oneOf(AboutYouAnswer.values)
    }

  implicit lazy val arbitraryBusinessSize: Arbitrary[BusinessSize] =
    Arbitrary {
      Gen.oneOf(BusinessSize.values)
    }

  implicit lazy val arbitrarySingleAnswerRowModel: Arbitrary[SingleAnswerRow] =
    Arbitrary {
      for {
        label <- arbitrary[String]
        answer <- arbitrary[String]
        isMessageKey <- arbitrary[Boolean]
        changeUrl <- arbitrary[Option[String]]
      } yield SingleAnswerRow(label, answer, isMessageKey, changeUrl)
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
      Gen.oneOf(MoveWorker.values())
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
      Gen.oneOf(ChooseWhereWork.values())
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
      Gen.oneOf(IdentifyToStakeholders.values())
    }

  implicit lazy val arbitraryArrangedSubstitute: Arbitrary[ArrangedSubstitute] =
    Arbitrary {
      Gen.oneOf(ArrangedSubstitute.values)
    }

  implicit lazy val arbitraryCannotClaimAsExpense: Arbitrary[CannotClaimAsExpense] =
    Arbitrary {
      Gen.oneOf(CannotClaimAsExpense.values)
    }

  implicit lazy val arbitraryWorkerType: Arbitrary[WorkerType] =
    Arbitrary {
      Gen.oneOf(WorkerType.values)
    }
}
