import sbt._
import sbt.Keys._

val logbackVersion = "1.1.7"
val scalaLoggingVersion = "3.5.0"
val scalaAsyncVersion = "0.9.6"
val macWireVersion = "2.3.0"
val akkaVersion = "2.4.17"
val akkaHttpVersion = "10.0.6"
val prometheusVersion = "0.0.26"
val swaggerJaxrsVersion = "1.5.13"
val akkaHttpSwaggerVersion = "0.9.1"
val doobieVersion = "0.4.1"
val circeVersion = "0.8.0"
val scalaTestVersion = "3.0.1"
val scalaMockVersion = "3.6.0"
val sparkVersion = "2.3.0"


val exclusionsForXmlAndSparkConflicts = Seq(
  /*ExclusionRule(organization = "org.scalamock"),*/
  ExclusionRule(organization = "com.fasterxml.jackson.module"),
  ExclusionRule(organization = "org.scala-lang.modules")
)

// common settings
lazy val commonSettings = Seq(
  organization := "com.bitool.analytics",
  scalaVersion := "2.12.6",


  libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
    "org.scala-lang.modules" %% "scala-async" % scalaAsyncVersion,

    "com.softwaremill.macwire" %% "macros" % macWireVersion,
    "com.softwaremill.macwire" %% "macrosakka" % macWireVersion,

    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "io.swagger" % "swagger-jaxrs" % swaggerJaxrsVersion,
    "com.github.swagger-akka-http" %% "swagger-akka-http" % akkaHttpSwaggerVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "io.verizon.delorean" %% "core" % "1.2.40-scalaz-7.2", // used for converting Tasks -> Futures

    "org.tpolecat" %% "doobie-core" % doobieVersion,

    "io.circe" %% "circe-generic" % circeVersion, // for auto-derivation of JSON codecs
    "io.circe" %% "circe-literal" % circeVersion, // for string interpolation to JSON model
    "io.circe" %% "circe-parser" % circeVersion, // for parsing VCAP_SERVICES
    "io.circe" %% "circe-optics" % circeVersion, // JSONPath
    "org.scalafx" %% "scalafx" % "8.0.144-R12",
    "org.scalafx" %% "scalafxml-core-sfx8" % "0.4",
    ("org.apache.spark" % "spark-core_2.11" % sparkVersion).excludeAll(exclusionsForXmlAndSparkConflicts:_*),
    ("org.apache.spark" % "spark-sql_2.11" % sparkVersion).excludeAll(exclusionsForXmlAndSparkConflicts:_*),
    ("org.apache.spark" % "spark-hive_2.11" % sparkVersion).excludeAll(exclusionsForXmlAndSparkConflicts:_*)


   /* "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockVersion % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test"*/
  )

)

// root project
lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "BITOOL-1.0"
  )
