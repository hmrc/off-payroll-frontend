/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package filters

import java.util.regex.Pattern
import java.util.regex.Pattern.{compile, _}

import scala.annotation.tailrec

trait InputFilter {
  val filters: Seq[Pattern] = Seq(
    compile("<script>(.*?)</script>", CASE_INSENSITIVE),
    compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", CASE_INSENSITIVE | MULTILINE | DOTALL),
    compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", CASE_INSENSITIVE | MULTILINE | DOTALL),
    compile("<script(.*?)>", CASE_INSENSITIVE | MULTILINE | DOTALL),
    compile("</script>", CASE_INSENSITIVE),
    compile("eval\\((.*?)\\)", CASE_INSENSITIVE | MULTILINE | DOTALL),
    compile("expression\\((.*?)\\)", CASE_INSENSITIVE | MULTILINE | DOTALL),
    compile("javascript:", CASE_INSENSITIVE),
    compile("vbscript:", CASE_INSENSITIVE),
    compile("onload(.*?)=", CASE_INSENSITIVE | MULTILINE | DOTALL)
  )

  def filter(input: String): String = {
    @tailrec
    def applyFilters(filters: Seq[Pattern], sanitizedOuput: String): String = filters match {
      case Nil => sanitizedOuput
      case filter :: tail => applyFilters(tail, filter.matcher(sanitizedOuput).replaceAll(""))
    }
    applyFilters(filters, input)
  }
}
