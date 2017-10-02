name := "kong4s"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "org.typelevel" % "cats-core_2.11" % "1.0.0-MF",
  "org.typelevel" % "cats-free_2.11" % "1.0.0-MF",
  "io.circe" % "circe-core_2.11" % "0.8.0",
  "io.circe" % "circe-generic_2.11" % "0.8.0",
  "io.circe" % "circe-parser_2.11" % "0.8.0",
  "org.typelevel" % "cats-effect_2.11" % "0.4"
)
