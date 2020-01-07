/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package connectors

import base.SpecBase
import generators.Generators
import org.mockito.Matchers.{eq => eqTo, _}
import org.mockito.Mockito._
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.JsString
import repositories.SessionRepository
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.Future

class MongoCacheConnectorSpec extends SpecBase with ScalaCheckPropertyChecks with Generators with MockitoSugar with ScalaFutures with OptionValues {

  val mockSessionRepository = mock[SessionRepository]

  ".save" must {

    "save the cache map to the Mongo repository" in {

      when(mockSessionRepository.upsert(any[CacheMap])) thenReturn Future.successful(true)

      val mongoCacheConnector = new MongoCacheConnector(mockSessionRepository)

      forAll(arbitrary[CacheMap]) {
        cacheMap =>

          val result = mongoCacheConnector.save(cacheMap)

          whenReady(result) {
            savedCacheMap =>

              savedCacheMap mustEqual cacheMap
              verify(mockSessionRepository).upsert(cacheMap)
          }
      }
    }
  }

  ".fetch" when {

    "there isn't a record for this key in Mongo" must {

      "return None" in {

        when(mockSessionRepository.get(any())) thenReturn Future.successful(None)

        val mongoCacheConnector = new MongoCacheConnector(mockSessionRepository)

        forAll(nonEmptyString) {
          cacheId =>

            val result = mongoCacheConnector.fetch(cacheId)

            whenReady(result) {
              optionalCacheMap =>

                optionalCacheMap must be(empty)
            }
        }
      }
    }

    "a record exists for this key" must {

      "return the record" in {

        val mongoCacheConnector = new MongoCacheConnector(mockSessionRepository)

        forAll(arbitrary[CacheMap]) {
          cacheMap =>

            when(mockSessionRepository.get(eqTo(cacheMap.id))) thenReturn Future.successful(Some(cacheMap))

            val result = mongoCacheConnector.fetch(cacheMap.id)

            whenReady(result) {
              optionalCacheMap =>

                optionalCacheMap.value mustEqual cacheMap
            }
        }
      }
    }
  }

  ".getEntry" when {

    "there isn't a record for this key in Mongo" must {

      "return None" in {

        when(mockSessionRepository.get(any())) thenReturn Future.successful(None)

        val mongoCacheConnector = new MongoCacheConnector(mockSessionRepository)

        forAll(nonEmptyString, nonEmptyString) {
          (cacheId, key) =>

            val result = mongoCacheConnector.getEntry[String](cacheId, key)

            whenReady(result) {
              optionalValue =>

                optionalValue must be(empty)
            }
        }
      }
    }

    "a record exists in Mongo but this key is not present" must {

      "return None" in {

        val mongoCacheConnector = new MongoCacheConnector(mockSessionRepository)

        val gen = for {
          key      <- nonEmptyString
          cacheMap <- arbitrary[CacheMap]
        } yield (key, cacheMap copy (data = cacheMap.data - key))

        forAll(gen) {
          case (key, cacheMap) =>

            when(mockSessionRepository.get(eqTo(cacheMap.id))) thenReturn Future.successful(Some(cacheMap))

            val result = mongoCacheConnector.getEntry[String](cacheMap.id, key)

            whenReady(result) {
              optionalValue =>

                optionalValue must be(empty)
            }
        }
      }
    }

    "a record exists in Mongo with this key" must {

      "return the key's value" in {

        val mongoCacheConnector = new MongoCacheConnector(mockSessionRepository)

        val gen = for {
          key      <- nonEmptyString
          value    <- nonEmptyString
          cacheMap <- arbitrary[CacheMap]
        } yield (key, value, cacheMap copy (data = cacheMap.data + (key -> JsString(value))))

        forAll(gen) {
          case (key, value, cacheMap) =>

            when(mockSessionRepository.get(eqTo(cacheMap.id))) thenReturn Future.successful(Some(cacheMap))

            val result = mongoCacheConnector.getEntry[String](cacheMap.id, key)

            whenReady(result) {
              optionalValue =>

                optionalValue.value mustEqual value
            }
        }
      }
    }
  }
}
