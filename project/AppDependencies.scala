import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "simple-reactivemongo" % "7.20.0-play-26",
    "uk.gov.hmrc" %% "logback-json-logger" % "4.6.0",
    "uk.gov.hmrc" %% "govuk-template" % "5.37.0-play-26",
    "uk.gov.hmrc" %% "play-health" % "3.14.0-play-26",
    "uk.gov.hmrc" %% "play-ui" % "7.40.0-play-26",
    "uk.gov.hmrc" %% "http-caching-client" % "8.4.0-play-26",
    "uk.gov.hmrc" %% "play-conditional-form-mapping" % "1.1.0-play-26",
    "uk.gov.hmrc" %% "bootstrap-play-26" % "0.42.0",
    "uk.gov.hmrc" %% "play-whitelist-filter" % "3.1.0-play-26",
    "ai.x" %% "play-json-extensions" % "0.30.1",
    "com.typesafe.play" %% "play-json-joda" % "2.6.0",
    "org.typelevel" %% "cats-core" % "2.0.0-M1"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "hmrctest" % "3.9.0-play-26",
    "uk.gov.hmrc" %% "reactivemongo-test" % "4.15.0-play-26",
    "org.scalatest" %% "scalatest" % "3.0.8",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
    "org.pegdown" % "pegdown" % "1.6.0",
    "org.jsoup" % "jsoup" % "1.12.1",
    "com.typesafe.play" %% "play-test" % PlayVersion.current,
    "org.mockito" % "mockito-all" % "1.10.19",
    "org.scalacheck" %% "scalacheck" % "1.14.0",
    "com.github.tomakehurst" % "wiremock-jre8" % "2.21.0",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0",
    "com.github.tomakehurst" % "wiremock-standalone" % "2.22.0"
  ).map(_ % "test, it")

  def apply() = compile ++ test
}
