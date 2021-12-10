addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.8")

addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.2")

resolvers += Resolver.sonatypeRepo("public") // (not entirely sure this line is necessary)

addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.4.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.9.2")