

name := "canvas-grade-data-uploader"

version := "0.1.0"

scalaVersion := "2.12.6"

enablePlugins(
  JavaAppPackaging,
  BuildInfoPlugin
)

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "info.picocli" % "picocli" % "3.6.0",
  "org.apache.pdfbox" % "pdfbox" % "2.0.11",
  "org.apache.commons" % "commons-text" % "1.4",
  "com.google.oauth-client" % "google-oauth-client" % "1.25.0",
  "com.google.oauth-client" % "google-oauth-client-java6" % "1.25.0",
  "com.google.http-client" % "google-http-client-jackson2" % "1.25.0",
  "com.google.oauth-client" % "google-oauth-client-jetty" % "1.25.0",
  "com.squareup.retrofit2" % "retrofit" % "2.4.0",
  "com.squareup.retrofit2" % "adapter-scala" % "2.4.0",
  "com.squareup.retrofit2" % "converter-gson" % "2.4.0",
  "com.squareup.okhttp3" % "okhttp" % "3.11.0",
  "com.squareup.okio" % "okio" % "2.0.0",
  "com.google.code.gson" % "gson" % "2.8.5",
  "com.github.sergeygrigorev" %% "gson-object-scala-syntax" % "0.4.0",
  "com.google.inject" % "guice" % "4.2.1",
  "net.codingwell" %% "scala-guice" % "4.2.1",
)

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
buildInfoPackage := "build"