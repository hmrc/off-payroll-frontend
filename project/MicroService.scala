import sbt.Keys._
import sbt._
import play.routes.compiler.StaticRoutesGenerator
import sbtassembly.AssemblyKeys.{assembly, assemblyJarName, assemblyExcludedJars}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._


trait MicroService {

  import uk.gov.hmrc._
  import DefaultBuildSettings._
  import uk.gov.hmrc.{SbtBuildInfo, ShellPrompt, SbtAutoBuildPlugin}
  import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
  import uk.gov.hmrc.versioning.SbtGitVersioning



  val appName: String

  lazy val appDependencies : Seq[ModuleID] = ???
  lazy val plugins : Seq[Plugins] = Seq.empty
  lazy val playSettings : Seq[Setting[_]] = Seq.empty


  lazy val scoverageSettings = {
    import scoverage._
    Seq(
      // Semicolon-separated list of regexs matching classes to exclude
      ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;config.*;.*(AuthService|BuildInfo|Routes).*;.*views.*",
      ScoverageKeys.coverageMinimum := 80,
      ScoverageKeys.coverageFailOnMinimum := false,
      ScoverageKeys.coverageHighlighting := true
    )
  }

  lazy val microservice = Project(appName, file("."))
    .enablePlugins(Seq(play.sbt.PlayScala,SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin) ++ plugins : _*)
    .settings(playSettings : _*)
    .settings(scalaSettings: _*)
    .settings(playSettings ++ scoverageSettings : _*)
    .settings(publishingSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(
      scalaVersion := "2.11.12",
      libraryDependencies ++= appDependencies,
      retrieveManaged := true,
      evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false)
    )
    .configs(IntegrationTest)
    .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
    .settings(integrationTestSettings())
    .settings(AssemblySettings())
}


object AssemblySettings{
  val includeJars = List("scala-library")
  def include(name: String) = includeJars.exists(name.startsWith(_))
  def apply()= Seq(
    assemblyJarName in assembly := "interview-decompressor.jar",
    assemblyExcludedJars in assembly := {
      val cp = (fullClasspath in assembly).value
      cp filterNot {a => include(a.data.getName)}
    },
    mainClass in assembly := Some("uk.gov.hmrc.offpayroll.util.InterviewDecompressor")
  )
}
