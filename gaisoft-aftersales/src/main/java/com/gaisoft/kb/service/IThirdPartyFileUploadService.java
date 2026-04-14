package com.gaisoft.kb.service;

import org.springframework.web.multipart.MultipartFile;

public interface IThirdPartyFileUploadService {
    public String uploadToThirdParty(MultipartFile var1) throws Exception;

    public String uploadToThirdParty(MultipartFile var1, String var2) throws Exception;

    public byte[] downloadFileByUrl(String var1) throws Exception;
}
