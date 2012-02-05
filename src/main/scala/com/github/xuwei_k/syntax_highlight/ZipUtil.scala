package com.github.xuwei_k.syntax_highlight

import java.util.zip._
import java.io._
import scala.collection.{ mutable => mu }

/** zipの処理に関するutility
 */
object ZipUtil {

  /** zipのファイルを解凍して、それぞれをByteFileというentityにする
   */
  def extractFileList(in: InputStream): Seq[ByteFile] = {
    resource.managed(new ZipInputStream(in)).acquireAndGet{ zipIn =>
      val buf = new Array[Byte](4096)
      val builder = new mu.ArrayBuilder.ofByte
      Iterator.continually(zipIn.getNextEntry).takeWhile{
        null ne
      }.filterNot{
        _.isDirectory
      }.map{ e =>
        var length = 0
        while ({ length = zipIn.read(buf); length != -1 }) {
          builder ++= (buf.take(length))
        }
        val file = new ByteFile(e.getName, builder.result)
        builder.clear()
        file
      }.toBuffer
    }
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
