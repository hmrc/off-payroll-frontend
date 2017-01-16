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

package uk.gov.hmrc.offpayroll

import java.util.Properties

/**
  * Created by peter on 11/12/2016.
  */
object PropertyFileLoader {

  def getMessagesFileAsMap: Map[String, String] = {
    import scala.collection.JavaConverters._

    val props = new Properties

    val fileStream = this.getClass.getResourceAsStream("/messages")
    props.load(fileStream)
    fileStream.close()

    props.asScala.toMap
  }

  def getMessagesForACluster(clusterName: String): Map[String, String] =
    this.getMessagesFileAsMap.filterKeys{key =>
      val keyParts = key.split('.')
      keyParts(0) == clusterName
    }

  def transformMapToAListOfAnswers(map: Map[String, String]) =
    map.foldLeft[List[(String, String)]](Nil)((currentList,prop) => {(prop._1, "Yes") :: currentList})

  def transformMapFromQuestionTextToAnswers(clusterName: String):Map[String, String] = {
    this.getMessagesForACluster(clusterName).map(properties => (properties._1, "Yes"))
  }

  def transformMapFromQuestionTextToAnswersDropChildren(clusterName: String):Map[String, String] = {

    this.getMessagesForACluster(clusterName).map{
      case (question, answer) => (question.split('.'), answer)
    }.map{
      case (questionArray, answer) => {
        if(questionArray.size > 1) (questionArray(0) + "." + questionArray(1), answer)
        else if(questionArray.size == 1) (questionArray(0), answer)
        else ("", answer)
      }
    }.toSet.toMap
  }



}
