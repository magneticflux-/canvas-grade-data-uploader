addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.9")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

// For JDeb Debian packaging
libraryDependencies += "org.vafer" % "jdeb" % "1.6" artifacts Artifact("jdeb", "jar", "jar")
