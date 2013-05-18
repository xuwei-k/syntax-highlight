package com.github.xuwei_k.syntax_highlight

import java.util.zip._
import java.io._
import scala.collection.{ mutable => mu }

/** zipの処理に関するutility
 */
object ZipUtil {

  // 大きいjarが含まれているとtimeoutで失敗するので、必要なさそうなファイルは省く
  val defaultFilter = { e: ZipEntry =>
     (! e.getName.endsWith("jar") ) && (e.getSize < 100000)
  }

  /** zipのファイルを解凍して、それぞれをByteFileというentityにする
   * @todo このfilterの条件を、URLのparameterで受け取れるようにする
   * @todo filterしたファイル名一覧を書いたtextファイルを添付するなどして、
   *       Download したユーザーがわかるようにする
   */
  def extractFileList(in: InputStream, filter: ZipEntry => Boolean = defaultFilter): Seq[ByteFile] = {
    resource.managed(new ZipInputStream(in)).acquireAndGet{ zipIn =>
      val buf = new Array[Byte](4096)
      val builder = new mu.ArrayBuilder.ofByte
      Iterator.continually(zipIn.getNextEntry).takeWhile{
        null ne
      }.filter{
        filter
      }.filterNot{
        _.isDirectory
      }.map{ e =>
        @annotation.tailrec
        def read(){
          val len = zipIn.read(buf)
          if(len != -1){
            builder ++= buf.take(len)
            read()
          }
        }
        read()
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
  def encode(zos: ZipOutputStream, files: Traversable[ByteFile]) {
    for {
      f <- files
    } {
      val name = f.convertedName.replace('\\', '/')
      zos.putNextEntry(new ZipEntry(name))
      zos.write(f.data)
    }
    zos.close
  }

  def multiMap[A, B]() = new mu.HashMap[A, mu.Set[B]] with mu.MultiMap[A, B]

  /** syntax highlight用のjsのfileを加えるのに必要な情報を取得
   * fileの一覧から
   * Map(ディレクトリ -> そこに存在するFileTypeのSet)
   * を作成
   */
  def getFileMap(files: Traversable[ByteFile]): mu.HashMap[Dir, mu.Set[FileType]] = {

    val map = multiMap[Dir, FileType]()
    for {
      f <- files
      if f.isSupport
    } {
      map.addBinding(f.parentDir, f.fileType.get)
    }
    map
  }
}
