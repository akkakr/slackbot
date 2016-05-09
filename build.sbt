name := "slackbot"

version := "1.0"

scalaVersion := "2.11.8"

enablePlugins(JavaAppPackaging)

libraryDependencies ++= {
  val akkaVersion = "2.4.3"

  Seq(
    //"com.typesafe.akka" %% "akka-agent" % akkaVersion,
    //"com.typesafe.akka" %% "akka-camel" % akkaVersion,
    //"com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    //"com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
    //"com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
    //"com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    //"com.typesafe.akka" %% "akka-contrib" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion,
    //"com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
    //"com.typesafe.akka" %% "akka-osgi" % akkaVersion,
    //"com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    //"com.typesafe.akka" %% "akka-persistence-tck" % akkaVersion,
    //"com.typesafe.akka" %% "akka-remote" % akkaVersion,
    //"com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    //"com.typesafe.akka" %% "akka-stream" % akkaVersion,
    //"com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
    //"com.typesafe.akka" %% "akka-testkit" % akkaVersion,
    //"com.typesafe.akka" %% "akka-distributed-data-experimental" % akkaVersion,
    //"com.typesafe.akka" %% "akka-typed-experimental" % akkaVersion,
    //"com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
    //"com.typesafe.akka" %% "akka-http-jackson-experimental" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
    //"com.typesafe.akka" %% "akka-http-xml-experimental" % akkaVersion,
    //"com.typesafe.akka" %% "akka-persistence-query-experimental" % akkaVersion,
    //"com.typesafe.akka" %% "akka-typed-experimental" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion
  )
}
