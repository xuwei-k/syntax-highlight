package com.github.xuwei_k

import javax.servlet.http._
import org.scalatra.{ ScalatraFilter }
import org.apache.commons.fileupload.{FileItemStream,FileItemIterator}
import org.apache.commons.fileupload.servlet.ServletFileUpload
import scala.collection.JavaConversions._
import java.io.{ ByteArrayOutputStream, ByteArrayInputStream, File, InputStream }
import com.google.appengine.api.mail.{ MailService }

/**
 * request の urlによって処理振り分ける
 */
class Front extends ScalatraFilter {

  implicit def toScalaIterator[A](ite:{def next():A;def hasNext():Boolean}) =
    new Iterator[A]{
      def next = ite.next
      def hasNext = ite.hasNext
    }


  get("/*") {
    "hello"
  }

  post("/source.zip") { //urlからフェッチする場合

    params.get("url") match {
      case Some(url) => {
        val convertedData = Source2html.sourceFiles2html(FileService.getConnection(url).getInputStream)

        val fileName = "source" //todo 取得して設定

        params.get("mail").map { address =>
          MyMailService.send(address, new MailService.Attachment(fileName + ".txt", convertedData))
        }

        if (params.get("download").isDefined) {
          DownloadService.download(response, fileName, convertedData)
        }
      }

      case None => response.sendRedirect("/index.html")
    }
  }

  /**
   * requestから、uploadされたfileとパラメータを抽出
   */
  def getUploadFiles():List[InputFile] = {

    val list = new ServletFileUpload().getItemIterator(request)

    list.collect{case f if !f.isFormField => getUploadFile(f) }.toList
  }

  type InputFile = (String,InputStream) //todo classにする？

  def getUploadFile(in:FileItemStream):InputFile = {
    val stream = in.openStream
    val out = new ByteArrayOutputStream
    val fileName = in.getName

    var len = 0
    val buf = new Array[Byte](1024 * 1024)
    while ({ len = stream.read(buf, 0, buf.length); len } != -1) {
      out.write(buf, 0, len)
    }
    (fileName, new ByteArrayInputStream(out.toByteArray))
  }

  /**
   * 変換後のファイルを、mailまたは、responseとして転送
   * @param name
   * @param data
   */
  def transferData(name: String, data: Array[Byte]) {

    if (params.isDefinedAt("mail") && params.isDefinedAt("mail_address")) {
      MyMailService.send(params("mail_address"), new MailService.Attachment(name + ".txt", data))
    }

    if (params.isDefinedAt("download")) {
      DownloadService.download(response, name, data)
    }else{
      redirect("/")	
    }
  }

  post("/file_upload") { //Userが自分のfileをuploadした場合

    val files = getUploadFiles //uploadされたファイル取得

    if(! files.isEmpty && files.head._1.length > 0){

      val f = files.head //todo 複数ファイル対応
      val convertedData = Source2html.sourceFiles2html( f._2 ) //変換

      transferData( f._1 ,convertedData)
    }
  }

  post("/_ah/mail/*") { //mailを受信して、メールで返信
    import javax.mail.{ Session }
    import javax.mail.internet.MimeMessage
    import com.google.appengine.api.mail.{ MailService }

    val s = Session.getInstance(System.getProperties)
    val msg = new MimeMessage(s, request.getInputStream)

    val senderAddress = msg.getFrom.apply(0).toString //メールを送ってきた人のアドレス

    //添付ファイルを取得して、それぞれ変換
    val attachments =
      MyMailService.partAnalysis(msg, Nil).map { file =>
        new MailService.Attachment(
          file.name + ".txt", //zipだと、GAEの制限で送れないので、騙して送る
          Source2html.sourceFiles2html(file.data))
      }

    MyMailService.send(senderAddress, attachments: _*)
  }
}

