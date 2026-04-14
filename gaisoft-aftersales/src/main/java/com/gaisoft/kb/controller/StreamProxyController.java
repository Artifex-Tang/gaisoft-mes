package com.gaisoft.kb.controller;

import com.gaisoft.kb.controller.GetAuthorization;
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
    @Resource
    private WebClient webClient;
    @Autowired
    GetAuthorization getAuthorization;

    @PostMapping(path={"/stream"}, produces={"text/event-stream"})
    public Flux<String> streamProxy(@RequestBody Map<String, Object> params) {
        String base = this.iSysConfigService.selectConfigByKey("RagFlowServerBaseUrl");
        String url = params.get("url").toString();
        if (params.containsKey("url")) {
            params.remove("url");
        }
        return ((WebClient.RequestBodySpec)((WebClient.RequestBodySpec)this.webClient.post().uri(base + url, new Object[0])).header("Authorization", new String[]{this.getAuthorization.getAuthorization()})).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(params)).retrieve().bodyToFlux(String.class).map(data -> data + "\n\n");
    }
}
