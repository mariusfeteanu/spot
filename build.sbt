lazy val root = (project in file(".")).
  settings(
    name := "spot",
    version := "1.0",
    scalaVersion := "2.11.7"
  )

libraryDependencies += "org.scala-lang" % "scala-xml" % "2.11.0-M4"

mainClass in Compile := Some("Run")

scalacOptions ++= Seq("-feature")

// META-INF discarding
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
   {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
   }
}
