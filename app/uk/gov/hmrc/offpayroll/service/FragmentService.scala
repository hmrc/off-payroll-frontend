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

package uk.gov.hmrc.offpayroll.service

import play.Logger
import play.twirl.api.Html



/**
  * Created by peter on 05/01/2017.
  */
class FragmentService(val sourceDir: String){

  val  personalData: Map[String, Html] = {
    val is = getClass.getResourceAsStream(sourceDir)

    val fileArray = scala.io.Source.fromInputStream(is).getLines().mkString(":").split(':')

    def htmlFromResource(filename: String ): Html = {
      Html.apply(scala.io.Source.fromInputStream(
        getClass.getResourceAsStream(filename)).getLines().mkString(""))
    }

    fileArray.map{ file => file -> htmlFromResource(sourceDir + file)}.toMap

  }

  def getFragmentByName(name: String): Html = {

    Logger.debug("FragmentService getting fragment for " + name)
    personalData.getOrElse(name + ".html", Html.apply(""))
  }

}

object FragmentService {

  def apply(sourceDir: String): FragmentService = {
    new FragmentService(sourceDir)
  }




}
