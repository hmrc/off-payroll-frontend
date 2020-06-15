import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val wireMockVersion = "2.26.3"

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "simple-reactivemongo" % "7.27.0-play-26",
    "uk.gov.hmrc" %% "logback-json-logger" % "4.8.0",
    "uk.gov.hmrc" %% "govuk-template" % "5.55.0-play-26",
    "uk.gov.hmrc" %% "play-health" % "3.15.0-play-26",
    "uk.gov.hmrc" %% "play-ui" % "8.11.0-play-26",
    "uk.gov.hmrc" %% "http-caching-client" % "9.1.0-play-26",
    "uk.gov.hmrc" %% "play-conditional-form-mapping" % "1.2.0-play-26",
    "uk.gov.hmrc" %% "bootstrap-play-26" % "1.8.0",
    "uk.gov.hmrc" %% "play-whitelist-filter" % "3.4.0-play-26",
    "com.typesafe.play" %% "play-json-joda" % "2.6.14"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "hmrctest" % "3.9.0-play-26",
    "uk.gov.hmrc" %% "reactivemongo-test" % "4.19.0-play-26",
    "org.scalatest" %% "scalatest" % "3.0.8",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
    "org.pegdown" % "pegdown" % "1.6.0",
    "org.jsoup" % "jsoup" % "1.13.1",
    "com.typesafe.play" %% "play-test" % PlayVersion.current,
    "org.mockito" % "mockito-all" % "1.10.19",
    "org.scalacheck" %% "scalacheck" % "1.14.3",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0",
    "com.github.tomakehurst" % "wiremock-jre8" % wireMockVersion,
    "com.github.tomakehurst" % "wiremock-standalone" % wireMockVersion
  ).map(_ % "test, it")

  def tmpMacWorkaround(): Seq[ModuleID] =
    if (sys.props.get("os.name").exists(_.toLowerCase.contains("mac")))
      Seq("org.reactivemongo" % "reactivemongo-shaded-native" % "0.20.11-osx-x86-64" % "runtime,test,it")
    else Seq()

  def apply() = compile ++ test ++ tmpMacWorkaround
}
