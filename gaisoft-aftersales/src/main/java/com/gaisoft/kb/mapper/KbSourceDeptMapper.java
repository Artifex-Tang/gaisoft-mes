package com.gaisoft.kb.mapper;

import com.gaisoft.kb.domain.KbSourceDept;
import java.util.List;

public interface KbSourceDeptMapper {
    public KbSourceDept selectKbSourceDeptBySourceId(Long var1);

    public List<KbSourceDept> selectKbSourceDeptList(KbSourceDept var1);

    public int insertKbSourceDept(KbSourceDept var1);

    public int updateKbSourceDept(KbSourceDept var1);

    public int deleteKbSourceDeptBySourceId(Long var1);

    public int deleteKbSourceDeptByDeptId(Long var1);

    public int deleteKbSourceDeptBySourceIds(Long[] var1);
}
