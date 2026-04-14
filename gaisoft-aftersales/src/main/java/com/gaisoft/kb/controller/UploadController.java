package com.gaisoft.kb.controller;

import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.sign.Base64;
import com.gaisoft.kb.service.IThirdPartyFileUploadService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/kb"})
public class UploadController {
    @Autowired
    private IThirdPartyFileUploadService thirdPartyFileUploadService;

    @PostMapping(value={"/upload"})
    public AjaxResult uploadFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return AjaxResult.error((String)"上传文件不能为空");
            }
            String result = this.thirdPartyFileUploadService.uploadToThirdParty(file);
            if (StringUtils.isNotEmpty((String)result)) {
                return AjaxResult.success((String)"文件上传成功", (Object)result);
            }
            return AjaxResult.error((String)"文件上传第三方失败");
        }
        catch (Exception e) {
            return AjaxResult.error((String)("上传文件异常：" + e.getMessage()));
        }
    }

    @PostMapping(value={"/uploadforkb"})
    public AjaxResult uploadFile(MultipartFile file, String kb_id) {
        try {
            if (file.isEmpty()) {
                return AjaxResult.error((String)"上传文件不能为空");
            }
            String result = this.thirdPartyFileUploadService.uploadToThirdParty(file, kb_id);
            if (StringUtils.isNotEmpty((String)result)) {
                return AjaxResult.success((String)"文件上传成功", (Object)result);
            }
            return AjaxResult.error((String)"文件上传第三方失败");
        }
        catch (Exception e) {
            return AjaxResult.error((String)("上传文件异常：" + e.getMessage()));
        }
    }

    @GetMapping(value={"/download"})
    public ResponseEntity<byte[]> downloadFile(@RequestParam String url, @RequestParam String fileName) {
        try {
            byte[] fileData = this.thirdPartyFileUploadService.downloadFileByUrl(url);
            if (fileData == null || fileData.length == 0) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            HttpHeaders headers = new HttpHeaders();
            if (fileName == null || fileName.trim().isEmpty()) {
                fileName = this.extractFileNameFromUrl(url);
            } else {
                fileName = fileName.replaceAll(" ", "");
                fileName = new String(Base64.decode((String)fileName), "utf-8");
            }
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            headers.add("Content-Length", String.valueOf(fileData.length));
            return new ResponseEntity((Object)fileData, (MultiValueMap)headers, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractFileNameFromUrl(String url) {
        int lastSlashIndex;
        if (url == null || url.isEmpty()) {
            return "downloaded_file";
        }
        int paramIndex = url.indexOf(63);
        if (paramIndex != -1) {
            url = url.substring(0, paramIndex);
        }
        if ((lastSlashIndex = url.lastIndexOf(47)) != -1 && lastSlashIndex < url.length() - 1) {
            return url.substring(lastSlashIndex + 1);
        }
        return "downloaded_file";
    }
}
