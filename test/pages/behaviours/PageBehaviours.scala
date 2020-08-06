/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.behaviours

import generators.Generators
import models.UserAnswers
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.QuestionPage
import play.api.libs.json.{Json, Reads, Writes, _}
import uk.gov.hmrc.http.cache.client.CacheMap

trait PageBehaviours extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks with Generators with OptionValues {

  class BeRetrievable[A] {
    def apply[P <: QuestionPage[A]](genP: Gen[P])(implicit ev1: Arbitrary[A], ev2: Format[A], ev3: Reads[A], ev4: Writes[A]): Unit = {

      "return None" when {

        "being retrieved from UserAnswers" when {

          "the question has not been answered" in {

            val gen = for {
              page     <- genP
              cacheMap <- arbitrary[CacheMap]
            } yield (page, cacheMap copy (data = cacheMap.data - page.toString))

            forAll(gen) {
              case (page, cacheMap) =>

                whenever(!cacheMap.data.keySet.contains(page.toString)) {

                  val userAnswers = UserAnswers(cacheMap)
                  userAnswers.get(page) must be(empty)
                }
            }
          }
        }
      }

      "return the saved value" when {

        "being retrieved from UserAnswers" when {

          "the question has been answered" in {

            val gen = for {
              page       <- genP
              savedValue <- arbitrary[A]
              cacheMap   <- arbitrary[CacheMap]
            } yield (page, savedValue, cacheMap copy (data = cacheMap.data + (page.toString -> Json.toJson(savedValue))))

            forAll(gen) {
              case (page, savedValue, cacheMap) =>

                val userAnswers = UserAnswers(cacheMap)
                userAnswers.get(page).value mustEqual savedValue
            }
          }
        }
      }
    }
  }

  class BeSettable[A] {
    def apply[P <: QuestionPage[A]](genP: Gen[P])(implicit ev1: Arbitrary[A], ev2: Format[A], ev3: Reads[A], ev4: Writes[A]): Unit = {

      "be able to be set on UserAnswers" in {

        val gen = for {
          page     <- genP
          newValue <- arbitrary[A]
          cacheMap <- arbitrary[CacheMap]
        } yield (page, newValue, cacheMap)

        forAll(gen) {
          case (page, newValue, cacheMap) =>

            val userAnswers = UserAnswers(cacheMap)
            val updatedAnswers = userAnswers.set(page, newValue)
            updatedAnswers.get(page).value mustEqual newValue
        }
      }
    }
  }

  class BeRemovable[A] {

    def apply[P <: QuestionPage[A]](genP: Gen[P])(implicit ev1: Arbitrary[A], ev2: Format[A], ev3: Reads[A], ev4: Writes[A]): Unit = {

      "be able to be removed from UserAnswers" in {

        val gen = for {
          page       <- genP
          savedValue <- arbitrary[A]
          cacheMap   <- arbitrary[CacheMap]
        } yield (page, cacheMap copy (data = cacheMap.data + (page.toString -> Json.toJson(savedValue))))

        forAll(gen) {
          case (page, cacheMap)=>

            val userAnswers = UserAnswers(cacheMap)
            val updatedAnswers = userAnswers.remove(page)
            updatedAnswers.get(page) must be(empty)
        }
      }
    }
  }

  def beRetrievable[A]: BeRetrievable[A] = new BeRetrievable[A]

  def beSettable[A]: BeSettable[A] = new BeSettable[A]

  def beRemovable[A]: BeRemovable[A] = new BeRemovable[A]
}