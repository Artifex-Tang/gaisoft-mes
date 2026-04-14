package com.gaisoft.web.controller.system;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.system.domain.SysConfig;
import com.gaisoft.system.service.ISysConfigService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/system/config"})
public class SysConfigController
extends BaseController {
    @Autowired
    private ISysConfigService configService;

    @PreAuthorize(value="@ss.hasPermi('system:config:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysConfig config) {
        this.startPage();
        List list = this.configService.selectConfigList(config);
        return this.getDataTable(list);
    }

    @Log(title="参数管理", businessType=BusinessType.EXPORT)
    @PreAuthorize(value="@ss.hasPermi('system:config:export')")
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, SysConfig config) {
        List list = this.configService.selectConfigList(config);
        ExcelUtil util = new ExcelUtil(SysConfig.class);
        util.exportExcel(response, list, "参数数据");
    }

    @PreAuthorize(value="@ss.hasPermi('system:config:query')")
    @GetMapping(value={"/{configId}"})
    public AjaxResult getInfo(@PathVariable Long configId) {
        return this.success(this.configService.selectConfigById(configId));
    }

    @GetMapping(value={"/configKey/{configKey}"})
    public AjaxResult getConfigKey(@PathVariable String configKey) {
        return this.success(this.configService.selectConfigByKey(configKey));
    }

    @PreAuthorize(value="@ss.hasPermi('system:config:add')")
    @Log(title="参数管理", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysConfig config) {
        if (!this.configService.checkConfigKeyUnique(config)) {
            return this.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(this.getUsername());
        return this.toAjax(this.configService.insertConfig(config));
    }

    @PreAuthorize(value="@ss.hasPermi('system:config:edit')")
    @Log(title="参数管理", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysConfig config) {
        if (!this.configService.checkConfigKeyUnique(config)) {
            return this.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(this.getUsername());
        return this.toAjax(this.configService.updateConfig(config));
    }

    @PreAuthorize(value="@ss.hasPermi('system:config:remove')")
    @Log(title="参数管理", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{configIds}"})
    public AjaxResult remove(@PathVariable Long[] configIds) {
        this.configService.deleteConfigByIds(configIds);
        return this.success();
    }

    @PreAuthorize(value="@ss.hasPermi('system:config:remove')")
    @Log(title="参数管理", businessType=BusinessType.CLEAN)
    @DeleteMapping(value={"/refreshCache"})
    public AjaxResult refreshCache() {
        this.configService.resetConfigCache();
        return this.success();
    }
}
