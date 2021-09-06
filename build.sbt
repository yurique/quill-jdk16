scalaVersion := "2.13.6"
scalacOptions ++= Seq(
  "-language:higherKinds",
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked"
)
libraryDependencies ++= Seq(
  "io.getquill" %% "quill-sql" % "3.8.0"
)
