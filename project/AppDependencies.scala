import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val wireMockVersion = "2.27.2"

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "simple-reactivemongo" % "8.0.0-play-27",
    "uk.gov.hmrc" %% "logback-json-logger" % "5.1.0",
    "uk.gov.hmrc" %% "govuk-template" % "5.66.0-play-27",
    "uk.gov.hmrc" %% "play-health" % "3.16.0-play-27",
    "uk.gov.hmrc" %% "play-ui" % "9.1.0-play-27",
    "uk.gov.hmrc" %% "http-caching-client" % "9.2.0-play-27",
    "uk.gov.hmrc" %% "play-conditional-form-mapping" % "1.6.0-play-26",
    "uk.gov.hmrc" %% "bootstrap-frontend-play-27" % "3.4.0",
    "uk.gov.hmrc" %% "play-allowlist-filter" % "1.0.0-play-27",
    "com.typesafe.play" %% "play-json-joda" % "2.7.4",
    "uk.gov.hmrc" %% "digital-engagement-platform-chat" % "0.15.0-play-27"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "service-integration-test" % "1.1.0-play-27",
    "uk.gov.hmrc" %% "reactivemongo-test" % "5.0.0-play-27",
    "org.scalatest" %% "scalatest" % "3.3.0-SNAP3",
    "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3",
    "org.scalatestplus" %% "scalatestplus-mockito" % "1.0.0-SNAP5",
    "org.scalatestplus" %% "scalacheck-1-14" % "3.2.2.0",
    "org.pegdown" % "pegdown" % "1.6.0",
    "org.jsoup" % "jsoup" % "1.13.1",
    "com.typesafe.play" %% "play-test" % PlayVersion.current,
    "org.mockito" % "mockito-all" % "2.0.2-beta",
    "org.scalacheck" %% "scalacheck" % "1.15.4",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0",
    "com.github.tomakehurst" % "wiremock-jre8" % wireMockVersion,
    "com.github.tomakehurst" % "wiremock-standalone" % wireMockVersion,
    "com.vladsch.flexmark" % "flexmark-all" % "0.35.10"
  ).map(_ % "test, it")
  
  def apply() = compile ++ test
}
