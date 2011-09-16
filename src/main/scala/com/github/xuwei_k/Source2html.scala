package com.github.xuwei_k

import java.util.zip._
import java.io._
import scala.collection.{mutable => mu }
import Using._

/** source fileをhtmlに変換する処理
 * @since 2011/03/23 2:59:18
 */
object Source2html {

  /** jsとかcss */
  def addFiles(fType:FileType*):Seq[ByteFile] =
    fType.map{_.jsFile} ++ Seq("sh_main.js","sh_style.css").map{ f =>
      ByteFile(f , fileToByteArray("./resource/" + f))
    }
	
  /** ファイルをbyte配列にして返す
   */
  def fileToByteArray(fileName:String):Array[Byte] = {
    val file = new File(fileName)
    val buf = new Array[Byte](file.length toInt)
    using(new FileInputStream(file)){
      in =>
      in.read(buf)
    }
    buf
  }
  
  /**
   * @param in zipになってるsource file
   * @param out 作成したzipの出力先
   */
  def sourceFiles2html(in:InputStream,out:OutputStream){
    val files = ZipUtil.extractFileList( in )

    val map = ZipUtil.getFileMap( files )
    
    for{
      (dir,fileTypes) <- map
      f <- addFiles(fileTypes.toSeq:_*)
    }(files += f.copy(name = {dir + f.name}))
    
    ZipUtil.encode(new ZipOutputStream( out ), files )
  }
  
  def sourceFiles2html(in:InputStream):Array[Byte] = {
    val out = new java.io.ByteArrayOutputStream
    Source2html.sourceFiles2html( in ,out)
    out.toByteArray
  }
}