lazy val root = (project in file(".")).
  settings(
    name := "spot",
    version := "1.0",
    scalaVersion := "2.11.7"
  )

libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.5"
libraryDependencies += "com.typesafe.slick" % "slick_2.11" % "3.1.1"
libraryDependencies += "com.typesafe" % "config" % "1.3.0"

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
