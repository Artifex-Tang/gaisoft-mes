package com.gaisoft.kb.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaisoft.common.utils.StringUtils;

public class CommonDto {
    private String url;
    private String params;
    private String method;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        if (StringUtils.isNotEmpty((String)this.params)) {
            String unescapedJson = null;
            try {
                ObjectMapper mapper = new ObjectMapper();
                Object jsonObject = mapper.readValue(this.params, Object.class);
                unescapedJson = mapper.writeValueAsString(jsonObject);
            }
            catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            System.out.println("unescapedJson========================" + unescapedJson);
            return unescapedJson;
        }
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
