/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

import models.sections.control.ScheduleOfWorkingHours
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class ScheduleOfWorkingHoursSpec extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "ScheduleOfWorkingHours" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(ScheduleOfWorkingHours.values)

      forAll(gen) {
        scheduleOfWorkingHours =>

          JsString(scheduleOfWorkingHours.toString).validate[ScheduleOfWorkingHours].asOpt.value mustEqual scheduleOfWorkingHours
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!ScheduleOfWorkingHours.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[ScheduleOfWorkingHours] mustEqual JsError("Unknown scheduleOfWorkingHours")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(ScheduleOfWorkingHours.values)

      forAll(gen) {
        scheduleOfWorkingHours =>

          Json.toJson(scheduleOfWorkingHours) mustEqual JsString(scheduleOfWorkingHours.toString)
      }
    }
  }
}
