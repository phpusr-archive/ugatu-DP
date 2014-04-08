import sbt.Package._
import scala.Some

// Notes on syntax
//  - settings are initialized with :=
//  - dependency paths given by %
//  - dependency paths against specific scala versions are given by %%

name := "RSA"

//organization := "org.dyndns.phpusr"

//version := "1.0"

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

// Additional manifest attributes
packageOptions := Seq(ManifestAttributes(
  ("Main-Class", "Googoosha1"),
  ("Author", "phpusr")
))

// Set Main-Class in jar
packageOptions in (Compile, packageBin) +=  {
  import java.util.jar.{Attributes, Manifest}
  val manifest = new Manifest
  manifest.getMainAttributes.put(Attributes.Name.MAIN_CLASS, "Googoosha2")
  Package.JarManifest(manifest)
}