package com.gaisoft.generator.controller;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.core.text.Convert;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.SecurityUtils;
import com.gaisoft.common.utils.sql.SqlUtil;
import com.gaisoft.generator.config.GenConfig;
import com.gaisoft.generator.domain.GenTable;
import com.gaisoft.generator.domain.GenTableColumn;
import com.gaisoft.generator.service.IGenTableColumnService;
import com.gaisoft.generator.service.IGenTableService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
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
@RequestMapping(value={"/tool/gen"})
public class GenController
extends BaseController {
    @Autowired
    private IGenTableService genTableService;
    @Autowired
    private IGenTableColumnService genTableColumnService;

    @PreAuthorize(value="@ss.hasPermi('tool:gen:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo genList(GenTable genTable) {
        this.startPage();
        List<GenTable> list = this.genTableService.selectGenTableList(genTable);
        return this.getDataTable(list);
    }

    @PreAuthorize(value="@ss.hasPermi('tool:gen:query')")
    @GetMapping(value={"/{tableId}"})
    public AjaxResult getInfo(@PathVariable Long tableId) {
        GenTable table = this.genTableService.selectGenTableById(tableId);
        List<GenTable> tables = this.genTableService.selectGenTableAll();
        List<GenTableColumn> list = this.genTableColumnService.selectGenTableColumnListByTableId(tableId);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("info", (Object)table);
        map.put("rows", list);
        map.put("tables", tables);
        return this.success(map);
    }

    @PreAuthorize(value="@ss.hasPermi('tool:gen:list')")
    @GetMapping(value={"/db/list"})
    public TableDataInfo dataList(GenTable genTable) {
        this.startPage();
        List<GenTable> list = this.genTableService.selectDbTableList(genTable);
        return this.getDataTable(list);
    }

    @PreAuthorize(value="@ss.hasPermi('tool:gen:list')")
    @GetMapping(value={"/column/{tableId}"})
    public TableDataInfo columnList(Long tableId) {
        TableDataInfo dataInfo = new TableDataInfo();
        List<GenTableColumn> list = this.genTableColumnService.selectGenTableColumnListByTableId(tableId);
        dataInfo.setRows(list);
        dataInfo.setTotal((long)list.size());
        return dataInfo;
    }

    @PreAuthorize(value="@ss.hasPermi('tool:gen:import')")
    @Log(title="代码生成", businessType=BusinessType.IMPORT)
    @PostMapping(value={"/importTable"})
    public AjaxResult importTableSave(String tables) {
        String[] tableNames = Convert.toStrArray((String)tables);
        List<GenTable> tableList = this.genTableService.selectDbTableListByNames(tableNames);
        this.genTableService.importGenTable(tableList, SecurityUtils.getUsername());
        return this.success();
    }

    @PreAuthorize(value="@ss.hasRole('admin')")
    @Log(title="创建表", businessType=BusinessType.OTHER)
    @PostMapping(value={"/createTable"})
    public AjaxResult createTableSave(String sql) {
        try {
            SqlUtil.filterKeyword((String)sql);
            @SuppressWarnings("unchecked") List<SQLStatement> sqlStatements = SQLUtils.parseStatements((String)sql, (DbType)DbType.mysql);
            ArrayList<String> tableNames = new ArrayList<String>();
            for (SQLStatement sqlStatement : sqlStatements) {
                MySqlCreateTableStatement createTableStatement;
                if (!(sqlStatement instanceof MySqlCreateTableStatement) || !this.genTableService.createTable((createTableStatement = (MySqlCreateTableStatement)sqlStatement).toString())) continue;
                String tableName = createTableStatement.getTableName().replaceAll("`", "");
                tableNames.add(tableName);
            }
            List<GenTable> tableList = this.genTableService.selectDbTableListByNames(tableNames.toArray(new String[tableNames.size()]));
            String operName = SecurityUtils.getUsername();
            this.genTableService.importGenTable(tableList, operName);
            return AjaxResult.success();
        }
        catch (Exception e) {
            this.logger.error(e.getMessage(), (Throwable)e);
            return AjaxResult.error((String)"创建表结构异常");
        }
    }

    @PreAuthorize(value="@ss.hasPermi('tool:gen:edit')")
    @Log(title="代码生成", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult editSave(@Validated @RequestBody GenTable genTable) {
        this.genTableService.validateEdit(genTable);
        this.genTableService.updateGenTable(genTable);
        return this.success();
    }

    @PreAuthorize(value="@ss.hasPermi('tool:gen:remove')")
    @Log(title="代码生成", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{tableIds}"})
    public AjaxResult remove(@PathVariable Long[] tableIds) {
        this.genTableService.deleteGenTableByIds(tableIds);
        return this.success();
    }

    @PreAuthorize(value="@ss.hasPermi('tool:gen:preview')")
    @GetMapping(value={"/preview/{tableId}"})
    public AjaxResult preview(@PathVariable(value="tableId") Long tableId) throws IOException {
        Map<String, String> dataMap = this.genTableService.previewCode(tableId);
        return this.success(dataMap);
    }

    @PreAuthorize(value="@ss.hasPermi('tool:gen:code')")
    @Log(title="代码生成", businessType=BusinessType.GENCODE)
    @GetMapping(value={"/download/{tableName}"})
    public void download(HttpServletResponse response, @PathVariable(value="tableName") String tableName) throws IOException {
        byte[] data = this.genTableService.downloadCode(tableName);
        this.genCode(response, data);
    }

    @PreAuthorize(value="@ss.hasPermi('tool:gen:code')")
    @Log(title="代码生成", businessType=BusinessType.GENCODE)
    @GetMapping(value={"/genCode/{tableName}"})
    public AjaxResult genCode(@PathVariable(value="tableName") String tableName) {
        if (!GenConfig.isAllowOverwrite()) {
            return AjaxResult.error((String)"【系统预设】不允许生成文件覆盖到本地");
        }
        this.genTableService.generatorCode(tableName);
        return this.success();
    }

    @PreAuthorize(value="@ss.hasPermi('tool:gen:edit')")
    @Log(title="代码生成", businessType=BusinessType.UPDATE)
    @GetMapping(value={"/synchDb/{tableName}"})
    public AjaxResult synchDb(@PathVariable(value="tableName") String tableName) {
        this.genTableService.synchDb(tableName);
        return this.success();
    }

    @PreAuthorize(value="@ss.hasPermi('tool:gen:code')")
    @Log(title="代码生成", businessType=BusinessType.GENCODE)
    @GetMapping(value={"/batchGenCode"})
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = Convert.toStrArray((String)tables);
        byte[] data = this.genTableService.downloadCode(tableNames);
        this.genCode(response, data);
    }

    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write((byte[])data, (OutputStream)response.getOutputStream());
    }
}
