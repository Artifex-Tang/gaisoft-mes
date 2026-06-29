package com.gaisoft.kb.controller;

import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.system.service.ISysConfigService;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value={"/proxy"})
public class StreamProxyController {
    @Autowired
    private ISysConfigService iSysConfigService;
    @Autowired
    private GetAuthorization getAuthorization;
    @Resource
    private WebClient webClient;

    /**
     * ragflow 0.18.0 dual auth:
     * /api/v1/* SDK endpoints → Bearer API key
     * /v1/* web UI endpoints → session token from /v1/user/login (Authorization response header)
     * (Legacy pure-API-key auth 401s on /v1/* — they require the login session token.)
     */
    private String getAuth(String url) {
        if (url != null && url.startsWith("/api/v1/")) {
            String apiKey = this.iSysConfigService.selectConfigByKey("RagFlowKey");
            if (StringUtils.isNotEmpty(apiKey)) {
                return "Bearer " + apiKey;
            }
        }
        // /v1/* web UI endpoints: session login token (cached 60s by GetAuthorization)
        String token = this.getAuthorization.getAuthorization();
        return StringUtils.isNotEmpty(token) ? token : "";
    }

    @PostMapping(path={"/stream"}, produces={"text/event-stream"})
    public Flux<String> streamProxy(@RequestBody Map<String, Object> params) {
        String base = this.iSysConfigService.selectConfigByKey("RagFlowServerBaseUrl");
        String url = params.get("url").toString();
        if (params.containsKey("url")) {
            params.remove("url");
        }
        return ((WebClient.RequestBodySpec)((WebClient.RequestBodySpec)this.webClient.post().uri(base + url, new Object[0])).header("Authorization", new String[]{getAuth(url)})).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(params)).retrieve().bodyToFlux(String.class).map(data -> data + "\n\n");
    }
}
