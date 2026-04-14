package com.gaisoft.web.controller.monitor;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.domain.model.LoginUser;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.core.redis.RedisCache;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.system.domain.SysUserOnline;
import com.gaisoft.system.service.ISysUserOnlineService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/monitor/online"})
public class SysUserOnlineController
extends BaseController {
    @Autowired
    private ISysUserOnlineService userOnlineService;
    @Autowired
    private RedisCache redisCache;

    @PreAuthorize(value="@ss.hasPermi('monitor:online:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(String ipaddr, String userName) {
        Collection<String> keys = this.redisCache.keys("login_tokens:*");
        ArrayList<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        for (String key : keys) {
            LoginUser user = (LoginUser)this.redisCache.getCacheObject(key);
            if (StringUtils.isNotEmpty((String)ipaddr) && StringUtils.isNotEmpty((String)userName)) {
                userOnlineList.add(this.userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
                continue;
            }
            if (StringUtils.isNotEmpty((String)ipaddr)) {
                userOnlineList.add(this.userOnlineService.selectOnlineByIpaddr(ipaddr, user));
                continue;
            }
            if (StringUtils.isNotEmpty((String)userName) && StringUtils.isNotNull((Object)user.getUser())) {
                userOnlineList.add(this.userOnlineService.selectOnlineByUserName(userName, user));
                continue;
            }
            userOnlineList.add(this.userOnlineService.loginUserToUserOnline(user));
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return this.getDataTable(userOnlineList);
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:online:forceLogout')")
    @Log(title="在线用户", businessType=BusinessType.FORCE)
    @DeleteMapping(value={"/{tokenId}"})
    public AjaxResult forceLogout(@PathVariable String tokenId) {
        this.redisCache.deleteObject("login_tokens:" + tokenId);
        return this.success();
    }
}
