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

trait ModelGenerators {

  implicit lazy val arbitraryHowWorkIsDone: Arbitrary[HowWorkIsDone] =
    Arbitrary {
      Gen.oneOf(HowWorkIsDone.values.toSeq)
    }

  implicit lazy val arbitraryScheduleOfWorkingHours: Arbitrary[ScheduleOfWorkingHours] =
    Arbitrary {
      Gen.oneOf(ScheduleOfWorkingHours.values.toSeq)
    }

  implicit lazy val arbitraryChooseWhereWork: Arbitrary[ChooseWhereWork] =
    Arbitrary {
      Gen.oneOf(ChooseWhereWork.values.toSeq)
    }

  implicit lazy val arbitraryHowWorkerIsPaid: Arbitrary[HowWorkerIsPaid] =
    Arbitrary {
      Gen.oneOf(HowWorkerIsPaid.values.toSeq)
    }

  implicit lazy val arbitraryPutRightAtOwnCost: Arbitrary[PutRightAtOwnCost] =
    Arbitrary {
      Gen.oneOf(PutRightAtOwnCost.values.toSeq)
    }

  implicit lazy val arbitraryIdentifyToStakeholders: Arbitrary[IdentifyToStakeholders] =
    Arbitrary {
      Gen.oneOf(IdentifyToStakeholders.values.toSeq)
    }

  implicit lazy val arbitraryArrangedSubstitue: Arbitrary[ArrangedSubstitue] =
    Arbitrary {
      Gen.oneOf(ArrangedSubstitue.values.toSeq)
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
