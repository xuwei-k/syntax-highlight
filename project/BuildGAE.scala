import sbt._
import Keys._

object BuildGAE extends Build{

  val gaeSDK = "1.5.2"

  val sourceCount    = TaskKey[Unit]("source-count")
  val createSxrSlide = TaskKey[Unit]("create-sxr-slide")

  lazy val root = Project("syntax-highlight", file("."),
    settings = {
      Defaults.defaultSettings ++ 
      sbtappengine.AppenginePlugin.webSettings ++ 
      Seq(
        scalaVersion := "2.9.1", 
        libraryDependencies ++= Seq(
           "javax.servlet" % "servlet-api" % "2.5"
          ,"commons-fileupload" % "commons-fileupload" % "1.2.2"
          ,"com.google.appengine" % "appengine-java-sdk" % gaeSDK 
          ,"com.google.appengine" % "appengine-api-1.0-sdk" % gaeSDK 
          ,"net.kindleit" % "gae-runtime" % gaeSDK 
          ,"org.scalatra" %% "scalatra" % "2.0.0"
        )
        ,resolvers ++= Seq(
            "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
           ,"xuwei-k repo" at "http://xuwei-k.github.com/mvn"
         )
        ,addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")
        ,sourceCount <<= ( sources in Compile , sources in Test ) map{ (main,test) =>
           println{
             "\nmain " + main.map{f => IO.readLines(f).size}.sum +
             "\ntest " + test.map{f => IO.readLines(f).size}.sum
           }
        }
        ,createSxrSlide <<= ( sources in Compile , sources in Test ) map{ (main,test) =>
          Seq(main -> "main" ,test -> "test" ).foreach{ case (files,n) =>
            IO.write( file(n) , create(SxrBaseURL + n,files.map{_.toString}) )
          }
        }
      )
    }
  )

  val SxrBaseURL = "http://xuwei-k.github.com/syntax-highlight/"

  def dom(src:String,w:Int,h:Int):xml.Elem = {
    <iframe src={src} width={w.toString} height={h.toString} />
  }

  def create(baseURL:String,files:Seq[String],w:Int = 1200,h:Int = 600):String = {
    files.map{ f =>
      "\n\n!SLIDE\n\n" + dom( baseURL + f , w , h )
    }.mkString
  }
}
