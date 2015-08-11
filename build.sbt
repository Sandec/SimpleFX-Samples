enablePlugins(JavaFXMobilePlugin)
enablePlugins(SimpleFXPlugin)

scalaVersion := "2.11.7"
organization := "SANDEC"

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
mainClass := Some("samples.Untangle")

appName := "Untangle"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:_")
fork := true
