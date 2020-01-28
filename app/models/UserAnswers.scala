/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import config.featureSwitch.FeatureSwitching
import pages._
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap

case class UserAnswers(cacheMap: CacheMap) extends Enumerable.Implicits with FeatureSwitching {

  def get[A](page: QuestionPage[A])(implicit rds: Reads[A]): Option[A] = cacheMap.getEntry[A](page)

  def set[A](page: QuestionPage[A], value: A)(implicit writes: Writes[A]): UserAnswers =
    UserAnswers(cacheMap copy (data = cacheMap.data + (page.toString -> Json.toJson(value))))

  def remove[A](page: QuestionPage[A]): UserAnswers = UserAnswers(cacheMap copy (data = cacheMap.data - page))

}

object UserAnswers {

  def apply(cacheId: String): UserAnswers =
    UserAnswers(new CacheMap(cacheId, Map()))
}
