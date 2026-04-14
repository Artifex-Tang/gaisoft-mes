package com.gaisoft.web.controller.monitor;

import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.system.domain.SysCache;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/monitor/cache"})
public class CacheController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final List<SysCache> caches = new ArrayList<SysCache>();

    public CacheController() {
        caches.add(new SysCache("login_tokens:", "用户信息"));
        caches.add(new SysCache("sys_config:", "配置信息"));
        caches.add(new SysCache("sys_dict:", "数据字典"));
        caches.add(new SysCache("captcha_codes:", "验证码"));
        caches.add(new SysCache("repeat_submit:", "防重提交"));
        caches.add(new SysCache("rate_limit:", "限流处理"));
        caches.add(new SysCache("pwd_err_cnt:", "密码错误次数"));
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:cache:list')")
    @GetMapping
    public AjaxResult getInfo() throws Exception {
        Properties info = (Properties)this.redisTemplate.execute((RedisCallback<Properties>) connection -> connection.info());
        Properties commandStats = (Properties)this.redisTemplate.execute((RedisCallback<Properties>) connection -> connection.info("commandstats"));
        Object dbSize = this.redisTemplate.execute((RedisCallback<Long>) connection -> connection.dbSize());
        HashMap<String, Object> result = new HashMap<String, Object>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);
        ArrayList pieList = new ArrayList();
        commandStats.stringPropertyNames().forEach(key -> {
            HashMap<String, String> data = new HashMap<String, String>(2);
            String property = commandStats.getProperty((String)key);
            data.put("name", StringUtils.removeStart((String)key, (String)"cmdstat_"));
            data.put("value", StringUtils.substringBetween((String)property, (String)"calls=", (String)",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return AjaxResult.success(result);
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:cache:list')")
    @GetMapping(value={"/getNames"})
    public AjaxResult cache() {
        return AjaxResult.success(caches);
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:cache:list')")
    @GetMapping(value={"/getKeys/{cacheName}"})
    public AjaxResult getCacheKeys(@PathVariable String cacheName) {
        Set<String> cacheKeys = this.redisTemplate.keys(cacheName + "*");
        return AjaxResult.success(new TreeSet<>(cacheKeys));
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:cache:list')")
    @GetMapping(value={"/getValue/{cacheName}/{cacheKey}"})
    public AjaxResult getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey) {
        String cacheValue = (String)this.redisTemplate.opsForValue().get((Object)cacheKey);
        SysCache sysCache = new SysCache(cacheName, cacheKey, cacheValue);
        return AjaxResult.success((Object)sysCache);
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping(value={"/clearCacheName/{cacheName}"})
    public AjaxResult clearCacheName(@PathVariable String cacheName) {
        Set<String> cacheKeys = this.redisTemplate.keys(cacheName + "*");
        this.redisTemplate.delete((Collection<String>)cacheKeys);
        return AjaxResult.success();
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping(value={"/clearCacheKey/{cacheKey}"})
    public AjaxResult clearCacheKey(@PathVariable String cacheKey) {
        this.redisTemplate.delete(cacheKey);
        return AjaxResult.success();
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping(value={"/clearCacheAll"})
    public AjaxResult clearCacheAll() {
        Set<String> cacheKeys = this.redisTemplate.keys("*");
        this.redisTemplate.delete((Collection)cacheKeys);
        return AjaxResult.success();
    }
}
