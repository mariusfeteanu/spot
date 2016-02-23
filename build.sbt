lazy val root = (project in file(".")).
  settings(
    name := "spot",
    version := "1.0",
    scalaVersion := "2.11.7"
  )

libraryDependencies += "org.scala-lang" % "scala-xml" % "2.11.0-M4"

mainClass in Compile := Some("com.spotai.main.Run")

scalacOptions ++= Seq("-feature")
scalacOptions ++= Seq("-deprecation")

// META-INF discarding
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
   {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
   }
}
