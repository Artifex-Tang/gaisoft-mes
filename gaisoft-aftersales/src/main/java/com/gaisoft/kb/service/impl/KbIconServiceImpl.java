package com.gaisoft.kb.service.impl;

import com.gaisoft.kb.domain.KbIcon;
import com.gaisoft.kb.mapper.KbIconMapper;
import com.gaisoft.kb.service.IKbIconService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KbIconServiceImpl
implements IKbIconService {
    @Autowired
    private KbIconMapper kbIconMapper;

    @Override
    public KbIcon selectKbIconById(Long id) {
        return this.kbIconMapper.selectKbIconById(id);
    }

    @Override
    public List<KbIcon> selectKbIconList(KbIcon kbIcon) {
        return this.kbIconMapper.selectKbIconList(kbIcon);
    }

    @Override
    public int insertKbIcon(KbIcon kbIcon) {
        return this.kbIconMapper.insertKbIcon(kbIcon);
    }

    @Override
    public int updateKbIcon(KbIcon kbIcon) {
        return this.kbIconMapper.updateKbIcon(kbIcon);
    }

    @Override
    public int deleteKbIconByIds(Long[] ids) {
        return this.kbIconMapper.deleteKbIconByIds(ids);
    }

    @Override
    public int deleteKbIconById(Long id) {
        return this.kbIconMapper.deleteKbIconById(id);
    }
}
