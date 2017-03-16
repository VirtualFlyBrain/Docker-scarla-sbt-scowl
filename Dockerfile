FROM hseeberger/scala-sbt

RUN mkdir -p ~/.sbt/0.13/plugins

COPY plugins.sbt ~/.sbt/0.13/plugins/plugins.sbt
