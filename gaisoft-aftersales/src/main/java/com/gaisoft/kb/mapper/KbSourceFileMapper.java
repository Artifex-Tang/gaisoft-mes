package com.gaisoft.kb.mapper;

import com.gaisoft.kb.domain.KbSourceFile;
import com.gaisoft.kb.domain.KbSourceType;
import java.util.List;

public interface KbSourceFileMapper {
    public KbSourceFile selectKbSourceFileById(String var1);

    public List<KbSourceFile> selectKbSourceFileList(KbSourceFile var1);

    public int insertKbSourceFile(KbSourceFile var1);

    public int updateKbSourceFile(KbSourceFile var1);

    public int deleteKbSourceFileById(String var1);

    public int deleteKbSourceFileByIds(String[] var1);

    public int deleteKbSourceTypeByIds(String[] var1);

    public int batchKbSourceType(List<KbSourceType> var1);

    public int deleteKbSourceTypeById(String var1);
}
