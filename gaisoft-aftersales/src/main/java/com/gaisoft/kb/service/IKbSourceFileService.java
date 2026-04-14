package com.gaisoft.kb.service;

import com.gaisoft.kb.domain.KbSourceFile;
import java.util.List;

public interface IKbSourceFileService {
    public KbSourceFile selectKbSourceFileById(String var1);

    public List<KbSourceFile> selectKbSourceFileList(KbSourceFile var1);

    public int insertKbSourceFile(KbSourceFile var1);

    public int updateKbSourceFile(KbSourceFile var1);

    public int deleteKbSourceFileByIds(String[] var1);

    public int deleteKbSourceFileById(String var1);

    public List<KbSourceFile> selectKbSourceFileListByIds(List<String> var1);
}
