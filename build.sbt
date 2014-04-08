import scala.Some

name := "RSA"

val runClass = "experiment.form.MainForm"

// sbt-onejar (https://github.com/sbt/sbt-onejar)
com.github.retronym.SbtOneJar.oneJarSettings

mainClass in Compile := Some(runClass)

scalaVersion := "2.10.3"

javacOptions ++= Seq("-encoding", "UTF-8")

//libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test" TODO тесты
libraryDependencies += "org.scala-lang" % "scala-swing" % scalaVersion.value

// Run task (run-app, runApp)
lazy val runApp = TaskKey[Unit]("run-app", "A custom run task.")

fullRunTask(runApp, Test, runClass)
