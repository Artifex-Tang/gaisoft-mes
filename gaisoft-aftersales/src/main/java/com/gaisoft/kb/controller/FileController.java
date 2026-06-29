package com.gaisoft.kb.controller;

import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.sign.Base64;
import com.gaisoft.system.service.ISysConfigService;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/file"})
public class FileController {
    @Autowired
    private ISysConfigService iSysConfigService;

    private String getRagflowAuth() {
        String apiKey = this.iSysConfigService.selectConfigByKey("RagFlowKey");
        if (StringUtils.isNotEmpty(apiKey)) {
            return "Bearer " + apiKey;
        }
        return "";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @GetMapping(value={"/view"})
    public void proxyPdf(HttpServletResponse response, @RequestParam String pdfUrl,
            @RequestParam(value="suffix", required=false, defaultValue="pdf") String suffix) throws Exception {
        if (!pdfUrl.startsWith("https://") && !pdfUrl.startsWith("http://")) {
            response.sendError(400, "无效的文件地址");
            return;
        }
        URL url = new URL(pdfUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(50000);
        connection.setReadTimeout(50000);
        String auth = getRagflowAuth();
        if (StringUtils.isNotEmpty(auth)) {
            connection.setRequestProperty("Authorization", auth);
        }
        try (InputStream in = connection.getInputStream();
             ServletOutputStream out = response.getOutputStream();){
            int len;
            response.setContentType(contentTypeOf(suffix));
            response.setHeader("Content-Disposition", "inline;filename=proxy." + suffix);
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

    /** Map file suffix to MIME type (pdf default). */
    private String contentTypeOf(String suffix) {
        if (suffix == null) {
            return "application/pdf";
        }
        switch (suffix) {
            case "png": return "image/png";
            case "jpg":
            case "jpeg": return "image/jpeg";
            case "gif": return "image/gif";
            case "xls": return "application/vnd.ms-excel";
            case "xlsx": return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt": return "application/vnd.ms-powerpoint";
            case "pptx": return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "doc": return "application/msword";
            case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            default: return "application/pdf";
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
        String auth = getRagflowAuth();
        if (StringUtils.isNotEmpty(auth)) {
            connection.setRequestProperty("Authorization", auth);
        }
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
