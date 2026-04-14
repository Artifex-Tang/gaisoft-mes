package com.gaisoft.generator.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.gaisoft.common.utils.DateUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.generator.domain.GenTable;
import com.gaisoft.generator.domain.GenTableColumn;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.velocity.VelocityContext;

public class VelocityUtils {
    private static final String PROJECT_PATH = "main/java";
    private static final String MYBATIS_PATH = "main/resources/mapper";
    private static final String DEFAULT_PARENT_MENU_ID = "3";

    public static VelocityContext prepareContext(GenTable genTable) {
        String moduleName = genTable.getModuleName();
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        String tplCategory = genTable.getTplCategory();
        String functionName = genTable.getFunctionName();
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tplCategory", (Object)genTable.getTplCategory());
        velocityContext.put("tableName", (Object)genTable.getTableName());
        velocityContext.put("functionName", (Object)(StringUtils.isNotEmpty((String)functionName) ? functionName : "【请填写功能名称】"));
        velocityContext.put("ClassName", (Object)genTable.getClassName());
        velocityContext.put("className", (Object)StringUtils.uncapitalize((String)genTable.getClassName()));
        velocityContext.put("moduleName", (Object)genTable.getModuleName());
        velocityContext.put("BusinessName", (Object)StringUtils.capitalize((String)genTable.getBusinessName()));
        velocityContext.put("businessName", (Object)genTable.getBusinessName());
        velocityContext.put("basePackage", (Object)VelocityUtils.getPackagePrefix(packageName));
        velocityContext.put("packageName", (Object)packageName);
        velocityContext.put("author", (Object)genTable.getFunctionAuthor());
        velocityContext.put("datetime", (Object)DateUtils.getDate());
        velocityContext.put("pkColumn", (Object)genTable.getPkColumn());
        velocityContext.put("importList", VelocityUtils.getImportList(genTable));
        velocityContext.put("permissionPrefix", (Object)VelocityUtils.getPermissionPrefix(moduleName, businessName));
        velocityContext.put("columns", genTable.getColumns());
        velocityContext.put("table", (Object)genTable);
        velocityContext.put("dicts", (Object)VelocityUtils.getDicts(genTable));
        VelocityUtils.setMenuVelocityContext(velocityContext, genTable);
        if ("tree".equals(tplCategory)) {
            VelocityUtils.setTreeVelocityContext(velocityContext, genTable);
        }
        if ("sub".equals(tplCategory)) {
            VelocityUtils.setSubVelocityContext(velocityContext, genTable);
        }
        return velocityContext;
    }

    public static void setMenuVelocityContext(VelocityContext context, GenTable genTable) {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSON.parseObject((String)options);
        String parentMenuId = VelocityUtils.getParentMenuId(paramsObj);
        context.put("parentMenuId", (Object)parentMenuId);
    }

    public static void setTreeVelocityContext(VelocityContext context, GenTable genTable) {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSON.parseObject((String)options);
        String treeCode = VelocityUtils.getTreecode(paramsObj);
        String treeParentCode = VelocityUtils.getTreeParentCode(paramsObj);
        String treeName = VelocityUtils.getTreeName(paramsObj);
        context.put("treeCode", (Object)treeCode);
        context.put("treeParentCode", (Object)treeParentCode);
        context.put("treeName", (Object)treeName);
        context.put("expandColumn", (Object)VelocityUtils.getExpandColumn(genTable));
        if (paramsObj.containsKey("treeParentCode")) {
            context.put("tree_parent_code", (Object)paramsObj.getString("treeParentCode"));
        }
        if (paramsObj.containsKey("treeName")) {
            context.put("tree_name", (Object)paramsObj.getString("treeName"));
        }
    }

