package com.gaisoft.web.controller.common;

import com.gaisoft.common.config.RuoYiConfig;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.file.FileUploadUtils;
import com.gaisoft.common.utils.file.FileUtils;
import com.gaisoft.framework.config.ServerConfig;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/common"})
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private ServerConfig serverConfig;
    private static final String FILE_DELIMETER = ",";

    @GetMapping(value={"/download"})
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload((String)fileName)) {
                throw new Exception(StringUtils.format((String)"文件名称({})非法，不允许下载。 ", (Object[])new Object[]{fileName}));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;
            response.setContentType("application/octet-stream");
            FileUtils.setAttachmentResponseHeader((HttpServletResponse)response, (String)realFileName);
            FileUtils.writeBytes((String)filePath, (OutputStream)response.getOutputStream());
            if (delete.booleanValue()) {
                FileUtils.deleteFile((String)filePath);
            }
        }
        catch (Exception e) {
            log.error("下载文件失败", (Throwable)e);
        }
    }

    @PostMapping(value={"/upload"})
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            String filePath = RuoYiConfig.getUploadPath();
            String fileName = FileUploadUtils.upload((String)filePath, (MultipartFile)file);
            String url = this.serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", (Object)url);
            ajax.put("fileName", (Object)fileName);
            ajax.put("newFileName", (Object)FileUtils.getName((String)fileName));
            ajax.put("originalFilename", (Object)file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e) {
            return AjaxResult.error((String)e.getMessage());
        }
    }

    @PostMapping(value={"/uploads"})
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception {
        try {
            String filePath = RuoYiConfig.getUploadPath();
            ArrayList<String> urls = new ArrayList<String>();
            ArrayList<String> fileNames = new ArrayList<String>();
            ArrayList<String> newFileNames = new ArrayList<String>();
            ArrayList<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files) {
                String fileName = FileUploadUtils.upload((String)filePath, (MultipartFile)file);
                String url = this.serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName((String)fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", (Object)StringUtils.join(urls, (String)FILE_DELIMETER));
            ajax.put("fileNames", (Object)StringUtils.join(fileNames, (String)FILE_DELIMETER));
            ajax.put("newFileNames", (Object)StringUtils.join(newFileNames, (String)FILE_DELIMETER));
            ajax.put("originalFilenames", (Object)StringUtils.join(originalFilenames, (String)FILE_DELIMETER));
            return ajax;
        }
        catch (Exception e) {
            return AjaxResult.error((String)e.getMessage());
        }
    }

    @GetMapping(value={"/download/resource"})
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            if (!FileUtils.checkAllowDownload((String)resource)) {
                throw new Exception(StringUtils.format((String)"资源文件({})非法，不允许下载。 ", (Object[])new Object[]{resource}));
            }
            String localPath = RuoYiConfig.getProfile();
            String downloadPath = localPath + StringUtils.substringAfter((String)resource, (String)"/profile");
            String downloadName = StringUtils.substringAfterLast((String)downloadPath, (String)"/");
            response.setContentType("application/octet-stream");
            FileUtils.setAttachmentResponseHeader((HttpServletResponse)response, (String)downloadName);
            FileUtils.writeBytes((String)downloadPath, (OutputStream)response.getOutputStream());
        }
        catch (Exception e) {
            log.error("下载文件失败", (Throwable)e);
        }
    }
}
