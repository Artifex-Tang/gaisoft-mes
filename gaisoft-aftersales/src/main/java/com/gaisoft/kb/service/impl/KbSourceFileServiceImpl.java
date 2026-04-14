package com.gaisoft.kb.service.impl;

import com.gaisoft.common.utils.DateUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.kb.domain.KbSourceFile;
import com.gaisoft.kb.domain.KbSourceType;
import com.gaisoft.kb.mapper.KbSourceFileMapper;
import com.gaisoft.kb.service.IKbSourceFileService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KbSourceFileServiceImpl
implements IKbSourceFileService {
    @Autowired
    private KbSourceFileMapper kbSourceFileMapper;

    @Override
    public KbSourceFile selectKbSourceFileById(String id) {
        return this.kbSourceFileMapper.selectKbSourceFileById(id);
    }

    @Override
    public List<KbSourceFile> selectKbSourceFileList(KbSourceFile kbSourceFile) {
        return this.kbSourceFileMapper.selectKbSourceFileList(kbSourceFile);
    }

    @Override
    @Transactional
    public int insertKbSourceFile(KbSourceFile kbSourceFile) {
        kbSourceFile.setCreateTime(DateUtils.getNowDate());
        int rows = this.kbSourceFileMapper.insertKbSourceFile(kbSourceFile);
        return rows;
    }

    @Override
    @Transactional
    public int updateKbSourceFile(KbSourceFile kbSourceFile) {
        return this.kbSourceFileMapper.updateKbSourceFile(kbSourceFile);
    }

    @Override
    @Transactional
    public int deleteKbSourceFileByIds(String[] ids) {
        return this.kbSourceFileMapper.deleteKbSourceFileByIds(ids);
    }

    @Override
    @Transactional
    public int deleteKbSourceFileById(String id) {
        this.kbSourceFileMapper.deleteKbSourceTypeById(id);
        return this.kbSourceFileMapper.deleteKbSourceFileById(id);
    }

    @Override
    public List<KbSourceFile> selectKbSourceFileListByIds(List<String> ids) {
        ArrayList<KbSourceFile> list = new ArrayList<KbSourceFile>();
        for (String id : ids) {
            list.add(this.kbSourceFileMapper.selectKbSourceFileById(id));
        }
        return list;
    }

    public void insertKbSourceType(KbSourceFile kbSourceFile) {
        List<KbSourceType> kbSourceTypeList = kbSourceFile.getKbSourceTypeList();
        String id = kbSourceFile.getId();
        if (StringUtils.isNotNull(kbSourceTypeList)) {
            ArrayList<KbSourceType> list = new ArrayList<KbSourceType>();
            for (KbSourceType kbSourceType : kbSourceTypeList) {
                list.add(kbSourceType);
            }
            if (list.size() > 0) {
                this.kbSourceFileMapper.batchKbSourceType(list);
            }
        }
    }
}