    public static void setSubVelocityContext(VelocityContext context, GenTable genTable) {
        GenTable subTable = genTable.getSubTable();
        String subTableName = genTable.getSubTableName();
        String subTableFkName = genTable.getSubTableFkName();
        String subClassName = genTable.getSubTable().getClassName();
        String subTableFkClassName = StringUtils.convertToCamelCase((String)subTableFkName);
        context.put("subTable", (Object)subTable);
        context.put("subTableName", (Object)subTableName);
        context.put("subTableFkName", (Object)subTableFkName);
        context.put("subTableFkClassName", (Object)subTableFkClassName);
        context.put("subTableFkclassName", (Object)StringUtils.uncapitalize((String)subTableFkClassName));
        context.put("subClassName", (Object)subClassName);
        context.put("subclassName", (Object)StringUtils.uncapitalize((String)subClassName));
        context.put("subImportList", VelocityUtils.getImportList(genTable.getSubTable()));
    }

    public static List<String> getTemplateList(String tplCategory, String tplWebType) {
        String useWebType = "vm/vue";
        if ("element-plus".equals(tplWebType)) {
            useWebType = "vm/vue/v3";
        }
        ArrayList<String> templates = new ArrayList<String>();
        templates.add("vm/java/domain.java.vm");
        templates.add("vm/java/mapper.java.vm");
        templates.add("vm/java/service.java.vm");
        templates.add("vm/java/serviceImpl.java.vm");
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/xml/mapper.xml.vm");
        templates.add("vm/sql/sql.vm");
        templates.add("vm/js/api.js.vm");
        if ("crud".equals(tplCategory)) {
            templates.add(useWebType + "/index.vue.vm");
        } else if ("tree".equals(tplCategory)) {
            templates.add(useWebType + "/index-tree.vue.vm");
        } else if ("sub".equals(tplCategory)) {
            templates.add(useWebType + "/index.vue.vm");
            templates.add("vm/java/sub-domain.java.vm");
        }
        return templates;
    }

    public static String getFileName(String template, GenTable genTable) {
        String fileName = "";
        String packageName = genTable.getPackageName();
        String moduleName = genTable.getModuleName();
        String className = genTable.getClassName();
        String businessName = genTable.getBusinessName();
        String javaPath = "main/java/" + StringUtils.replace((String)packageName, (String)".", (String)"/");
        String mybatisPath = "main/resources/mapper/" + moduleName;
        String vuePath = "vue";
        if (template.contains("domain.java.vm")) {
            fileName = StringUtils.format((String)"{}/domain/{}.java", (Object[])new Object[]{javaPath, className});
        }
        if (template.contains("sub-domain.java.vm") && StringUtils.equals((CharSequence)"sub", (CharSequence)genTable.getTplCategory())) {
            fileName = StringUtils.format((String)"{}/domain/{}.java", (Object[])new Object[]{javaPath, genTable.getSubTable().getClassName()});
        } else if (template.contains("mapper.java.vm")) {
            fileName = StringUtils.format((String)"{}/mapper/{}Mapper.java", (Object[])new Object[]{javaPath, className});
        } else if (template.contains("service.java.vm")) {
            fileName = StringUtils.format((String)"{}/service/I{}Service.java", (Object[])new Object[]{javaPath, className});
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = StringUtils.format((String)"{}/service/impl/{}ServiceImpl.java", (Object[])new Object[]{javaPath, className});
        } else if (template.contains("controller.java.vm")) {
            fileName = StringUtils.format((String)"{}/controller/{}Controller.java", (Object[])new Object[]{javaPath, className});
        } else if (template.contains("mapper.xml.vm")) {
            fileName = StringUtils.format((String)"{}/{}Mapper.xml", (Object[])new Object[]{mybatisPath, className});
        } else if (template.contains("sql.vm")) {
            fileName = businessName + "Menu.sql";
        } else if (template.contains("api.js.vm")) {
            fileName = StringUtils.format((String)"{}/api/{}/{}.js", (Object[])new Object[]{vuePath, moduleName, businessName});
        } else if (template.contains("index.vue.vm")) {
            fileName = StringUtils.format((String)"{}/views/{}/{}/index.vue", (Object[])new Object[]{vuePath, moduleName, businessName});
        } else if (template.contains("index-tree.vue.vm")) {
            fileName = StringUtils.format((String)"{}/views/{}/{}/index.vue", (Object[])new Object[]{vuePath, moduleName, businessName});
        }
        return fileName;
    }

