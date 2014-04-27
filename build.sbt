import scala.Some

name := "DP"

val runClass = "dataprotection.lab.common.run.Main"

unmanagedJars in Compile += file(s"${Path.userHome}/.ivy2/cache/org.scala-lang/scala-swing/jars/scala-swing-2.10.3.jar")

libraryDependencies ++= Seq(
  //"org.scala-lang" % "scala-swing" % scalaVersion.value,
  "org.scalatest" % "scalatest_2.10" % "2.1.3" % "test",
  "org.pegdown" % "pegdown" % "1.4.2" % "test"
)

// Show durations, short stack traces and generate html report
testOptions in Test += Tests.Argument("-oDS", "-h", "target/report")

javacOptions ++= Seq("-encoding", "UTF-8")



// sbt-onejar (https://github.com/sbt/sbt-onejar)
com.github.retronym.SbtOneJar.oneJarSettings

// Main-Class for jar
mainClass in Compile := Some(runClass)

// Package depends on tests
packageBin in Compile <<= (packageBin in Compile) dependsOn (test in Test)



// Run task (run-app, runApp)
lazy val runApp = TaskKey[Unit]("run-app", "A custom run task.")

fullRunTask(runApp, Test, runClass)

// Run app depends on tests
runApp <<= runApp dependsOn (test in Test)
