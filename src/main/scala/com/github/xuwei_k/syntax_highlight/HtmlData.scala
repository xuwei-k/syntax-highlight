package com.github.xuwei_k.syntax_highlight

object HtmlData {

  private[this] def header(t: FileType): Array[Byte] = {
   val n = t.name

   {"""<html>
    <head>
    <meta name="format-detection" content="telephone=no" />
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
  def convertHtml(data: Array[Byte], t: FileType): Array[Byte] = {
    header(t) ++ escapeHtml(data) ++ footer
  }

  // TODO side effect だらけで汚いｪ・・・(´・ω・｀)
  def escapeHtml(data: Array[Byte]): Array[Byte] = {
    import scala.xml.Utility.escape

    val buf = escape(bytes2String(data),new StringBuilder )

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
