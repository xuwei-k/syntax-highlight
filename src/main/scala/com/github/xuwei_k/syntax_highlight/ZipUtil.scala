package com.github.xuwei_k.syntax_highlight

import java.util.zip._
import java.io._
import scala.collection.{ mutable => mu }
import Using._

/** zipの処理に関するutility
 */
object ZipUtil {

  /**
   * zipのファイルを解凍して、それぞれをByteFileというentityにする
   */
  def extractFileList(in: InputStream): mu.ArrayBuffer[ByteFile] = {

    val fileList = mu.ArrayBuffer[ByteFile]()

    using(new ZipInputStream(in)) { zipIn =>

      var i: ZipEntry = null // TODO var !!! use loanPattern ?
      val buf = new Array[Byte](4096)
      val builder = new mu.ArrayBuilder.ofByte
      while ({ i = zipIn.getNextEntry; i != null }) {
        if (!i.isDirectory) {
          var length = 0
          while ({ length = zipIn.read(buf); length != -1 }) {
            builder ++= (buf.take(length))
          }
          val file = new ByteFile(i.getName, builder.result)
          fileList += file
        }
        builder.clear()
      }
    }

    fileList
  }

  //ディレクトリはとりあえずStringで保持してるけど、わかりやすいようにalias
  type Dir = String

  /** byteのdataをzipのstreamに書きだす
   * @param zos Zipの出力のためのStream
   * @param files 圧縮するファイルの元データのファイル一覧
   */
  def encode(zos: ZipOutputStream,files: Traversable[ByteFile]) {
    for {
      f <- files
    } {
      val name = f.convertedName.replace('\\', '/')
      zos.putNextEntry(new ZipEntry(name))
      zos.write(f.data)
    }
    zos.close
  }

  /** syntax highlight用のjsのfileを加えるのに必要な情報を取得
   * fileの一覧から
   * Map(ディレクトリ -> そこに存在するFileTypeのSet)
   * を作成
   */
  def getFileMap(files: Traversable[ByteFile]): mu.HashMap[Dir, mu.Set[FileType]] = {

    //なんでMultiMap作るだけなのに、こんなに長いの？
    val map = new mu.HashMap[Dir, mu.Set[FileType]] with mu.MultiMap[Dir, FileType]
    for {
      f <- files
      if f.isSupport
    } {
      map.addBinding(f.parentDir, f.fileType.get)
    }
    map
  }
}
