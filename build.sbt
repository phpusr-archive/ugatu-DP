import scala.Some

name := "RSA"

val runClass = "dataprotection.lab.three.run.Main"

scalaVersion := "2.10.3"

// Encryption library (http://www.bouncycastle.org/latest_releases.html)
unmanagedJars in Compile += file("lib/bcprov-jdk15on-150.jar")

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

// Main-Class for jar
mainClass in Compile := Some(runClass)

// Package depends on tests
packageBin in Compile <<= (packageBin in Compile) dependsOn (test in Test)



// Run task (run-app, runApp)
lazy val runApp = TaskKey[Unit]("run-app", "A custom run task.")

fullRunTask(runApp, Test, runClass)

// Run app depends on tests
runApp <<= runApp dependsOn (test in Test)
