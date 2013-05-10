import sbt._
import Keys._

object Build extends Build {
  import Resolvers._
  import BuildSettings._
  import Tasks._
  import Dependencies._
  import Versions._
  import com.github.siasia._
  
  lazy val yy = Project(
    id = "yy",
    base = file("."),
    settings = buildSettings ++
      seq(WebPlugin.webSettings: _*) ++
      seq(PluginKeys.port in WebPlugin.container.Configuration := 9001) ++
      seq(PluginKeys.scanDirectories in Compile := Nil) ++
      Seq(libraryDependencies := commonsDependencies ++ liftwebDependencies))

  object BuildSettings {
    import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys
    import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseCreateSrc

    val buildSettings = Defaults.defaultSettings ++ Seq(
      resolvers ++= Seq(aliyunNexus),
      scalaVersion := scalaBuildVersion,
      scalacOptions ++= Seq(
        "-encoding", "UTF-8",
        "-deprecation",
        "-unchecked"),
      EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource,
      javaOptions += "-Xmx912m",
      javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.6", "-target", "1.6"))
  }

  object Dependencies {
    val commonsDependencies = Seq(
      "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "container",
      "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "provided->default",
      "ch.qos.logback" % "logback-classic" % "1.0.10" % "compile->default",
      "net.liftmodules" %% "fobo_2.5" % "0.9.6-SNAPSHOT",
      "com.tencent" % "open-sdk" % "3.0.5" % "compile->default",
      "org.apache.httpcomponents" % "httpclient" % "4.2.2",
      "mysql" % "mysql-connector-java" % "5.1.21" % "runtime->default",
      "org.specs2" % "specs2_2.10" % "1.14" % "test->default",
      "junit" % "junit" % "4.10" % "test->default")

    val liftwebDependencies = Seq(
      "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default" withSources (),
      "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default" withSources (),
      "net.liftweb" %% "lift-wizard" % liftVersion % "compile->default" withSources (),
      "net.liftmodules" %% "lift-jquery-module" % (liftVersion + "-2.3"))
  }

  object Resolvers {
    val typeSafeRepo = "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
    val typeSafeSnapsRepo = "Typesafe Snaps Repo" at "http://repo.typesafe.com/typesafe/snapshots/"
    val oss = "OSS Sonatype" at "http://oss.sonatype.org/content/repositories/releases/"
    val ossSnaps = "OSS Sonatype Snaps" at "http://oss.sonatype.org/content/repositories/snapshots/"
    val twttr = "Maven twttr" at "http://maven.twttr.com/"
    val spray = "spray repo" at "http://repo.spray.io"
    val aliyunNexus = "Aliyun nexus" at "http://42.120.5.18:9081/nexus/content/groups/public/"
  }

  object Tasks {
    //empty now
  }

  object Versions {
    val scalaBuildVersion = "2.10.0"
    val liftVersion = "2.5-RC4"
    val jettyVersion = "8.0.4.v20111024"
  }
}