package com.gaisoft.kb.service.impl;

import com.gaisoft.kb.domain.KbSourceDept;
import com.gaisoft.kb.mapper.KbSourceDeptMapper;
import com.gaisoft.kb.service.IKbSourceDeptService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KbSourceDeptServiceImpl
implements IKbSourceDeptService {
    @Autowired
    private KbSourceDeptMapper kbSourceDeptMapper;

    @Override
    public KbSourceDept selectKbSourceDeptBySourceId(Long sourceId) {
        return this.kbSourceDeptMapper.selectKbSourceDeptBySourceId(sourceId);
    }

    @Override
    public List<KbSourceDept> selectKbSourceDeptList(KbSourceDept kbSourceDept) {
        return this.kbSourceDeptMapper.selectKbSourceDeptList(kbSourceDept);
    }

    @Override
    public int insertKbSourceDept(KbSourceDept kbSourceDept) {
        Integer i = 0;
        this.kbSourceDeptMapper.deleteKbSourceDeptByDeptId(kbSourceDept.getDeptId());
        for (Long sourceId : kbSourceDept.getSourceIds()) {
            kbSourceDept.setSourceId(sourceId);
            kbSourceDept.setAncestors(kbSourceDept.getAncestorss().get(i));
            this.kbSourceDeptMapper.insertKbSourceDept(kbSourceDept);
            Integer n = i;
            i = i + 1;
        }
        return i;
    }

    @Override
    public int updateKbSourceDept(KbSourceDept kbSourceDept) {
        return this.kbSourceDeptMapper.updateKbSourceDept(kbSourceDept);
    }

    @Override
    public int deleteKbSourceDeptBySourceIds(Long[] sourceIds) {
        return this.kbSourceDeptMapper.deleteKbSourceDeptBySourceIds(sourceIds);
    }

    @Override
    public int deleteKbSourceDeptBySourceId(Long sourceId) {
        return this.kbSourceDeptMapper.deleteKbSourceDeptBySourceId(sourceId);
    }

    @Override
    public int deleteKbSourceDeptByDeptId(Long deptId) {
        return this.kbSourceDeptMapper.deleteKbSourceDeptByDeptId(deptId);
    }
}
