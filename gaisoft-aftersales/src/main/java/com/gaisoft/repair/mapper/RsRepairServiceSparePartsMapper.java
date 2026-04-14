package com.gaisoft.repair.mapper;

import com.gaisoft.repair.domain.RsRepairServiceSpareParts;
import java.util.List;

public interface RsRepairServiceSparePartsMapper {
    public RsRepairServiceSpareParts selectRsRepairServiceSparePartsBySparePartId(Long var1);

    public List<RsRepairServiceSpareParts> selectRsRepairServiceSparePartsList(RsRepairServiceSpareParts var1);

    public int insertRsRepairServiceSpareParts(RsRepairServiceSpareParts var1);

    public int updateRsRepairServiceSpareParts(RsRepairServiceSpareParts var1);

    public int deleteRsRepairServiceSparePartsBySparePartId(Long var1);

    public int deleteRsRepairServiceSparePartsBySparePartIds(Long[] var1);
}
