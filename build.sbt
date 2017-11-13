name := "airport"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= List(
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "org.postgresql" % "postgresql" % "42.1.4",
  "org.slf4j" % "slf4j-nop" % "1.7.10"
)
        