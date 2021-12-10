lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    name := """SOEN6441-ReditLytics-GoalDiggers""",
    version := "1.0",
    scalaVersion := "2.13.6",
    // https://github.com/sbt/junit-interface
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v"),
    libraryDependencies ++= Seq(
      guice,
      ws,
      "org.webjars" %% "webjars-play" % "2.8.0",
      "org.webjars" % "bootstrap" % "2.3.2",
      "org.webjars" % "flot" % "0.8.3",

      // Testing libraries for dealing with CompletionStage...
      "org.assertj" % "assertj-core" % "3.14.0" % Test,
      "org.awaitility" % "awaitility" % "4.0.1" % Test,
      "org.json" % "json" % "20210307",
      "edu.stanford.nlp" % "stanford-corenlp" % "4.3.1",
      "edu.stanford.nlp" % "stanford-corenlp" % "4.3.1" classifier "models",
      "com.google.code.gson" % "gson" % "2.8.9",
      "org.mockito" % "mockito-core" % "4.0.0",
      "com.typesafe.akka" %% "akka-actor" % "2.6.10",
      "com.typesafe.akka" %% "akka-testkit" % "2.6.14"
    ),
    LessKeys.compress := true,
    javacOptions ++= Seq(
      "-Xlint:unchecked",
      "-Xlint:deprecation",
      "-Werror"
    )
  )
