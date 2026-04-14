package com.gaisoft.kb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaisoft.common.utils.http.HttpUtils;
import com.gaisoft.system.service.ISysConfigService;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAuthorization {
    private static final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap();
    private final long CACHE_EXPIRE_TIME = 60000L;
    @Autowired
    private ISysConfigService sysConfigService;

    private boolean isExpired(CacheEntry entry) {
        long entryTime;
        long currentTime = System.currentTimeMillis();
        return currentTime - (entryTime = entry.getTimestamp().getTime()) > 60000L * (long)Integer.parseInt(this.sysConfigService.selectConfigByKey("CACHE_EXPIRE_TIME"));
    }

    public String getAuthorization() {
        try {
            CacheEntry entry = cache.get("authorization1");
            if (entry != null && !this.isExpired(entry)) {
                System.out.println("使用缓存的 Authorization 头字段");
                System.out.println("reponse" + entry.getValue());
                return entry.getValue();
            }
            HttpUtils httpUtils = new HttpUtils();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("email", this.sysConfigService.selectConfigByKey("email"));
            map.put("password", this.sysConfigService.selectConfigByKey("password"));
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(map);
            String reponse = HttpUtils.sendPost1((String)(this.sysConfigService.selectConfigByKey("RagFlowServerBaseUrl") + "/v1/user/login"), (String)json, (String)"application/json");
            if (reponse != null) {
                cache.put("authorization1", new CacheEntry(reponse, new Date()));
            }
            System.out.println("reponsereponse");
            return reponse;
        }
        catch (Exception e) {
            System.err.println("获取 Authorization 头字段时出错: " + e.getMessage());
            return null;
        }
    }

    public String saveAuthorization() {
        HttpUtils httpUtils = new HttpUtils();
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("email", this.sysConfigService.selectConfigByKey("email"));
            map.put("password", this.sysConfigService.selectConfigByKey("password"));
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(map);
            String reponse = HttpUtils.sendPost1((String)(this.sysConfigService.selectConfigByKey("RagFlowServerBaseUrl") + "/v1/user/login"), (String)json, (String)"application/json");
            if (reponse != null) {
                cache.put("authorization1", new CacheEntry(reponse, new Date()));
            }
            System.out.println("reponse" + reponse);
            return reponse;
        }
        catch (Exception e) {
            System.err.println("获取 Authorization 头字段时出错: " + e.getMessage());
            return null;
        }
    }

    private class CacheEntry {
        private final String value;
        private final Date timestamp;

        public CacheEntry(String value, Date timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }

        public String getValue() {
            return this.value;
        }

        public Date getTimestamp() {
            return this.timestamp;
        }
    }
}
