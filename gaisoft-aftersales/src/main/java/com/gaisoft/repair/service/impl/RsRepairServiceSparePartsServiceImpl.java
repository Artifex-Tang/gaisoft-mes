package com.gaisoft.repair.service.impl;

import com.gaisoft.common.utils.DateUtils;
import com.gaisoft.repair.domain.RsRepairServiceSpareParts;
import com.gaisoft.repair.mapper.RsRepairServiceSparePartsMapper;
import com.gaisoft.repair.service.IRsRepairServiceSparePartsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RsRepairServiceSparePartsServiceImpl
implements IRsRepairServiceSparePartsService {
    @Autowired
    private RsRepairServiceSparePartsMapper rsRepairServiceSparePartsMapper;

    @Override
    public RsRepairServiceSpareParts selectRsRepairServiceSparePartsBySparePartId(Long sparePartId) {
        return this.rsRepairServiceSparePartsMapper.selectRsRepairServiceSparePartsBySparePartId(sparePartId);
    }

    @Override
    public List<RsRepairServiceSpareParts> selectRsRepairServiceSparePartsList(RsRepairServiceSpareParts rsRepairServiceSpareParts) {
        return this.rsRepairServiceSparePartsMapper.selectRsRepairServiceSparePartsList(rsRepairServiceSpareParts);
    }

    @Override
    public int insertRsRepairServiceSpareParts(RsRepairServiceSpareParts rsRepairServiceSpareParts) {
        rsRepairServiceSpareParts.setCreateTime(DateUtils.getNowDate());
        return this.rsRepairServiceSparePartsMapper.insertRsRepairServiceSpareParts(rsRepairServiceSpareParts);
    }

    @Override
    public int updateRsRepairServiceSpareParts(RsRepairServiceSpareParts rsRepairServiceSpareParts) {
        rsRepairServiceSpareParts.setUpdateTime(DateUtils.getNowDate());
        return this.rsRepairServiceSparePartsMapper.updateRsRepairServiceSpareParts(rsRepairServiceSpareParts);
    }

    @Override
    public int deleteRsRepairServiceSparePartsBySparePartIds(Long[] sparePartIds) {
        return this.rsRepairServiceSparePartsMapper.deleteRsRepairServiceSparePartsBySparePartIds(sparePartIds);
    }

    @Override
    public int deleteRsRepairServiceSparePartsBySparePartId(Long sparePartId) {
        return this.rsRepairServiceSparePartsMapper.deleteRsRepairServiceSparePartsBySparePartId(sparePartId);
    }
}
