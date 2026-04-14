package com.gaisoft.kb.service.impl;

import com.gaisoft.kb.controller.GetAuthorization;
import com.gaisoft.kb.service.IThirdPartyFileUploadService;
import com.gaisoft.system.service.ISysConfigService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ThirdPartyFileUploadServiceImpl
implements IThirdPartyFileUploadService {
    @Autowired
    GetAuthorization getAuthorization;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ISysConfigService iSysConfigService;

    @Override
    public String uploadToThirdParty(MultipartFile file) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.add("Authorization", this.getAuthorization.getAuthorization());
            headers.add("Content-Type", "multipart/form-data");
            LinkedMultiValueMap body = new LinkedMultiValueMap();
            body.add((Object)"file", (Object)new MultipartFileResource(file.getOriginalFilename(), file.getInputStream()));
            body.add((Object)"file", (Object)new MultipartFileResource(file.getOriginalFilename(), file.getInputStream()));
            HttpEntity requestEntity = new HttpEntity((Object)body, (MultiValueMap)headers);
            String response = (String)this.restTemplate.postForObject(this.iSysConfigService.selectConfigByKey("RagFlowServerBaseUrl") + "/v1/file/upload", (Object)requestEntity, String.class, new Object[0]);
            return response;
        }
        catch (IOException e) {
            throw new Exception("文件处理异常：" + e.getMessage());
        }
        catch (Exception e) {
            throw new Exception("调用第三方接口异常：" + e.getMessage());
        }
    }

    @Override
    public String uploadToThirdParty(MultipartFile file, String kb_id) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.add("Authorization", this.getAuthorization.getAuthorization());
            headers.add("Content-Type", "multipart/form-data");
            LinkedMultiValueMap body = new LinkedMultiValueMap();
            body.add((Object)"file", (Object)new MultipartFileResource(file.getOriginalFilename(), file.getInputStream()));
            body.add((Object)"kb_id", (Object)kb_id);
            HttpEntity requestEntity = new HttpEntity((Object)body, (MultiValueMap)headers);
            String response = (String)this.restTemplate.postForObject(this.iSysConfigService.selectConfigByKey("RagFlowServerBaseUrl") + "/v1/document/upload", (Object)requestEntity, String.class, new Object[0]);
            return response;
        }
        catch (IOException e) {
            throw new Exception("文件处理异常：" + e.getMessage());
        }
        catch (Exception e) {
            throw new Exception("调用第三方接口异常：" + e.getMessage());
        }
    }

    @Override
    public byte[] downloadFileByUrl(String url) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", this.getAuthorization.getAuthorization());
            HttpEntity requestEntity = new HttpEntity((MultiValueMap)headers);
            ResponseEntity response = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity, byte[].class, new Object[0]);
            if (response.getStatusCode() == HttpStatus.OK) {
                return (byte[])response.getBody();
            }
            throw new Exception("下载文件失败，状态码：" + response.getStatusCodeValue());
        }
        catch (Exception e) {
            throw new Exception("下载文件异常：" + e.getMessage());
        }
    }

    private static class MultipartFileResource
    implements Resource {
        private final String filename;
        private final InputStream inputStream;

        public MultipartFileResource(String filename, InputStream inputStream) {
            this.filename = filename;
            this.inputStream = inputStream;
        }

        public InputStream getInputStream() throws IOException {
            return this.inputStream;
        }

        public String getFilename() {
            return this.filename;
        }

        public boolean exists() {
            return false;
        }

        public boolean isReadable() {
            return true;
        }

        public boolean isOpen() {
            return false;
        }

        public URL getURL() throws IOException {
            return null;
        }

        public URI getURI() throws IOException {
            return null;
        }

        public File getFile() throws IOException {
            return null;
        }

        public long contentLength() throws IOException {
            return 0L;
        }

        public long lastModified() throws IOException {
            return 0L;
        }

        public Resource createRelative(String relativePath) throws IOException {
            return null;
        }

        public String getDescription() {
            return null;
        }
    }
}
