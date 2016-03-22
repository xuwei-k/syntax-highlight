name := "syntax-highlight"

fullResolvers ~= {_.filterNot(_.name == "jcenter")}

sbtappengine.Plugin.appengineSettings

scalaVersion := "2.10.6"

scalacOptions ++= (
  "-language:postfixOps" ::
  "-language:implicitConversions" ::
  "-language:higherKinds" ::
  "-language:existentials" ::
  "-deprecation" ::
  "-unchecked" ::
  "-Xlint" ::
  Nil
)

libraryDependencies ++= (
  ("org.eclipse.jetty" % "jetty-webapp" % "7.6.8.v20121106" % "container") ::
  ("javax.servlet" % "servlet-api" % "2.5" % "provided") ::
  ("commons-fileupload" % "commons-fileupload" % "1.2.2") ::
  ("org.scalatra" %% "scalatra" % "2.0.5") :: // https://code.google.com/p/googleappengine/issues/detail?id=3091
  ("org.pegdown" % "pegdown" % "1.4.1") ::
  ("org.scalaz" %% "scalaz-core" % "7.2.1") ::
  ("com.jsuereth" %% "scala-arm" % "1.4") ::
  Nil
)
