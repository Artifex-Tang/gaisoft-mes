package com.gaisoft.system.service.impl;

import com.gaisoft.common.annotation.DataSource;
import com.gaisoft.common.core.redis.RedisCache;
import com.gaisoft.common.core.text.Convert;
import com.gaisoft.common.enums.DataSourceType;
import com.gaisoft.common.exception.ServiceException;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.system.domain.SysConfig;
import com.gaisoft.system.mapper.SysConfigMapper;
import com.gaisoft.system.service.ISysConfigService;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl
implements ISysConfigService {
    @Autowired
    private SysConfigMapper configMapper;
    @Autowired
    private RedisCache redisCache;

    @PostConstruct
    public void init() {
        this.loadingConfigCache();
    }

    @Override
    @DataSource(value=DataSourceType.MASTER)
    public SysConfig selectConfigById(Long configId) {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return this.configMapper.selectConfig(config);
    }

    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr((Object)this.redisCache.getCacheObject(this.getCacheKey(configKey)));
        if (StringUtils.isNotEmpty((String)configValue)) {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = this.configMapper.selectConfig(config);
        if (StringUtils.isNotNull((Object)((Object)retConfig))) {
            this.redisCache.setCacheObject(this.getCacheKey(configKey), (Object)retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return "";
    }

    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = this.selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty((String)captchaEnabled)) {
            return true;
        }
        return Convert.toBool((Object)captchaEnabled);
    }

    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        return this.configMapper.selectConfigList(config);
    }

    @Override
    public int insertConfig(SysConfig config) {
        int row = this.configMapper.insertConfig(config);
        if (row > 0) {
            this.redisCache.setCacheObject(this.getCacheKey(config.getConfigKey()), (Object)config.getConfigValue());
        }
        return row;
    }

    @Override
    public int updateConfig(SysConfig config) {
        int row;
        SysConfig temp = this.configMapper.selectConfigById(config.getConfigId());
        if (!StringUtils.equals((CharSequence)temp.getConfigKey(), (CharSequence)config.getConfigKey())) {
            this.redisCache.deleteObject(this.getCacheKey(temp.getConfigKey()));
        }
        if ((row = this.configMapper.updateConfig(config)) > 0) {
            this.redisCache.setCacheObject(this.getCacheKey(config.getConfigKey()), (Object)config.getConfigValue());
        }
        return row;
    }

    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = this.selectConfigById(configId);
            if (StringUtils.equals((CharSequence)"Y", (CharSequence)config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            this.configMapper.deleteConfigById(configId);
            this.redisCache.deleteObject(this.getCacheKey(config.getConfigKey()));
        }
    }

    @Override
    public void loadingConfigCache() {
        List<SysConfig> configsList = this.configMapper.selectConfigList(new SysConfig());
        for (SysConfig config : configsList) {
            this.redisCache.setCacheObject(this.getCacheKey(config.getConfigKey()), (Object)config.getConfigValue());
        }
    }

    @Override
    public void clearConfigCache() {
        Collection keys = this.redisCache.keys("sys_config:*");
        this.redisCache.deleteObject(keys);
    }

    @Override
    public void resetConfigCache() {
        this.clearConfigCache();
        this.loadingConfigCache();
    }

    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        Long configId = StringUtils.isNull((Object)config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = this.configMapper.checkConfigKeyUnique(config.getConfigKey());
        return !StringUtils.isNotNull((Object)((Object)info)) || info.getConfigId().longValue() == configId.longValue();
    }

    private String getCacheKey(String configKey) {
        return "sys_config:" + configKey;
    }
}
