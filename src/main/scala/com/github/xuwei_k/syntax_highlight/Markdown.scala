package com.github.xuwei_k.syntax_highlight

import org.pegdown._

object Markdown{
  def apply(body: String): Array[Byte] = {
    (new PegDownProcessor(
       Extensions.AUTOLINKS | Extensions.WIKILINKS | Extensions.FENCED_CODE_BLOCKS | Extensions.TABLES | Extensions.HARDWRAPS
    )).markdownToHtml(body).getBytes
  }
}

