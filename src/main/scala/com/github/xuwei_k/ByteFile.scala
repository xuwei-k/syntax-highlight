package com.github.xuwei_k

import java.io._

/**
 * @param name fileの名前
 * @param data fileの中身
 */
case class ByteFile(val name: String, originalData: Array[Byte]) {
  import ByteFile._

  /** (変換できるならば)変換後のbinaryのデータ */
  lazy val data =
    fileType.map{ fType =>
      HtmlData.convertHtml(originalData, fType)
    }.getOrElse{
      originalData
    }

  /** 拡張子 */
  lazy val fileExtension: String = name.split("\\.").lastOption.getOrElse("")

  /** 対応しているファイルの種類か? */
  lazy val isSupport: Boolean =
    FileType.allExtentions.contains(fileExtension)

  /** syntax highlightに対応してなければNone */
  lazy val fileType: Option[FileType] =
    for {
      f <- Some(this.fileExtension)
      if isSupport
    } yield FileType.getFileType(f)

  /** そのファイルが含まれているdirectory名 */
  lazy val parentDir: ZipUtil.Dir = name.reverse.dropWhile(_ != '/').reverse

}

