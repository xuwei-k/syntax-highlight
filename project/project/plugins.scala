import sbt._

object PluginDef extends Build {
  lazy val root = Project("plugins", file(".")) dependsOn(
    uri("git://github.com/sbt/sbt-appengine#0.4.1")
  )
}

