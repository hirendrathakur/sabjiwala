name := "sabjiwala-backend"
organization := "com.flipkart"
version := "0.1"
scalaVersion := "2.11.7"

/** all akka only **/
val akkaVersion = "2.4.17"
val akkaHttpVersion = "10.0.4"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-stream" % akkaVersion withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion withSources(),
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test withSources() withJavadoc()
)

libraryDependencies ++= Seq(
"com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.7",
  "commons-lang" % "commons-lang" % "2.6"


)

mainClass in (Compile, run) := Some("com.flipkart.sabjiwala.SabjiWala")
