/*
 * Copyright 2018 HM Revenue & Customs
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

import java.io.File

import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Suite}
import play.api._
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceableModule}
import play.api.test.Helpers._

/**
  * Created by work on 18/01/2017.
  */

trait WithTestFakeApplication extends BeforeAndAfterAll {
  this: Suite =>

  def configFile: String

  lazy val fakeApplication = new GuiceApplicationBuilder()
    .loadConfig(new Configuration(ConfigFactory.load(configFile)))
    .bindings(bindModules:_*).build()

  def bindModules: Seq[GuiceableModule] = Seq()

  override def beforeAll() {
    super.beforeAll()
    Play.start(fakeApplication)
  }

  override def afterAll() {
    super.afterAll()
    Play.stop(fakeApplication)
  }

  def evaluateUsingPlay[T](block: => T): T = {
    running(fakeApplication) {
      block
    }
  }

}
