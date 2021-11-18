import sbt._

object AppDependencies {
  import play.core.PlayVersion._

  val wireMockVersion = "2.31.0"
  val wireMockStandAloneVersion = "2.27.2"

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc"       %% "simple-reactivemongo"             % "8.0.0-play-28",
    "uk.gov.hmrc"       %% "logback-json-logger"              % "5.1.0",
    "uk.gov.hmrc"       %% "govuk-template"                   % "5.72.0-play-28",
    "uk.gov.hmrc"       %% "play-ui"                          % "9.7.0-play-28",
    "uk.gov.hmrc"       %% "http-caching-client"              % "9.5.0-play-28",
    "uk.gov.hmrc"       %% "play-conditional-form-mapping"    % "1.10.0-play-28",
    "uk.gov.hmrc"       %% "bootstrap-frontend-play-28"       % "5.16.0",
    "uk.gov.hmrc"       %% "play-allowlist-filter"            % "1.0.0-play-28",
    "com.typesafe.play" %% "play-json-joda"                   % "2.9.2",
    "uk.gov.hmrc"       %% "digital-engagement-platform-chat" % "0.22.0-play-28"
  )

  val test = Seq(
    "uk.gov.hmrc"                  %% "service-integration-test"    % "1.2.0-play-28",
    "uk.gov.hmrc"                  %% "reactivemongo-test"          % "5.0.0-play-28",
    "org.scalatest"                %% "scalatest"                   % "3.3.0-SNAP3",
    "org.scalatestplus.play"       %% "scalatestplus-play"          % "5.1.0",
    "org.mockito"                  %  "mockito-core"                % "4.0.0",
    "org.scalatestplus"            %% "scalatestplus-mockito"       % "1.0.0-M2",
    "org.scalatestplus"            %% "scalacheck-1-15"             % "3.2.10.0",
    "org.pegdown"                  %  "pegdown"                     % "1.6.0",
    "org.jsoup"                    %  "jsoup"                       % "1.14.3",
    "com.typesafe.play"            %% "play-test"                   % current,
    "org.scalacheck"               %% "scalacheck"                  % "1.15.4",
    "org.scalamock"                %% "scalamock-scalatest-support" % "3.6.0",
    "uk.gov.hmrc"                  %% "bootstrap-test-play-28"      % "5.16.0",
    "com.github.tomakehurst"       %  "wiremock-jre8"               % wireMockVersion,
    "com.github.tomakehurst"       %  "wiremock-standalone"         % wireMockStandAloneVersion,
    "com.fasterxml.jackson.module" %% "jackson-module-scala"        % "2.13.0"
  ).map(_ % "test, it")
  
  def apply() = compile ++ test
}
