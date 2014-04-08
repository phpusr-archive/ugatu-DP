import scala.Some

name := "RSA"

val runClass = "dataprotection.lab.three.run.Main"

scalaVersion := "2.10.3"

//libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test" TODO тесты
libraryDependencies += "org.scala-lang" % "scala-swing" % scalaVersion.value

javacOptions ++= Seq("-encoding", "UTF-8")

// sbt-onejar (https://github.com/sbt/sbt-onejar)
com.github.retronym.SbtOneJar.oneJarSettings

mainClass in Compile := Some(runClass)

// Run task (run-app, runApp)
lazy val runApp = TaskKey[Unit]("run-app", "A custom run task.")

fullRunTask(runApp, Test, runClass)
