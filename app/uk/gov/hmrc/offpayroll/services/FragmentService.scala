/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.services

import play.Logger
import play.twirl.api.Html



class FragmentService(val sourceDir: String){

  private lazy val fragments: Map[String, Html] = {

    val is = getClass.getResourceAsStream(sourceDir)
    val fileArray = scala.io.Source.fromInputStream(is).getLines().mkString(":").split(':')

    def htmlFromResource(filename: String ): Html = {
      Html.apply(scala.io.Source.fromInputStream(
        getClass.getResourceAsStream(filename)).getLines().mkString(""))
    }

    fileArray.map{file => file -> htmlFromResource(sourceDir + file)}.toMap

  }


  def getFragmentByName(name: String): Html = {
    Logger.debug("FragmentService getting fragment for " + name)
    fragments.getOrElse(name + ".html", Html.apply(""))
  }

  def getAllFragmentsForInterview(interview: Map[String, String]): Map[String, Html] = {
    fragments.filter {
      case (filename, _) => interview.exists {
        case (question, _) => question + ".html" == filename
      }
    }.map {
      case (filename, html) =>  (filename.replace(".html", ""), html)
    }
  }


}

object FragmentService {

  def apply(sourceDir: String): FragmentService = {
    new FragmentService(sourceDir)
  }

}
