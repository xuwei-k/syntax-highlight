package com.github.xuwei_k.syntax_highlight

import pamflet.PamfletDiscounter._

object Markdown{
  def apply(body:String): Array[Byte] = {
    toXHTML(knockoff(body)).toString.getBytes
  }
}

