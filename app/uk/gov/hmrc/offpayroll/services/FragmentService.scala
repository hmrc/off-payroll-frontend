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
import play.api.i18n.Lang
import play.twirl.api.Html



class FragmentService(val sourceDirs: Seq[String]){

  private lazy val defaultFragments: Map[String, Html] = loadFragments("en")
  private lazy val cyFragments: Map[String, Html] = loadFragments("cy")

  private def loadFragments(localeCode: String): Map[String, Html] = {
    var fragmentsMap: Map[String, Html] = Map()

    sourceDirs.foreach{sourceDir =>
      val directoryName = if (localeCode.equals("cy")) sourceDir + s".${localeCode}/" else sourceDir+"/"
      val is = getClass.getResourceAsStream(directoryName)
      val fileArray = scala.io.Source.fromInputStream(is).getLines().mkString(":").split(':')

      def htmlFromResource(filename: String ): Html = {
        Html.apply(scala.io.Source.fromInputStream(
          getClass.getResourceAsStream(filename)).getLines().mkString(""))
      }

      fragmentsMap = fragmentsMap ++ fileArray.map{file => file -> htmlFromResource(directoryName+ file)}.toMap
    }
    fragmentsMap

  }

  private def getFragments(lang: Lang): Map[String, Html] = lang.code match {
    case "cy" => cyFragments
    case _ => defaultFragments
  }

  def getFragmentByName(name: String)(implicit lang: Lang): Html = {
    Logger.debug("FragmentService getting fragment for " + name)
    getFragments(lang).getOrElse(name + ".html", Html.apply(""))
  }

  def getAllFragmentsForInterview(interview: Map[String, String])(implicit lang: Lang): Map[String, Html] = {
    getFragments(lang).filter {
      case (filename, _) => interview.exists {
        case (question, _) => question + ".html" == filename
      }
    }.map {
      case (filename, html) =>  (filename.replace(".html", ""), html)
    }
  }

  def getFragmentsByFilenamePrefix(prefix: String)(implicit lang: Lang): Map[String, Html] = {
    getFragments(lang).filter {
      case (filename, _) => filename.startsWith(prefix)
    }.map {
      case (filename, html) =>  (filename.replace(".html", ""), html)
    }
  }


}

object FragmentService {

  def apply(sourceDir: String*): FragmentService = {
    new FragmentService(sourceDir)
  }

}
