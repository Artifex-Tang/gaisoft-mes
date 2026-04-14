package com.gaisoft.web.controller.system;

import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.domain.model.RegisterBody;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.framework.web.service.SysRegisterService;
import com.gaisoft.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysRegisterController
extends BaseController {
    @Autowired
    private SysRegisterService registerService;
    @Autowired
    private ISysConfigService configService;

    @PostMapping(value={"/register"})
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!"true".equals(this.configService.selectConfigByKey("sys.account.registerUser"))) {
            return this.error("当前系统没有开启注册功能！");
        }
        String msg = this.registerService.register(user);
        return StringUtils.isEmpty((String)msg) ? this.success() : this.error(msg);
    }
}
