package com.gaisoft.web.controller.monitor;

import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.framework.web.domain.Server;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/monitor/server"})
public class ServerController {
    @PreAuthorize(value="@ss.hasPermi('monitor:server:list')")
    @GetMapping
    public AjaxResult getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success((Object)server);
    }
}
