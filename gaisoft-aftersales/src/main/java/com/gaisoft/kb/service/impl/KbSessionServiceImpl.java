package com.gaisoft.kb.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaisoft.common.utils.DateUtils;
import com.gaisoft.common.utils.http.HttpUtils;
import com.gaisoft.kb.controller.GetAuthorization;
import com.gaisoft.kb.domain.KbChat;
import com.gaisoft.kb.domain.KbSession;
import com.gaisoft.kb.mapper.KbChatMapper;
import com.gaisoft.kb.mapper.KbSessionMapper;
import com.gaisoft.kb.service.IKbSessionService;
import com.gaisoft.system.service.ISysConfigService;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KbSessionServiceImpl
implements IKbSessionService {
    @Autowired
    private KbSessionMapper kbSessionMapper;
    @Autowired
    private KbChatMapper kbChatMapper;
    @Autowired
    private ISysConfigService iSysConfigService;
    @Autowired
    GetAuthorization getAuthorization;

    @Override
    public KbSession selectKbSessionById(Long id) {
        return this.kbSessionMapper.selectKbSessionById(id);
    }

    @Override
    public List<KbSession> selectKbSessionList(KbSession kbSession) {
        return this.kbSessionMapper.selectKbSessionList(kbSession);
    }

    @Override
    public KbSession insertKbSession(KbSession kbSession) throws JsonProcessingException {
        String base = this.iSysConfigService.selectConfigByKey("RagFlowServerBaseUrl");
        String url = base + "/v1/conversation/set";
        String apiKey = this.iSysConfigService.selectConfigByKey("RagFlowKey");
        String response = null;
        String code = null;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", kbSession.getSessionName());
        map.put("dialog_id", kbSession.getChatId());
        map.put("is_new", true);
        map.put("conversation_id", kbSession.getSessionId());
        ObjectMapper objectMapper = new ObjectMapper();
        String param = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        HttpUtils httpRagUtils = new HttpUtils();
        response = HttpUtils.sendPost((String)url, (String)param, (String)"application/json", (String)"POST", (String)this.getAuthorization.getAuthorization());
        JsonNode rootNode = objectMapper.readTree(response);
        code = String.valueOf(rootNode.path("code"));
        if (401 == Integer.parseInt(code)) {
            HttpUtils httpUtils = new HttpUtils();
            this.getAuthorization.saveAuthorization();
            response = HttpUtils.sendPost((String)url, (String)param, (String)"application/json", (String)"POST", (String)this.getAuthorization.getAuthorization());
            JsonNode rootNode1 = objectMapper.readTree(response);
            code = String.valueOf(rootNode1.path("code"));
        }
        if (0 == Integer.parseInt(code)) {
            kbSession.setCreateTime(DateUtils.getNowDate());
            String sessionId = rootNode.path("data").path("id").asText();
            kbSession.setSessionId(sessionId);
            kbSession.setCreateBy(kbSession.getCreateBy());
            this.kbSessionMapper.insertKbSession(kbSession);
            KbChat kbChat = new KbChat();
            kbChat.setChatId(kbSession.getChatId());
            kbChat.setSessionId(kbSession.getSessionId());
            kbChat.setRole("assistant");
            kbChat.setContent("你好！ 我是你的助理，有什么可以帮到你的吗？");
            kbChat.setCreateTime(DateUtils.getNowDate());
            kbChat.setCreateBy(kbSession.getCreateBy());
            this.kbChatMapper.insertKbChat(kbChat);
            return kbSession;
        }
        return null;
    }

    @Override
    public int updateKbSession(KbSession kbSession) {
        kbSession.setUpdateTime(DateUtils.getNowDate());
        return this.kbSessionMapper.updateKbSession(kbSession);
    }

    @Override
    public int deleteKbSessionByIds(Long[] ids) throws JsonProcessingException {
        return this.kbSessionMapper.deleteKbSessionByIds(ids);
    }

    @Override
    public int deleteKbSessionById(Long id) {
        return this.kbSessionMapper.deleteKbSessionById(id);
    }
}
