import scala.Some

name := "RSA"

val runClass = "dataprotection.lab.three.run.Main"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-swing" % scalaVersion.value,
  "org.scalatest" % "scalatest_2.10" % "2.1.3" % "test",
  "org.pegdown" % "pegdown" % "1.4.2" % "test"
)

// Show durations, short stack traces and generate html report
testOptions in Test += Tests.Argument("-oDS", "-h", "target/report")

javacOptions ++= Seq("-encoding", "UTF-8")

// sbt-onejar (https://github.com/sbt/sbt-onejar)
com.github.retronym.SbtOneJar.oneJarSettings

mainClass in Compile := Some(runClass)

// Run task (run-app, runApp)
lazy val runApp = TaskKey[Unit]("run-app", "A custom run task.")

fullRunTask(runApp, Test, runClass)
