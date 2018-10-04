import sbt._

object FrontendBuild extends Build with MicroService {

  val appName = "off-payroll-frontend"

  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
}

private object AppDependencies {
  import play.sbt.PlayImport._
  import play.core.PlayVersion

  private val bootstrapPlay25Version = "3.8.0"
  private val govukTemplateVersion = "5.22.0"
  private val playUIVersion = "7.22.0"
  private val playPartialsVersion = "6.1.0"
  private val hmrcTestVersion = "3.1.0"
  private val scalaTestVersion = "3.0.3"
  private val pegdownVersion = "1.6.0"
  private val jacksonVersion = "2.9.6"

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "bootstrap-play-25" % bootstrapPlay25Version,
    "uk.gov.hmrc" %% "govuk-template" % govukTemplateVersion,
    "uk.gov.hmrc" %% "play-ui" % playUIVersion,
    "uk.gov.hmrc" %% "play-partials" % playPartialsVersion,
    "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
    "ai.x" %% "play-json-extensions" % "0.8.0"
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test : Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % scope,
        "org.scalatest" %% "scalatest" % scalaTestVersion % scope,
        "org.pegdown" % "pegdown" % pegdownVersion % scope,
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
        "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
        "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
        "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
        "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
        "org.mockito" % "mockito-core" % "2.13.0",
        "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % scope


      )
    }.test
  }

  def apply() = compile ++ Test()

}