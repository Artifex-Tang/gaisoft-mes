package com.gaisoft.system.service.impl;

import com.gaisoft.common.core.domain.entity.SysDictData;
import com.gaisoft.common.core.domain.entity.SysDictType;
import com.gaisoft.common.exception.ServiceException;
import com.gaisoft.common.utils.DictUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.system.mapper.SysDictDataMapper;
import com.gaisoft.system.mapper.SysDictTypeMapper;
import com.gaisoft.system.service.ISysDictTypeService;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysDictTypeServiceImpl
implements ISysDictTypeService {
    @Autowired
    private SysDictTypeMapper dictTypeMapper;
    @Autowired
    private SysDictDataMapper dictDataMapper;

    @PostConstruct
    public void init() {
        this.loadingDictCache();
    }

    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        return this.dictTypeMapper.selectDictTypeList(dictType);
    }

    @Override
    public List<SysDictType> selectDictTypeAll() {
        return this.dictTypeMapper.selectDictTypeAll();
    }

    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = DictUtils.getDictCache((String)dictType);
        if (StringUtils.isNotEmpty((Collection)dictDatas)) {
            return dictDatas;
        }
        dictDatas = this.dictDataMapper.selectDictDataByType(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache((String)dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return this.dictTypeMapper.selectDictTypeById(dictId);
    }

    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        return this.dictTypeMapper.selectDictTypeByType(dictType);
    }

    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            SysDictType dictType = this.selectDictTypeById(dictId);
            if (this.dictDataMapper.countDictDataByType(dictType.getDictType()) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            this.dictTypeMapper.deleteDictTypeById(dictId);
            DictUtils.removeDictCache((String)dictType.getDictType());
        }
    }

    @Override
    public void loadingDictCache() {
        SysDictData dictData = new SysDictData();
        dictData.setStatus("0");
        Map<String, List<SysDictData>> dictDataMap = this.dictDataMapper.selectDictDataList(dictData).stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache((String)entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
        }
    }

    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    @Override
    public void resetDictCache() {
        this.clearDictCache();
        this.loadingDictCache();
    }

    @Override
    public int insertDictType(SysDictType dict) {
        int row = this.dictTypeMapper.insertDictType(dict);
        if (row > 0) {
            DictUtils.setDictCache((String)dict.getDictType(), null);
        }
        return row;
    }

    @Override
    @Transactional
    public int updateDictType(SysDictType dict) {
        SysDictType oldDict = this.dictTypeMapper.selectDictTypeById(dict.getDictId());
        this.dictDataMapper.updateDictDataType(oldDict.getDictType(), dict.getDictType());
        int row = this.dictTypeMapper.updateDictType(dict);
        if (row > 0) {
            List<SysDictData> dictDatas = this.dictDataMapper.selectDictDataByType(dict.getDictType());
            DictUtils.setDictCache((String)dict.getDictType(), dictDatas);
        }
        return row;
    }

    @Override
    public boolean checkDictTypeUnique(SysDictType dict) {
        Long dictId = StringUtils.isNull((Object)dict.getDictId()) ? -1L : dict.getDictId();
        SysDictType dictType = this.dictTypeMapper.checkDictTypeUnique(dict.getDictType());
        return !StringUtils.isNotNull((Object)dictType) || dictType.getDictId().longValue() == dictId.longValue();
    }
}
