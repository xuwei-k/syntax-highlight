import sbt._
import Keys._

object BuildGAE extends Build{

  val gaeSDK = "1.9.2"

  lazy val root = Project("syntax-highlight", file("."),
    settings = {
      Defaults.defaultSettings ++
      sbtappengine.Plugin.appengineSettings ++
      Seq(
        scalaVersion := "2.10.4",
        scalacOptions ++= Seq("-deprecation", "-language:_"),
        libraryDependencies ++= Seq(
           "org.eclipse.jetty" % "jetty-webapp" % "7.6.8.v20121106" % "container"
          ,"javax.servlet" % "servlet-api" % "2.5" % "provided"
          ,"commons-fileupload" % "commons-fileupload" % "1.2.2"
          ,"com.google.appengine" % "appengine-java-sdk" % gaeSDK
          ,"com.google.appengine" % "appengine-api-1.0-sdk" % gaeSDK
          ,"org.scalatra" %% "scalatra" % "2.0.5"
          ,"org.pegdown" % "pegdown" % "1.4.1"
          ,"org.scalaz" %% "scalaz-core" % "7.1.0-M6"
          ,"com.jsuereth" %% "scala-arm" % "1.3"
        )
      )
    }
  )

}
