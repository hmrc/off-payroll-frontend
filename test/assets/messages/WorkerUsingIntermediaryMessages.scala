/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package assets.messages

object WorkerUsingIntermediaryMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you provide your services through a limited company, partnership or unincorporated association"
    val heading = "Do you provide your services through a limited company, partnership or unincorporated association?"
    val title = heading
    val p1 = "These are also known as intermediaries."
    val p2 = "An ‘unincorporated association’ is an organisation set up by a group of people for a reason other than to make a profit. For example, a voluntary group or a sports club."

  }

  object Hirer {
    val error = "Select yes if the worker provides their services through a limited company, partnership or unincorporated association"
    val heading = "Does the worker provide their services through a limited company, partnership or unincorporated association?"
    val title = heading
    val p1 = "These are also known as intermediaries."
    val p2 = "An ‘unincorporated association’ is an organisation set up by a group of people for a reason other than to make a profit. For example, a voluntary group or a sports club."
  }

}
