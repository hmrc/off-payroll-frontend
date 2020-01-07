/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object IdentifyToStakeholdersMessages extends BaseMessages {

    object Worker {
      val error = "Select how you would introduce yourself to your client’s consumers or suppliers"
      val heading = "How would you introduce yourself to your client’s consumers or suppliers?"
      val title = heading
      val workForEndClient = "You work for your client"
      val workAsIndependent = "You are an independent worker acting on your client’s behalf"
      val workAsBusiness = "You work for your own business"
      val wouldNotHappen = "This would not happen"
    }
    object Hirer {
      val error = "Select how the worker would introduce themselves to your consumers or suppliers"
      val heading = "How would the worker introduce themselves to your consumers or suppliers?"
      val title = heading
      val workForEndClient = "They work for you"
      val workAsIndependent = "They are an independent worker acting on your behalf"
      val workAsBusiness = "They work for their own business"
      val wouldNotHappen = "This would not happen"
    }
}
