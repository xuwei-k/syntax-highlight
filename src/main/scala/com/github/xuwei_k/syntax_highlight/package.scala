package com.github.xuwei_k

import scalaz._;import Scalaz._

package object syntax_highlight{

  private[this] val REPLACE = io.Codec("UTF-8").onMalformedInput(java.nio.charset.CodingErrorAction.REPLACE)

  def bytes2String(data:Array[Byte]):String =
    io.Source.fromBytes(data)(REPLACE).mkString

  implicit val FileTypeEqual = equalA[FileType]

  val TEMPLATE_CSS = "template.css"
}
