enablePlugins(plugin.JavaFXMobilePlugin)

scalaVersion := "2.11.6"
organization := "SANDEC"

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
mainClass := Some("samples.Untangle")

appName := "Untangle"

resolvers += Resolver.url("SANDEC", url("http://dl.bintray.com/sandec/repo"))(Resolver.ivyStylePatterns)
libraryDependencies += "SANDEC" %% "simplefx" % "2.1.1"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:_")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)

fork := true
