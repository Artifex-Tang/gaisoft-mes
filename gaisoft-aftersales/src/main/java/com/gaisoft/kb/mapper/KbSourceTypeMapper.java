package com.gaisoft.kb.mapper;

import com.gaisoft.kb.domain.KbSourceType;
import java.util.List;

public interface KbSourceTypeMapper {
    public KbSourceType selectKbSourceTypeById(Long var1);

    public List<KbSourceType> selectKbSourceTypeList(KbSourceType var1);

    public List<KbSourceType> selectKbSourceTypeListAndDept(KbSourceType var1);

    public int insertKbSourceType(KbSourceType var1);

    public int updateKbSourceType(KbSourceType var1);

    public int deleteKbSourceTypeById(Long var1);

    public int deleteKbSourceTypeByIds(Long[] var1);
}
