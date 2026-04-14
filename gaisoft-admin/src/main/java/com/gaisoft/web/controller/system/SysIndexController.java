package com.gaisoft.web.controller.system;

import com.gaisoft.common.config.RuoYiConfig;
import com.gaisoft.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysIndexController {
    @Autowired
    private RuoYiConfig ruoyiConfig;

    @RequestMapping(value={"/"})
    public String index() {
        return StringUtils.format((String)"欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", (Object[])new Object[]{this.ruoyiConfig.getName(), this.ruoyiConfig.getVersion()});
    }
}
