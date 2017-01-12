package uk.gov.hmrc.offpayroll.util

import play.api.i18n.Messages

/**
  * Created by peter on 12/01/2017.
  */
object ViewHelper {

  def formatValue(messages: Messages, value: String): String = {
    if(value.toUpperCase != "YES" && value.toUpperCase != "YES") messages(value)
    else value
  }

}
