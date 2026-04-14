package com.gaisoft.kb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.http.HttpUtils;
import com.gaisoft.kb.controller.GetAuthorization;
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

    @PostMapping(value={"/common"})
    public String common(@RequestBody CommonDto dto) throws JsonProcessingException {
        String base = this.iSysConfigService.selectConfigByKey("RagFlowServerBaseUrl");
        HttpUtils httpUtils = new HttpUtils();
        String response = null;
        response = "get".equals(dto.getMethod()) ? (StringUtils.isNotEmpty((String)dto.getParams()) ? HttpUtils.sendGet((String)(base + dto.getUrl()), (String)dto.getParams(), (String)"application/json;charset=UTF-8", (String)this.getAuthorization.getAuthorization()) : HttpUtils.sendGet((String)(base + dto.getUrl()), (String)this.getAuthorization.getAuthorization())) : HttpUtils.sendPost((String)(base + dto.getUrl()), (String)dto.getParams(), (String)"application/json;charset=UTF-8", (String)dto.getMethod(), (String)this.getAuthorization.getAuthorization());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode1 = objectMapper.readTree(response);
        String code = String.valueOf(rootNode1.path("code"));
        if ("401".equals(code)) {
            this.getAuthorization.saveAuthorization();
            response = "get".equals(dto.getMethod()) ? (StringUtils.isNotEmpty((String)dto.getParams()) ? HttpUtils.sendGet((String)(base + dto.getUrl()), (String)dto.getParams(), (String)"application/json;charset=UTF-8", (String)this.getAuthorization.getAuthorization()) : HttpUtils.sendGet((String)(base + dto.getUrl()), (String)this.getAuthorization.getAuthorization())) : HttpUtils.sendPost((String)(base + dto.getUrl()), (String)dto.getParams(), (String)"application/json;charset=UTF-8", (String)dto.getMethod(), (String)this.getAuthorization.getAuthorization());
        }
        return response;
    }
}
