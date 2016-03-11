resolvers += Resolver.url("SANDEC", url("http://dl.bintray.com/sandec/repo"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.12.0")
addSbtPlugin("SANDEC" % "javafxmobile-sbt" % "0.1.3")
addSbtPlugin("SANDEC" % "simplefx-plugin" % "2.1.2")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "3.0.0")