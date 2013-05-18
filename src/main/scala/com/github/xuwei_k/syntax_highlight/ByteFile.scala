package com.github.xuwei_k.syntax_highlight

import scalaz._,Scalaz._
import java.io._
import scala.tools.nsc.doc.html.SyntaxHigh

/**
 * @param name fileの名前
 * @param data fileの中身
 */
case class ByteFile(name: String, originalData: Array[Byte]) {
  import ByteFile._

  lazy val convertedName = if(isSupport || isMarkdown) name + ".html" else name

  /** (変換できるならば)変換後のbinaryのデータ */
  lazy val data: Array[Byte] =
    if(fileType === Some(FileType.scala)){
      {<html>
         <head><link type="text/css" media="screen" rel="stylesheet" href={TEMPLATE_CSS} /></head>
         <body><div class="cmt"><pre>{
           SyntaxHigh(originalData)
         }</pre></div></body>
       </html>}.toString.getBytes
    }else{
      fileType.map{ fType =>
        HtmlData.convertHtml(originalData, fType)
      }.getOrElse{
        if(isMarkdown) Markdown(bytes2String(originalData))
        else originalData
      }
    }

  /** 拡張子 */
  private lazy val fileExtension: Option[String] = name.split('.').lastOption

  /** 対応しているファイルの種類か? */
  lazy val isSupport: Boolean =
    fileExtension.map{FileType.allExtentions.contains} | false

  /** syntax highlightに対応してなければNone */
  lazy val fileType: Option[FileType] =
    for {
      f <- fileExtension
      if isSupport
    } yield FileType.getFileType(f)

  /** そのファイルが含まれているdirectory名 */
  lazy val parentDir: ZipUtil.Dir = name.reverse.dropWhile(_ /== '/').reverse

  lazy val isMarkdown: Boolean =
    fileExtension.map{e => MDextensions.exists(e.equalsIgnoreCase)} | false

}

object ByteFile{
  private val MDextensions = Set("md","markdown")
}
