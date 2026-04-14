package com.gaisoft.kb.service.impl;

import com.gaisoft.common.utils.DateUtils;
import com.gaisoft.kb.domain.KbChat;
import com.gaisoft.kb.mapper.KbChatMapper;
import com.gaisoft.kb.service.IKbChatService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KbChatServiceImpl
implements IKbChatService {
    @Autowired
    private KbChatMapper kbChatMapper;

    @Override
    public KbChat selectKbChatById(Long id) {
        return this.kbChatMapper.selectKbChatById(id);
    }

    @Override
    public List<KbChat> selectKbChatList(KbChat kbChat) {
        return this.kbChatMapper.selectKbChatList(kbChat);
    }

    @Override
    public int insertKbChat(KbChat kbChat) {
        kbChat.setCreateTime(DateUtils.getNowDate());
        return this.kbChatMapper.insertKbChat(kbChat);
    }

    @Override
    public int updateKbChat(KbChat kbChat) {
        kbChat.setUpdateTime(DateUtils.getNowDate());
        return this.kbChatMapper.updateKbChat(kbChat);
    }

    @Override
    public int deleteKbChatByIds(Long[] ids) {
        return this.kbChatMapper.deleteKbChatByIds(ids);
    }

    @Override
    public int deleteKbChatById(Long id) {
        return this.kbChatMapper.deleteKbChatById(id);
    }
}
