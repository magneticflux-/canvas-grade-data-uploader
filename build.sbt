

name := "canvas-grade-data-uploader"

version := "0.1.0"

scalaVersion := "2.12.6"

enablePlugins(
  JavaAppPackaging,
  BuildInfoPlugin
)

libraryDependencies += "info.picocli" % "picocli" % "3.6.0"

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
buildInfoPackage := "build"