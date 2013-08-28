import sbt._
import Keys._

object BuildGAE extends Build{

  val gaeSDK = "1.8.3"

  lazy val root = Project("syntax-highlight", file("."),
    settings = {
      Defaults.defaultSettings ++
      sbtappengine.Plugin.appengineSettings ++
      Seq(
        scalaVersion := "2.9.3",
        libraryDependencies ++= Seq(
           "org.eclipse.jetty" % "jetty-webapp" % "7.6.8.v20121106" % "container"
          ,"javax.servlet" % "servlet-api" % "2.5" % "provided"
          ,"commons-fileupload" % "commons-fileupload" % "1.2.2"
          ,"com.google.appengine" % "appengine-java-sdk" % gaeSDK
          ,"com.google.appengine" % "appengine-api-1.0-sdk" % gaeSDK
//          ,"net.kindleit" % "gae-runtime" % gaeSDK
          ,"org.scalatra" % "scalatra_2.9.1" % "2.0.4"
          ,"net.databinder" % "pamflet-knockoff_2.9.1" % "0.4.1"
          ,"org.scalaz" %% "scalaz-core" % "7.1.0-M2"
          ,"com.jsuereth" % "scala-arm_2.9.2" % "1.3"
        )
      )
    }
  )

}
