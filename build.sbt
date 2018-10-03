

name := "canvas-grade-data-uploader"

version := "0.2.1"

maintainer := "Mitchell Skaggs <skaggsm333@gmail.com>"
packageSummary := "Canvas Grade Data Uploader"
packageDescription :=
  """A CLI to upload grades to Canvas. This program reads in scores from PDFs
    | and then uploads the grade and the PDF to the student's submission."""
    .stripMargin

scalaVersion := "2.12.7"

enablePlugins(
  JavaAppPackaging,
  UniversalPlugin,
  WindowsPlugin,
  DebianPlugin,
  JDebPackaging,
  BuildInfoPlugin,
)

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "info.picocli" % "picocli" % "3.6.1",
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
  "com.squareup.okhttp3" % "logging-interceptor" % "3.11.0",
  "com.squareup.okio" % "okio" % "2.1.0",
  "com.google.code.gson" % "gson" % "2.8.5",
  "com.github.sergeygrigorev" %% "gson-object-scala-syntax" % "0.4.0",
  "com.google.inject" % "guice" % "4.2.1",
  "net.codingwell" %% "scala-guice" % "4.2.1",
  "org.typelevel" %% "squants" % "1.3.0",
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.apache.logging.log4j" % "log4j-api" % "2.11.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.11.1",
  "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0",
  "org.apache.logging.log4j" % "log4j-web" % "2.11.1",
  "org.fusesource.jansi" % "jansi" % "1.17.1",
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
)

// Info to be added to a generated class
buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
buildInfoPackage := "build"

// Wix build information
Windows / wixProductId := "8a873157-adde-44bc-888f-0c638e158b0c"
Windows / wixProductUpgradeId := "672ed6eb-0c69-42a2-9789-f78ccbe14e9c"

// Debian dependencies
Debian / debianPackageDependencies := Seq("java8-runtime-headless")

val packageWindows = taskKey[Unit]("Builds packages available to be build on Windows. (Windows, Debian, and Universal)")
packageWindows := {
  (Universal / packageBin).value
  (Windows / packageBin).value
  (Debian / packageBin).value
}
