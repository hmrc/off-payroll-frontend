/*
 * Copyright 2018 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.util

import uk.gov.hmrc.offpayroll.models.Element

/**
  * Created by peter on 21/12/2016.
  */
object ClusterAndQuestion {

  def unapply(tag:String):Option[(String,String)] = {
    if (tag.split('.').length > 1) Some(tag.split('.')(0),tag.split('.')(1))
    else None
  }

  def unapply(element: Element):Option[(String,String)] = element match {
    case null => throw new IllegalArgumentException("Element cannot be null")
    case _ => this.unapply(element.questionTag)
  }

}
