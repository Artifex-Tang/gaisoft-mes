package com.gaisoft.repair.service.impl;

import com.gaisoft.common.utils.DateUtils;
import com.gaisoft.repair.domain.RsRepairServiceProvider;
import com.gaisoft.repair.mapper.RsRepairServiceProviderMapper;
import com.gaisoft.repair.service.IRsRepairServiceProviderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RsRepairServiceProviderServiceImpl
implements IRsRepairServiceProviderService {
    @Autowired
    private RsRepairServiceProviderMapper rsRepairServiceProviderMapper;

    @Override
    public RsRepairServiceProvider selectRsRepairServiceProviderByProviderId(Long providerId) {
        return this.rsRepairServiceProviderMapper.selectRsRepairServiceProviderByProviderId(providerId);
    }

    @Override
    public List<RsRepairServiceProvider> selectRsRepairServiceProviderList(RsRepairServiceProvider rsRepairServiceProvider) {
        return this.rsRepairServiceProviderMapper.selectRsRepairServiceProviderList(rsRepairServiceProvider);
    }

    @Override
    public int insertRsRepairServiceProvider(RsRepairServiceProvider rsRepairServiceProvider) {
        rsRepairServiceProvider.setCreateTime(DateUtils.getNowDate());
        return this.rsRepairServiceProviderMapper.insertRsRepairServiceProvider(rsRepairServiceProvider);
    }

    @Override
    public int updateRsRepairServiceProvider(RsRepairServiceProvider rsRepairServiceProvider) {
        rsRepairServiceProvider.setUpdateTime(DateUtils.getNowDate());
        return this.rsRepairServiceProviderMapper.updateRsRepairServiceProvider(rsRepairServiceProvider);
    }

    @Override
    public int deleteRsRepairServiceProviderByProviderIds(Long[] providerIds) {
        return this.rsRepairServiceProviderMapper.deleteRsRepairServiceProviderByProviderIds(providerIds);
    }

    @Override
    public int deleteRsRepairServiceProviderByProviderId(Long providerId) {
        return this.rsRepairServiceProviderMapper.deleteRsRepairServiceProviderByProviderId(providerId);
    }
}
