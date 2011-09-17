package com.github.xuwei_k

object HtmlData {

  private[this] def header(t:FileType):Array[Byte] = { 
   val n = t.name

   {"""<html>
    <head>
    <link type="text/css" rel="stylesheet" href="sh_style.css">
    <script type="text/javascript" src="sh_main.js"></script>
    <script type="text/javascript" src="sh_"""+ n +""".js"></script>
    </head>
    <body onload="sh_highlightDocument();">
    <pre class="sh_""" + n + """">"""}.getBytes
  }

  private[this] lazy val footer = 
    """
    </pre>
    </body>
    </html>
    """.getBytes

  /**
   */
  def convertHtml(data:Array[Byte],t:FileType):Array[Byte] = {
    header(t) ++ escapeHtml(data) ++ footer
  }

  val REPLACE = io.Codec("UTF-8").onMalformedInput(java.nio.charset.CodingErrorAction.REPLACE)

  // TODO side effect だらけで汚いｪ・・・(´・ω・｀)
  def escapeHtml(data:Array[Byte]):Array[Byte] = {
    val buf = new StringBuilder

    io.Source.fromBytes(data)(REPLACE).foreach{c =>
      {( c : @annotation.switch) match{
        case '&' => buf append "&amp;"
        case '<' => buf append "&lt;"
        case '>' => buf append "&gt;"
        case '"' => buf append "&quot;"
        case _   => buf append c
      }}
    }

    val ite = io.Source.fromString( buf.toString ).getLines.zipWithIndex
//ここから
    buf.clear // Performanceのために再利用

    ite.foreach{
      case (line,idx) =>
      buf.append("<b>").append("%5s".format(idx+1)).append("</b> | ").append(line).append("\n")
    }
//ここまで行番号表示の処理 todo もっと綺麗に表示させるようにする?

    buf.toString.getBytes
  }
}
