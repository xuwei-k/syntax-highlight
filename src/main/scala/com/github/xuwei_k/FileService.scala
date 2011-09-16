package com.github.xuwei_k

import javax.servlet.http.{HttpServletResponse => Res}
import java.net.{URL,HttpURLConnection}
import java.util.zip._
import java.io._
import scala.collection.{mutable => mu }
import Using._

object FileService {
  
  /** 
   * @param url 開くURL
   * @return そのURLのconnection開いたまま返す
   */
  def getConnection(url:String):HttpURLConnection =
    new URL(url).openConnection.asInstanceOf[HttpURLConnection]
  
}