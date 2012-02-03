package com.github.xuwei_k.syntax_highlight

import java.io._

/**
 * @param name fileの名前
 * @param data fileの中身
 */
case class ByteFile(val name: String, originalData: Array[Byte]) {
  import ByteFile._

  val convertedName = if(isSupport || isMarkdown) name + ".html" else name

  /** (変換できるならば)変換後のbinaryのデータ */
  lazy val data:Array[Byte] =
    fileType.map{ fType =>
      HtmlData.convertHtml(originalData, fType)
    }.getOrElse{
      if(isMarkdown) Markdown(bytes2String(originalData))
      else originalData
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

  lazy val isMarkdown: Boolean = Set("md","markdown").exists(fileExtension.equalsIgnoreCase)

}

