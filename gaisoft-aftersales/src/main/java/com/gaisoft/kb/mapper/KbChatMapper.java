package com.gaisoft.kb.mapper;

import com.gaisoft.kb.domain.KbChat;
import java.util.List;

public interface KbChatMapper {
    public KbChat selectKbChatById(Long var1);

    public List<KbChat> selectKbChatList(KbChat var1);

    public int insertKbChat(KbChat var1);

    public int updateKbChat(KbChat var1);

    public int deleteKbChatById(Long var1);

    public int deleteKbChatByIds(Long[] var1);
}
