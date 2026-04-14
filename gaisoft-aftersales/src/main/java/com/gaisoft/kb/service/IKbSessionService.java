package com.gaisoft.kb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaisoft.kb.domain.KbSession;
import java.util.List;

public interface IKbSessionService {
    public KbSession selectKbSessionById(Long var1);

    public List<KbSession> selectKbSessionList(KbSession var1);

    public KbSession insertKbSession(KbSession var1) throws JsonProcessingException;

    public int updateKbSession(KbSession var1);

    public int deleteKbSessionByIds(Long[] var1) throws JsonProcessingException;

    public int deleteKbSessionById(Long var1);
}
