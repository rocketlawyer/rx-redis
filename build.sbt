name := """rx-redis-parent"""

description in ThisBuild := """Reactive Extensions for Redis"""

organization in ThisBuild := """net.crispywalrus.rx-redis"""

scalaVersion in ThisBuild := Version.scala

scalacOptions in ThisBuild ++= List(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:postfixOps",
  "-target:jvm-1.7",
  "-unchecked",
  "-Xlint",
  "-Ywarn-dead-code"
)

shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

initialCommands := """|import rx.redis.api.RxRedis
                      |val client = RxRedis("localhost", 6379)""".stripMargin
