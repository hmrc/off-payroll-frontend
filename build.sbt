import play.sbt.routes.RoutesKeys
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, _}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

val appName: String = "cest-frontend"

lazy val appDependencies : Seq[ModuleID] = AppDependencies()
lazy val plugins : Seq[Plugins] = Seq(play.sbt.PlayScala)
lazy val playSettings : Seq[Setting[_]] = Seq.empty
val silencerVersion = "1.7.1"

lazy val scoverageSettings = {
    import scoverage.ScoverageKeys
    Seq(
        // Semicolon-separated list of regexs matching classes to exclude
        ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;.*filters.*;.*handlers.*;.*components.*;.*repositories.*;" +
          ".*BuildInfo.*;.*javascript.*;.*FrontendAuditConnector.*;.*Routes.*;.*GuiceInjector;" +
          ".*ControllerConfiguration;.*LanguageSwitchController;.*testonly.*;.*views.*;",
        ScoverageKeys.coverageMinimumStmtTotal := 80,
        ScoverageKeys.coverageFailOnMinimum := true,
        ScoverageKeys.coverageHighlighting := true,
        parallelExecution in Test := false
    )
}

lazy val microservice = Project(appName, file("."))
  .enablePlugins(Seq(play.sbt.PlayScala, SbtDistributablesPlugin) ++ plugins: _*)
  .settings(majorVersion := 1)
  .configs(IntegrationTest)
  .settings(scalaSettings: _*)
  .settings(publishingSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(scalaVersion := "2.12.12")
  .settings(scalacOptions += "-Ywarn-unused:-explicits,-implicits")
  .settings(playSettings ++ scoverageSettings: _*)
  .settings(
    RoutesKeys.routesImport += "models._",
    addTestReportOption(IntegrationTest, "int-test-reports"),
    inConfig(IntegrationTest)(Defaults.itSettings),
    libraryDependencies ++= appDependencies,
    retrieveManaged := true,
    parallelExecution          in Test := true,
    fork                       in Test := true,
    Keys.fork                  in IntegrationTest :=  false,
    unmanagedSourceDirectories in IntegrationTest := (baseDirectory in IntegrationTest)(base => Seq(base / "it")).value,
    parallelExecution in IntegrationTest := false,
    // ***************
    // Use the silencer plugin to suppress warnings from unused imports in compiled twirl templates
    scalacOptions += "-P:silencer:pathFilters=views;routes",
    libraryDependencies ++= Seq(
        compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
        "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full
    )
    // ***************
  )
  .settings(
    resolvers += Resolver.jcenterRepo
  )
  .disablePlugins(JUnitXmlReportPlugin)
