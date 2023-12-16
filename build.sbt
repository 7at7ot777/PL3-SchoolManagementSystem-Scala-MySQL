ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "SQL Project"
  )
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.4.1"

libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.33"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.40.1.0"
libraryDependencies += "com.typesafe.akka" %% "akka-persistence-typed" % "2.8.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.7"

