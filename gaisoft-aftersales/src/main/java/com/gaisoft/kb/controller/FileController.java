package com.gaisoft.kb.controller;

import com.gaisoft.common.utils.sign.Base64;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/file"})
public class FileController {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @GetMapping(value={"/view"})
    public void proxyPdf(HttpServletResponse response, @RequestParam String pdfUrl) throws Exception {
        if (!pdfUrl.startsWith("https://") && !pdfUrl.startsWith("http://")) {
            response.sendError(400, "无效的 PDF 地址");
            return;
        }
        URL url = new URL(pdfUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(50000);
        connection.setReadTimeout(50000);
        try (InputStream in = connection.getInputStream();
             ServletOutputStream out = response.getOutputStream();){
            int len;
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline;filename=proxy.pdf");
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        }
        finally {
            connection.disconnect();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @GetMapping(value={"/proxyOther"})
    public void proxyOther(HttpServletResponse response, @RequestParam String fileUrl, @RequestParam String suffix) throws Exception {
        fileUrl = new String(Base64.decode((String)fileUrl), "utf-8");
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(50000);
        connection.setReadTimeout(50000);
        try (InputStream in = connection.getInputStream();
             ServletOutputStream out = response.getOutputStream();){
            int len;
            String contentType;
            switch (suffix) {
                case "pdf": {
                    contentType = "application/pdf";
                    break;
                }
                case "xls": {
                    contentType = "application/vnd.ms-excel";
                    break;
                }
                case "xlsx": {
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    break;
                }
                case ".ppt": {
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    break;
                }
                case "jpg":
                case "jpeg": {
                    contentType = "image/jpeg";
                    break;
                }
                case "png": {
                    contentType = "image/png";
                    break;
                }
                default: {
                    contentType = "application/octet-stream";
                }
            }
            response.setContentType(contentType);
            response.setHeader("Content-Security-Policy", "frame-ancestors *;");
            response.setHeader("Content-Disposition", "inline;filename=" + new Date() + "." + suffix);
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        }
        finally {
            connection.disconnect();
        }
    }
}
