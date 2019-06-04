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

package assets.messages

object OfficeHolderMessages extends BaseMessages {

  val subheading = "About the worker’s duties"

  object Optimised {
    object Worker {
      val heading = "During the course of this work, will you ever have to act for your client as any of the following?"
      val title = heading
      val b1 = "A board member"
      val b2 = "A treasurer"
      val b3 = "A trustee"
      val b4 = "Company director"
      val b5 = "Company secretary"
      val b6 = "Other office holder position"
    }

    object Hirer {
      val heading = "During the course of this work, will the worker ever have to act for your organisation as any of the following:"
      val title = heading
      val b1 = "A board member"
      val b2 = "A treasurer"
      val b3 = "A trustee"
      val b4 = "Company director"
      val b5 = "Company secretary"
      val b6 = "Other office holder position"
    }
  }

  object Worker {
    val heading = "Will you perform office holder duties for the end client as part of this engagement?"
    val title = heading
    val p1 = "Being an office holder is not about the physical place where the work is done, it’s about your responsibilities within the organisation. Office holders can be appointed on a permanent or temporary basis."
    val p2 = "This engagement will include performing office holder duties for the end client, if:"
    val b1 = "You have a position of responsibility for the end client, including board membership or statutory board membership, or being appointed as a treasurer, trustee, company director, company secretary, or other similar statutory roles"
    val b2 = "The role is created by statute, articles of association, trust deed or from documents that establish your organisation (a director or company secretary, for example)"
    val b3 = "The role exists even if someone is not engaged to fill it (a club treasurer, for example)"
    val exclamation = "If you are not sure if these things apply, please ask the end client’s management about their organisational structure."
  }

  object Hirer {
    val heading = "Will the worker (or their business) perform office holder duties for you as part of this engagement?"
    val title = heading
    val p1 = "Being an office holder is not about the physical place where the work is done, it’s about the worker’s responsibilities within your organisation. Office holders can be appointed on a permanent or temporary basis."
    val p2 = "This engagement will include performing office holder duties for you, if:"
    val b1 = "The worker has a position of responsibility for you, including board membership or statutory board membership, or being appointed as a treasurer, trustee, company director, company secretary, or other similar statutory roles"
    val b2 = "The role is created by statute, articles of association, trust deed or from documents that establish your organisation (a director or company secretary, for example)"
    val b3 = "The role exists even if someone is not engaged to fill it (a club treasurer, for example)"
    val exclamation = "If you are not sure if these things apply, please ask your management about your organisational structure."
  }

  object NonTailored {
    val heading = "Will the worker (or their business) perform office holder duties for the end client as part of this engagement?"
    val title = heading
    val p1 = "Being an office holder is not about the physical place where the work is done, it is about the worker’s responsibilities within the organisation. Office holders can be appointed on a permanent or temporary basis."
    val p2 = "This engagement will include performing office holder duties for the end client, if:"
    val b1 = "the worker has a position of responsibility for the end client, including board membership or statutory board membership, or being appointed as a treasurer, trustee, company director, company secretary, or other similar statutory roles"
    val b2 = "the role is created by statute, articles of association, trust deed or from documents that establish an organisation (a director or company secretary, for example)"
    val b3 = "the role exists even if someone is not engaged to fill it (a club treasurer, for example)"
    val exclamation = "If you are not sure if these things apply, please ask the end client’s management about their organisational structure."
  }

}
