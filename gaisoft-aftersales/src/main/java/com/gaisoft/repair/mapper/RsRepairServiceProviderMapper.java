package com.gaisoft.repair.mapper;

import com.gaisoft.repair.domain.RsRepairServiceProvider;
import java.util.List;

public interface RsRepairServiceProviderMapper {
    public RsRepairServiceProvider selectRsRepairServiceProviderByProviderId(Long var1);

    public List<RsRepairServiceProvider> selectRsRepairServiceProviderList(RsRepairServiceProvider var1);

    public int insertRsRepairServiceProvider(RsRepairServiceProvider var1);

    public int updateRsRepairServiceProvider(RsRepairServiceProvider var1);

    public int deleteRsRepairServiceProviderByProviderId(Long var1);

    public int deleteRsRepairServiceProviderByProviderIds(Long[] var1);
}
