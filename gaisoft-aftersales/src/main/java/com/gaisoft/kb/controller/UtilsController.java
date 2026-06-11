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
     * ragflow 0.18.0 dual auth:
     * /api/v1/* SDK endpoints → Bearer API key
     * /v1/* web UI endpoints → session JWT token from login
     */
    private String getAuth(String url) {
        if (url != null && url.startsWith("/api/v1/")) {
            String apiKey = this.iSysConfigService.selectConfigByKey("RagFlowKey");
            if (StringUtils.isNotEmpty(apiKey)) {
                return "Bearer " + apiKey;
            }
        }
        // Web UI endpoints (/v1/*): use session JWT token
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
     * For /v1/* web UI endpoints: re-login and retry with fresh session token.
     * Never return code 401 to frontend to avoid triggering user re-login dialog.
     */
    private String handleResponse(String response, String base, String url, String method, String params) {
        // HttpUtils returns "" (not null) on ConnectException — treat both as failure
        if (StringUtils.isEmpty(response)) {
            return "{\"code\":500,\"msg\":\"Ragflow服务无响应，请检查RagFlowServerBaseUrl配置及网络连通性\",\"data\":null}";
        }
        // Check for ragflow 401
        if (response.contains("\"code\":401") || response.contains("\"code\": 401")) {
            // For /api/v1/* SDK endpoints, API key is static - retry won't help
            if (url != null && url.startsWith("/api/v1/")) {
                return "{\"code\":500,\"msg\":\"Ragflow API Key认证失败，请检查sys_config中RagFlowKey是否为当前Ragflow实例生成的有效Key\",\"data\":null}";
            }
            // For /v1/* web UI endpoints: re-login to get fresh session token
            this.getAuthorization.saveAuthorization();
            String newSessionToken = this.getAuthorization.getAuthorization();
            if (StringUtils.isEmpty(newSessionToken)) {
                return "{\"code\":500,\"msg\":\"Ragflow会话重新登录失败，请检查sys_config中email/password配置\",\"data\":null}";
            }
            String retry = doRequest(base, url, method, params, newSessionToken);
            if (retry != null && !retry.contains("\"code\":401") && !retry.contains("\"code\": 401")) {
                return retry;
            }
            // Retry also failed - return non-401 error
            return "{\"code\":500,\"msg\":\"Ragflow认证重试后仍失败，请检查Ragflow服务状态及认证配置\",\"data\":null}";
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
            return "{\"code\":500,\"msg\":\"Ragflow会话认证失败，请检查sys_config中RagFlowServerBaseUrl/email/password配置\",\"data\":null}";
        }
        String response = doRequest(base, dto.getUrl(), dto.getMethod(), dto.getParams(), auth);
        return handleResponse(response, base, dto.getUrl(), dto.getMethod(), dto.getParams());
    }
}
