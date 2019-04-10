import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "simple-reactivemongo" % "7.16.0-play-26",
    "uk.gov.hmrc" %% "logback-json-logger" % "4.4.0",
    "uk.gov.hmrc" %% "govuk-template" % "5.30.0-play-26",
    "uk.gov.hmrc" %% "play-health" % "3.12.0-play-26",
    "uk.gov.hmrc" %% "play-ui" % "7.37.0-play-26",
    "uk.gov.hmrc" %% "http-caching-client" % "8.1.0",
    "uk.gov.hmrc" %% "play-conditional-form-mapping" % "0.2.0",
    "uk.gov.hmrc" %% "bootstrap-play-26" % "0.37.0",
    "uk.gov.hmrc" %% "play-language" % "3.4.0",
    "uk.gov.hmrc" %% "play-whitelist-filter" % "2.0.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "hmrctest" % "3.6.0-play-26",
    "uk.gov.hmrc" %% "reactivemongo-test" % "4.10.0-play-26",
    "org.scalatest" %% "scalatest" % "3.0.7",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
    "org.pegdown" % "pegdown" % "1.6.0",
    "org.jsoup" % "jsoup" % "1.10.3",
    "com.typesafe.play" %% "play-test" % PlayVersion.current,
    "org.mockito" % "mockito-all" % "1.10.19",
    "org.scalacheck" %% "scalacheck" % "1.13.4",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0"
  ).map(_ % Test)

  def apply() = compile ++ test
}
