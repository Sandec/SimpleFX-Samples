resolvers += Resolver.url("SANDEC", url("http://dl.bintray.com/sandec/repo"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.2")
addSbtPlugin("SANDEC" % "javafxmobile-sbt" % "0.1.3")
addSbtPlugin("SANDEC" % "simplefx-plugin" % "2.2.0-SNAPSHOT")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "3.0.0")