    public static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        return StringUtils.substring((String)packageName, (int)0, (int)lastIndex);
    }

    public static HashSet<String> getImportList(GenTable genTable) {
        List<GenTableColumn> columns = genTable.getColumns();
        GenTable subGenTable = genTable.getSubTable();
        HashSet<String> importList = new HashSet<String>();
        if (StringUtils.isNotNull((Object)((Object)subGenTable))) {
            importList.add("java.util.List");
        }
        for (GenTableColumn column : columns) {
            if (!column.isSuperColumn() && "Date".equals(column.getJavaType())) {
                importList.add("java.util.Date");
                importList.add("com.fasterxml.jackson.annotation.JsonFormat");
                continue;
            }
            if (column.isSuperColumn() || !"BigDecimal".equals(column.getJavaType())) continue;
            importList.add("java.math.BigDecimal");
        }
        return importList;
    }

    public static String getDicts(GenTable genTable) {
        List<GenTableColumn> columns = genTable.getColumns();
        HashSet<String> dicts = new HashSet<String>();
        VelocityUtils.addDicts(dicts, columns);
        if (StringUtils.isNotNull((Object)((Object)genTable.getSubTable()))) {
            List<GenTableColumn> subColumns = genTable.getSubTable().getColumns();
            VelocityUtils.addDicts(dicts, subColumns);
        }
        return StringUtils.join(dicts, (String)", ");
    }

    public static void addDicts(Set<String> dicts, List<GenTableColumn> columns) {
        for (GenTableColumn column : columns) {
            if (column.isSuperColumn() || !StringUtils.isNotEmpty((String)column.getDictType()) || !StringUtils.equalsAny((CharSequence)column.getHtmlType(), (CharSequence[])new String[]{"select", "radio", "checkbox"})) continue;
            dicts.add("'" + column.getDictType() + "'");
        }
    }

    public static String getPermissionPrefix(String moduleName, String businessName) {
        return StringUtils.format((String)"{}:{}", (Object[])new Object[]{moduleName, businessName});
    }

    public static String getParentMenuId(JSONObject paramsObj) {
        if (StringUtils.isNotEmpty((Map)paramsObj) && paramsObj.containsKey("parentMenuId") && StringUtils.isNotEmpty((String)paramsObj.getString("parentMenuId"))) {
            return paramsObj.getString("parentMenuId");
        }
        return DEFAULT_PARENT_MENU_ID;
    }

    public static String getTreecode(JSONObject paramsObj) {
        if (paramsObj.containsKey("treeCode")) {
            return StringUtils.toCamelCase((String)paramsObj.getString("treeCode"));
        }
        return "";
    }

    public static String getTreeParentCode(JSONObject paramsObj) {
        if (paramsObj.containsKey("treeParentCode")) {
            return StringUtils.toCamelCase((String)paramsObj.getString("treeParentCode"));
        }
        return "";
    }

    public static String getTreeName(JSONObject paramsObj) {
        if (paramsObj.containsKey("treeName")) {
            return StringUtils.toCamelCase((String)paramsObj.getString("treeName"));
        }
        return "";
    }

    public static int getExpandColumn(GenTable genTable) {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSON.parseObject((String)options);
        String treeName = paramsObj.getString("treeName");
        int num = 0;
        for (GenTableColumn column : genTable.getColumns()) {
            if (!column.isList()) continue;
            ++num;
            String columnName = column.getColumnName();
            if (!columnName.equals(treeName)) continue;
            break;
        }
        return num;
    }
}
