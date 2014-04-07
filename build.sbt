// Notes on syntax
//  - settings are initialized with :=
//  - dependency paths given by %
//  - dependency paths against specific scala versions are given by %%

name := "RSA"

//organization := "org.dyndns.phpusr"

//version := "1.0"

scalaVersion := "2.10.3"

javacOptions ++= Seq("-encoding", "UTF-8")
 
//libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test" TODO тесты
libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10.3"

exportJars := true

//packageOptions in (Compile, packageBin) += Package.ManifestAttributes( java.util.jar.Attributes.Name.MAIN_CLASS -> "experiment.form.MainForm" )

packageOptions in (Compile, packageBin) +=  {
  import java.util.jar.{Attributes, Manifest}
  val manifest = new Manifest
  val mainAttributes = manifest.getMainAttributes
  mainAttributes.put(Attributes.Name.MAIN_CLASS, "experiment.form.MainForm")
  mainAttributes.put("Author", "phpusr") // TODO выяснить почему нельзя поставить
  Package.JarManifest( manifest )
}