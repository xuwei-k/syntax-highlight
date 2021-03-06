package com.github.xuwei_k.syntax_highlight

import scala.collection.mutable
import scalaz.Equal

/** fileの種類表す
 * @param name
 * @param extensions 拡張子の一覧
 */
sealed case class FileType private(name: String, extensions: String*){
  import FileType._

  instances += this //mapに保持

  private[this] lazy val jsFileName = "sh_%s.js" format name

  lazy val jsFile: ByteFile =
    ByteFile(jsFileName, Source2html.fileToByteArray(ResourceDir + jsFileName))
}

object FileType{
  val ResourceDir = "resource/"
  private val instances = new mutable.HashSet[FileType]()
  //todo 単にMap保持すれば、valで保持して、名前つける必要なくね？

  val bison = FileType("bison", "bison")
  val c = FileType("c", "c", "m")//objective c ほすぃ・・・
  val caml = FileType("caml", "caml", "ml", "fs", "fsi")
  val changelog = FileType("changelog")
  val cpp = FileType("cpp", "cpp", "hpp", "h", "mm")
  val csharp = FileType("csharp", "cs")
//  val css = FileType("css", "css")
  val desktop = FileType("desktop", "desktop")
  val diff = FileType("diff", "diff")
  val flex = FileType("flex", "flex")
  val glsl = FileType("glsl", "glsl")
  val haskell = FileType("haskell", "hs", "lhs", "hsc", "hs-boot", "lhs-boot", "purs", "e")
  val haxe = FileType("haxe", "haxe")
//  val html = FileType("html", "html", "xhtml") //todo encode
  val java = FileType("java", "java", "groovy", "gradle")
  val javascript = FileType("javascript", "json")
  val javascript_dom = FileType("javascript_dom")
  val latex = FileType("latex")
  val ldap = FileType("ldap")
  val log = FileType("log")
  val lsm = FileType("lsm")
  val m4 = FileType("m4")
  val makefile = FileType("makefile")
  val oracle = FileType("oracle")
  val pascal = FileType("pascal")
  val perl = FileType("perl", "pl")
  val php = FileType("php", "php")
  val prolog = FileType("prolog")
  val properties = FileType("properties")
  val python = FileType("python", "py")
  val ruby = FileType("ruby", "rb", "erb")
//  val scala = FileType("scala", "scala", "sbt")
  val scala = new FileType("scala", "scala", "sbt"){
    override lazy val jsFile = ByteFile(TEMPLATE_CSS, Source2html.fileToByteArray(ResourceDir+TEMPLATE_CSS))
  }

  val sh = FileType("sh", "sh")
  val slang = FileType("slang")
  val sml = FileType("sml")
  val spec = FileType("spec")
  val sql = FileType("sql", "sql")
  val tcl = FileType("tcl")
  val url = FileType("url")
//  val xml = FileType("xml", "xml") // todo encode
  val xorg = FileType("xorg")


  lazy val allExtensions = name2FileType.keys.toSet

  private[this] lazy val name2FileType: Map[String,FileType] =
    instances.flatMap{ f =>
      f.extensions.map{
        _ -> f
      }
    }.toMap

  def getFileType(extension: String): FileType = name2FileType(extension)

  implicit val FileTypeEqual: Equal[FileType] = Equal.equalA
}
