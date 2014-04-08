// Notes on syntax
//  - settings are initialized with :=
//  - dependency paths given by %
//  - dependency paths against specific scala versions are given by %%

name := "RSA"

//organization := "org.dyndns.phpusr"

//version := "1.0"

val runClass = "experiment.form.MainForm"

mainClass := Some(runClass)

scalaVersion := "2.10.3"

javacOptions ++= Seq("-encoding", "UTF-8")

//libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test" TODO тесты
libraryDependencies += "org.scala-lang" % "scala-swing" % scalaVersion.value

// Run task
lazy val runapp = taskKey[Unit]("A custom run task.")

fullRunTask(runapp, Test, runClass)