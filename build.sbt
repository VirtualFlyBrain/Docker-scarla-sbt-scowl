organization  := "org.virtualflybrain"

name          := "VFB_KB2OWL"

version       := "0.0.1"

scalaVersion  := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

javaOptions += "-Xmx8G"

libraryDependencies ++= {
    Seq(
      "org.neo4j.driver" % "neo4j-java-driver" % "1.0.4",
      "net.sourceforge.owlapi"     %  "owlapi-distribution" % "4.2.1",
      "org.semanticweb.elk"    %   "elk-owlapi"          % "0.4.1",
      "org.phenoscape"             %% "scowl"               % "1.1"
      )
}

