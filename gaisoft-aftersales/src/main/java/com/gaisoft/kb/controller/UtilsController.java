package com.gaisoft.kb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.http.HttpUtils;
import com.gaisoft.kb.domain.CommonDto;
import com.gaisoft.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/ragflow"})
public class UtilsController {
    @Autowired
    private ISysConfigService iSysConfigService;
    @Autowired
    GetAuthorization getAuthorization;

    /**
     * ragflow 0.18.0: all endpoints accept API key auth.
     * Use Bearer token for everything; fall back to session cookie only if no key configured.
     */
    private String getAuth(String url) {
        String apiKey = this.iSysConfigService.selectConfigByKey("RagFlowKey");
        if (StringUtils.isNotEmpty(apiKey)) {
            return "Bearer " + apiKey;
        }
        // Legacy fallback: session cookie auth
        return this.getAuthorization.getAuthorization();
    }

    private String doRequest(String base, String url, String method, String params, String auth) {
        if ("get".equals(method)) {
            return StringUtils.isNotEmpty(params)
                ? HttpUtils.sendGet(base + url, params, "application/json;charset=UTF-8", auth)
                : HttpUtils.sendGet(base + url, auth);
        }
        return HttpUtils.sendPost(base + url, params, "application/json;charset=UTF-8", method, auth);
    }

    /**
     * Check if ragflow response indicates auth failure (code 401).
     * Since all requests use API key, retry won't help on 401.
     * Never return code 401 to frontend to avoid triggering user re-login dialog.
     */
    private String handleResponse(String response, String base, String url, String method, String params) {
        if (response == null) {
            return "{\"code\":500,\"data\":null,\"message\":\"Ragflow returned empty response\"}";
        }
        // API key auth failed — return 500, not 401
        if (response.contains("\"code\":401") || response.contains("\"code\": 401")) {
            return "{\"code\":500,\"data\":null,\"message\":\"Ragflow API key authentication failed\"}";
        }
        return response;
    }

    @PostMapping(value={"/common"})
    public String common(@RequestBody CommonDto dto) throws JsonProcessingException {
        String base = this.iSysConfigService.selectConfigByKey("RagFlowServerBaseUrl");
        String auth = this.getAuth(dto.getUrl());
        if (StringUtils.isEmpty(auth)) {
            this.getAuthorization.saveAuthorization();
            auth = this.getAuth(dto.getUrl());
        }
        if (StringUtils.isEmpty(auth)) {
            return "{\"code\":500,\"data\":null,\"message\":\"Ragflow session authentication failed\"}";
        }
        String response = doRequest(base, dto.getUrl(), dto.getMethod(), dto.getParams(), auth);
        return handleResponse(response, base, dto.getUrl(), dto.getMethod(), dto.getParams());
    }
}
