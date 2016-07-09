lazy val root = (project in file(".")).
  settings(
    name := "spot",
    organization := "com.github.mariusfeteanu",
    version := "1.0.0",
    scalaVersion := "2.11.8"
  )

libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.5"
libraryDependencies += "com.typesafe.slick" % "slick_2.11" % "3.1.1"
libraryDependencies += "com.typesafe" % "config" % "1.3.0"
libraryDependencies += "com.typesafe.slick" %% "slick-hikaricp" % "3.1.1"
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.21"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.8.11.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4.8"

mainClass in Compile  := Some("com.spotai.main.RunActor")

scalacOptions ++= Seq("-feature")
scalacOptions ++= Seq("-deprecation")
scalacOptions ++= Seq("-unchecked")
scalacOptions ++= Seq("-Xlint")
scalacOptions ++= Seq("-Xfatal-warnings")

// META-INF discarding
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
   {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
   }
}

publishMavenStyle := true

// Publish to maven central
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

// Don't publish tests
publishArtifact in Test := false

// remove the repositories for optional dependencies
pomIncludeRepository := { _ => false }

// Add license and contact information
pomExtra := (
  <url>https://github.com/mariusfeteanu/spot</url>
  <licenses>
    <license>
      <name>GPLv3</name>
      <url>https://www.gnu.org/licenses/gpl-3.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:mariusfeteanu/spot.git</url>
    <connection>scm:git:git@github.com:mariusfeteanu/spot.git</connection>
  </scm>
  <developers>
    <developer>
      <id>mariusfeteanu</id>
      <name>Marius Feteanu</name>
      <url>https://github.com/mariusfeteanu</url>
    </developer>
  </developers>)
