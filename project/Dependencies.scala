import sbt._

object Version {
  val scala = "2.11.4"
  val netty = "4.0.24.Final"
  val rxScala = "0.22.0"
  val rxJava = "1.0.0-rc.8"
  val scalaTest = "2.2.2"
  val scalaCheck = "1.11.6"
}

object Library {
  val scalaReflect   = "org.scala-lang"      % "scala-reflect"    % Version.scala
  val nettyCommon    = "io.netty"            % "netty-common"     % Version.netty withSources() withJavadoc()
  val nettyBuffer    = "io.netty"            % "netty-buffer"     % Version.netty withSources() withJavadoc()
  val nettyTransport = "io.netty"            % "netty-transport"  % Version.netty withSources() withJavadoc()
  val rxJava         = "io.reactivex"        % "rxjava"           % Version.rxJava withSources() withJavadoc()
  val rxScala        = "io.reactivex"       %% "rxscala"          % Version.rxScala withSources() withJavadoc()
  val scalaTest      = "org.scalatest"      %% "scalatest"        % Version.scalaTest
  val scalaCheck     = "org.scalacheck"     %% "scalacheck"       % Version.scalaCheck
}

object Dependencies {

  import Library._

  val netty = List(nettyTransport, nettyBuffer, nettyCommon)

  val coreDeps = scalaReflect % "compileonly" :: List(nettyBuffer, scalaTest, scalaCheck).map(_  % "test")
  val commandsDeps = List(scalaTest % "test")
  val clientDeps = netty ::: List(rxJava, scalaTest % "it,test")
  val scalaApiDeps = List(rxScala)
}
