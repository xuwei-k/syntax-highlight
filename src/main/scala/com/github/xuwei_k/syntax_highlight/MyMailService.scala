package com.github.xuwei_k.syntax_highlight

import java.io._
import javax.mail._
import javax.mail.internet._
import com.google.appengine.api.mail.{MailService, MailServiceFactory}

object MyMailService {
  //添付ファイル
  final case class Attachment(name: String, data: InputStream)

  /** メール送信
   */
  def send(sendAddress: String, attachments: MailService.Attachment *): Unit ={

    //なにかしら、本文に文字を入れないと、送信できないらしい!!!
    val message = new MailService.Message("test1@syntax-highlight.appspotmail.com", sendAddress, "test", "body")
    message.setAttachments(attachments:_*)
    MailServiceFactory.getMailService.send(message)
  }

  /**
   * メール解析
   */
  def partAnalysis(part: Part, list: List[Attachment]): List[Attachment] = {

    if (part.isMimeType("multipart/*")) {

      val content = part.getContent.asInstanceOf[Multipart]
      // 含まれるパートを再帰的に処理
      list ++ {
        (0 until content.getCount).flatMap{
          n => partAnalysis(content.getBodyPart(n),Nil)
        }
      }
    } else if (part.isMimeType("text/plain")) {
      // テキストの場合なにもしない
      list
    } else {
      // その他の場合
      val disp = part.getDisposition

      if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
        getAttachments(part) :: list
      }else{
        list
      }
    }
  }

  /** 添付ファイル取得
   */
  private def getAttachments(part: Part): Attachment = {
    val name = MimeUtility.decodeText(part.getFileName())
    Attachment(name, part.getInputStream)
  }
}
