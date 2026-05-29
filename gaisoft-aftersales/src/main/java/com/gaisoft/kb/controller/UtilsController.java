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
     * Choose auth based on ragflow API version:
     * /api/v1/* uses API key (Bearer token)
     * /v1/* (legacy) uses session cookie from GetAuthorization
     */
    private String getAuth(String url) {
        if (url != null && url.startsWith("/api/v1/")) {
            return "Bearer " + this.iSysConfigService.selectConfigByKey("RagFlowKey");
        }
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

    @PostMapping(value={"/common"})
    public String common(@RequestBody CommonDto dto) throws JsonProcessingException {
        String base = this.iSysConfigService.selectConfigByKey("RagFlowServerBaseUrl");
        String auth = this.getAuth(dto.getUrl());
        return doRequest(base, dto.getUrl(), dto.getMethod(), dto.getParams(), auth);
    }
}
