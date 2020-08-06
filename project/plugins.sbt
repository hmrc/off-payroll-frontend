resolvers += Resolver.bintrayIvyRepo("hmrc", "sbt-plugin-releases")
resolvers += Resolver.bintrayRepo("hmrc", "releases")

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "2.9.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-git-versioning" % "2.1.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-distributables" % "2.0.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-artifactory" % "1.3.0")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.5")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")

addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.13")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.4")
