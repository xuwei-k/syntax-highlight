package com.github.xuwei_k.syntax_highlight

import javax.servlet.http.{ HttpServletResponse => Response }

object DownloadService {

  def download(response: Response, fileName: String , data: Array[Byte]): Unit = {
    response.setContentType("application/zip")
    response.setHeader("Content-Disposition", "attachment; filename=" + fileName )
    resource.managed(response.getOutputStream).foreach{ _.write(data) }
  }
}
