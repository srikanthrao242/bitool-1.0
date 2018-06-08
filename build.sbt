import sbt._
import sbt.Keys._

val logbackVersion = "1.1.7"
val scalaLoggingVersion = "3.9.0"
val scalaAsyncVersion = "0.9.6"
val macWireVersion = "2.3.0"
val akkaVersion = "2.4.17"
val akkaHttpVersion = "10.0.6"
val prometheusVersion = "0.0.26"
val swaggerJaxrsVersion = "1.5.19"
val akkaHttpSwaggerVersion = "0.9.1"
val doobieVersion = "0.4.1"
val circeVersion = "0.8.0"
val scalaTestVersion = "3.0.1"
val scalaMockVersion = "3.6.0"
val sparkVersion = "2.3.0"


val exclusionsForXmlAndSparkConflicts = Seq(
  /*ExclusionRule(organization = "org.scalamock"),*/
  //ExclusionRule(organization = "com.fasterxml.jackson.module"),
  //ExclusionRule(organization = "org.scala-lang.modules"),
  //ExclusionRule("org.scala-lang.modules", "scala-parser-combinators")
)


dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.7"



/*excludeDependencies ++= Seq(
  // commons-logging is replaced by jcl-over-slf4j
  ExclusionRule("org.scala-lang.modules", "scala-parser-combinators")
)*/

// common settings
lazy val commonSettings = Seq(
  organization := "com.bitool.analytics",
  scalaVersion := "2.11.12",


  libraryDependencies ++= Seq(
    /*"ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,*/
    "org.scala-lang.modules" %% "scala-async" % scalaAsyncVersion,

    "com.softwaremill.macwire" %% "macros" % macWireVersion,
    "com.softwaremill.macwire" %% "macrosakka" % macWireVersion,

    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "io.swagger" % "swagger-jaxrs" % swaggerJaxrsVersion,
    "com.github.swagger-akka-http" %% "swagger-akka-http" % akkaHttpSwaggerVersion,
    /*"com.typesafe.akka" %% "akka-slf4j" % akkaVersion,*/

    "io.circe" %% "circe-generic" % circeVersion, // for auto-derivation of JSON codecs
    "io.circe" %% "circe-literal" % circeVersion, // for string interpolation to JSON model
    "io.circe" %% "circe-parser" % circeVersion, // for parsing VCAP_SERVICES
    "io.circe" %% "circe-optics" % circeVersion, // JSONPath
    "org.scalafx" %% "scalafx" % "8.0.144-R12",
    "org.scalafx" %% "scalafxml-core-sfx8" % "0.4",
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion,
    "org.apache.spark" %% "spark-hive" % sparkVersion,
    "org.scala-lang" % "scala-xml" % "2.11.0-M4",
    "mysql" % "mysql-connector-java" % "6.0.5"

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
