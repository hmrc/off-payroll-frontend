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

package filters

import java.util.regex.Pattern
import java.util.regex.Pattern._
import java.util.regex.Pattern.compile

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
