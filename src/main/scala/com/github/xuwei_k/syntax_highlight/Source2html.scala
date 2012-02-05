package com.github.xuwei_k.syntax_highlight

import java.util.zip._
import java.io._
import scala.collection.{mutable => mu }

/** source fileをhtmlに変換する処理
 */
object Source2html {
  private[this] val commonJS =
    Seq("sh_main.js","sh_style.css").map{ f =>
      ByteFile(f , fileToByteArray("./resource/" + f))
    }

  /** jsとかcss */
  private def addFiles(fType:FileType*):Seq[ByteFile] =
    fType.map{_.jsFile} ++ commonJS

  /** ファイルをbyte配列にして返す
   */
  def fileToByteArray(fileName:String):Array[Byte] = {
    val file = new File(fileName)
    val buf = new Array[Byte](file.length toInt)
    resource.managed(new FileInputStream(file)).foreach{
      _.read(buf)
    }
    buf
  }

  /**
   * @param in zipになってるsource file
   * @param out 作成したzipの出力先
   */
  private def sourceFiles2html(in:InputStream,out:OutputStream){
    val files = ZipUtil.extractFileList( in )

    val newFiles = for{
      (dir,fileTypes) <- ZipUtil.getFileMap( files )
      f <- addFiles(fileTypes.toSeq:_*)
    } yield f.copy(name = {dir + f.name})

    ZipUtil.encode(new ZipOutputStream( out ), files ++ newFiles )
  }

  def sourceFiles2html(in:InputStream):Array[Byte] = {
    val out = new java.io.ByteArrayOutputStream
    Source2html.sourceFiles2html( in ,out)
    out.toByteArray
  }
}
