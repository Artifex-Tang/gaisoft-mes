package com.gaisoft.kb.service.impl;

import com.gaisoft.common.utils.DateUtils;
import com.gaisoft.kb.domain.KbSourceType;
import com.gaisoft.kb.mapper.KbSourceTypeMapper;
import com.gaisoft.kb.service.IKbSourceTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KbSourceTypeServiceImpl
implements IKbSourceTypeService {
    @Autowired
    private KbSourceTypeMapper kbSourceTypeMapper;

    @Override
    public KbSourceType selectKbSourceTypeById(Long id) {
        return this.kbSourceTypeMapper.selectKbSourceTypeById(id);
    }

    @Override
    public List<KbSourceType> selectKbSourceTypeList(KbSourceType kbSourceType) {
        return this.kbSourceTypeMapper.selectKbSourceTypeList(kbSourceType);
    }

    @Override
    public List<KbSourceType> selectKbSourceTypeListAndDept(KbSourceType kbSourceType) {
        return this.kbSourceTypeMapper.selectKbSourceTypeListAndDept(kbSourceType);
    }

    @Override
    public int insertKbSourceType(KbSourceType kbSourceType) {
        return this.kbSourceTypeMapper.insertKbSourceType(kbSourceType);
    }

    @Override
    public int updateKbSourceType(KbSourceType kbSourceType) {
        kbSourceType.setUpdateTime(DateUtils.getNowDate());
        return this.kbSourceTypeMapper.updateKbSourceType(kbSourceType);
    }

    @Override
    public int deleteKbSourceTypeByIds(Long[] ids) {
        return this.kbSourceTypeMapper.deleteKbSourceTypeByIds(ids);
    }

    @Override
    public int deleteKbSourceTypeById(Long id) {
        return this.kbSourceTypeMapper.deleteKbSourceTypeById(id);
    }
}
