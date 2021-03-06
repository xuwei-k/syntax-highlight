package com.github.xuwei_k.syntax_highlight

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream}

import com.google.appengine.api.mail.MailService
import org.apache.commons.fileupload.FileItemStream
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.scalatra.ScalatraFilter

import scala.collection.JavaConversions._

object Front{
  type InputFile = (String, InputStream) //todo classにする？

  private implicit def toScalaIterator[A](ite:{def next(): A; def hasNext(): Boolean}) =
    new Iterator[A]{
      override def next = ite.next
      override def hasNext = ite.hasNext
    }

  def getUploadFile(in: FileItemStream): InputFile = {
    val stream = in.openStream
    val out = new ByteArrayOutputStream
    val fileName = in.getName
    val buf = new Array[Byte](1024 * 1024)

    @annotation.tailrec
    def read(): Unit = {
      val len = stream.read(buf, 0, buf.length)
      if(len != -1){
        out.write(buf, 0, len)
        read()
      }
    }
    read()

    (fileName, new ByteArrayInputStream(out.toByteArray))
  }

  private val FileNameR = ".*filename=(.+)".r
}

final class Front extends ScalatraFilter {
  import Front._

  get("/*") {
    redirect("/")
  }

  post("/source.zip") { //urlからフェッチする場合
    catchErrorAndPrint{
    params.get("url") match {
      case Some(url) =>
        val con = FileService.getConnection(url)
        System.err.println(con.getHeaderFields)
        val fileName = {for{
          d <- Option(con.getHeaderFields.get("content-disposition"))
          f <- d.collectFirst{case FileNameR(n) => n}
        }yield f}.getOrElse("source.zip")

        val convertedData = Source2html.sourceFiles2html(con.getInputStream)

        params.get("mail").foreach{ address =>
          MyMailService.send(address, new MailService.Attachment(fileName + ".txt", convertedData))
        }

        if (params.get("download").isDefined) {
          DownloadService.download(response, fileName, convertedData)
        }

      case None => redirect("/index.html")
    }
    }
  }

  def catchErrorAndPrint[A](body: => A) = {
    try{
      body
    }catch{
      case e: Throwable =>
        e.printStackTrace
        status(500)
        e.getStackTrace.mkString(e.toString + "\n\n","\n","")
    }
  }

  /**
   * requestから、uploadされたfileとパラメータを抽出
   */
  def getUploadFiles(): List[InputFile] = {

    val list = new ServletFileUpload().getItemIterator(request)

    list.collect{case f if !f.isFormField => getUploadFile(f) }.toList
  }

  /**
   * 変換後のファイルを、mailまたは、responseとして転送
   * @param name
   * @param data
   */
  def transferData(name: String, data: Array[Byte]) {

    if (params.isDefinedAt("mail")) {
      params.get("mail_address").foreach{ addr =>
        MyMailService.send(addr,new MailService.Attachment(name + ".txt", data))
      }
    }

    if (params.isDefinedAt("download")) {
      DownloadService.download(response, name, data)
    }else{
      redirect("/index.html")
    }
  }

  // TODO use https://github.com/scalatra/scalatra/blob/2.0.3/fileupload/src/main/scala/org/scalatra/fileupload/FileUploadSupport.scala
  post("/file_upload") { //Userが自分のfileをuploadした場合
    catchErrorAndPrint{
    for{
      (name,data) <- getUploadFiles.headOption//todo 複数ファイル対応
      if name.length > 0 //このチェックおかしい?
    }{
      val convertedData = Source2html.sourceFiles2html(data)
      transferData(name,convertedData)
    }
    }
  }

  post("/_ah/mail/*") { //mailを受信して、メールで返信
    import javax.mail.Session
    import javax.mail.internet.MimeMessage

    import com.google.appengine.api.mail.MailService

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

