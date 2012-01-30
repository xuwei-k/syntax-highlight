package com.github.xuwei_k

import javax.servlet.http.{ HttpServletResponse => Response }

object DownloadService {

  def download(response: Response, fileName:String , data: Array[Byte]) {
    response.setContentType("application/zip")
    response.setHeader("Content-Disposition", "attachment; filename=" + fileName )
    Using.using(response.getOutputStream){ _.write(data) }
  }
}
