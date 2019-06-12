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

object IdentifyToStakeholdersMessages extends BaseMessages {

  val subheading = "About the worker’s integration into the organisation"

  object Optimised {
    object Worker {
      val heading = "If you were to formally interact with your client’s external consumers or suppliers, how would you introduce yourself?"
      val title = heading
      val workForEndClient = "You work for your client’s organisation"
      val workAsIndependent = "You are an independent worker acting on behalf of your client"
      val workAsBusiness = "You work for your own business"
      val wouldNotHappen = "This wouldn’t happen - you wouldn’t interact formally"
    }
    object Hirer {
      val heading = "If the worker was to formally interact with your consumers or suppliers, how would they introduce themselves?"
      val title = heading
      val workForEndClient = "They work for you"
      val workAsIndependent = "They are an independent worker acting on your behalf"
      val workAsBusiness = "They work for their own business"
      val wouldNotHappen = "This wouldn’t happen - they wouldn’t interact formally"
    }
  }

  object Worker {
    val heading = "When you interact with the end client’s customers, clients, audience or users, how do you identify yourself?"
    val title = heading
    val workForEndClient = "I work for the end client"
    val workAsIndependent = "I am an independent worker acting on behalf of the end client"
    val workAsBusiness = "I work for my own business"
  }

  object Hirer {
    val heading = "When the worker interacts with your customers, clients, audience or users, how do they identify themselves?"
    val title = heading
    val workForEndClient = "They work for you"
    val workAsIndependent = "They are an independent worker acting on your behalf"
    val workAsBusiness = "They work for their own business"
  }

  object NonTailored {
    val heading = "When the worker interacts with the end client’s customers, clients, audience or users, how do they identify themselves?"
    val title = heading
    val workForEndClient = "They work for the end client"
    val workAsIndependent = "They are an independent worker acting on behalf of the end client"
    val workAsBusiness = "They work for their own business"
  }

}
