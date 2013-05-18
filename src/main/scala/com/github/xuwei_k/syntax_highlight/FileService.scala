package com.github.xuwei_k.syntax_highlight

import java.net.{URL,HttpURLConnection}

object FileService {

  /**
   * @param url 開くURL
   * @return そのURLのconnection開いたまま返す
   */
  def getConnection(url: String): HttpURLConnection =
    new URL(url).openConnection.asInstanceOf[HttpURLConnection]

}
