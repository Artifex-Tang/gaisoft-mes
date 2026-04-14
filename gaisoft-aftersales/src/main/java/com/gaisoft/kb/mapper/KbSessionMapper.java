package com.gaisoft.kb.mapper;

import com.gaisoft.kb.domain.KbSession;
import java.util.List;

public interface KbSessionMapper {
    public KbSession selectKbSessionById(Long var1);

    public List<KbSession> selectKbSessionList(KbSession var1);

    public int insertKbSession(KbSession var1);

    public int updateKbSession(KbSession var1);

    public int deleteKbSessionById(Long var1);

    public int deleteKbSessionByIds(Long[] var1);
}
