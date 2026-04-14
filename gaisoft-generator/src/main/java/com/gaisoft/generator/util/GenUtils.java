package com.gaisoft.generator.util;

import com.gaisoft.common.constant.GenConstants;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.generator.config.GenConfig;
import com.gaisoft.generator.domain.GenTable;
import com.gaisoft.generator.domain.GenTableColumn;
import java.util.Arrays;
import org.apache.commons.lang3.RegExUtils;

public class GenUtils {
    public static void initTable(GenTable genTable, String operName) {
        genTable.setClassName(GenUtils.convertClassName(genTable.getTableName()));
        genTable.setPackageName(GenConfig.getPackageName());
        genTable.setModuleName(GenUtils.getModuleName(GenConfig.getPackageName()));
        genTable.setBusinessName(GenUtils.getBusinessName(genTable.getTableName()));
        genTable.setFunctionName(GenUtils.replaceText(genTable.getTableComment()));
        genTable.setFunctionAuthor(GenConfig.getAuthor());
        genTable.setCreateBy(operName);
    }

    public static void initColumnField(GenTableColumn column, GenTable table) {
        String dataType = GenUtils.getDbType(column.getColumnType());
        String columnName = column.getColumnName();
        column.setTableId(table.getTableId());
        column.setCreateBy(table.getCreateBy());
        column.setJavaField(StringUtils.toCamelCase((String)columnName));
        column.setJavaType("String");
        column.setQueryType("EQ");
        if (GenUtils.arraysContains(GenConstants.COLUMNTYPE_STR, dataType) || GenUtils.arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType)) {
            Integer columnLength = GenUtils.getColumnLength(column.getColumnType());
            String htmlType = columnLength >= 500 || GenUtils.arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType) ? "textarea" : "input";
            column.setHtmlType(htmlType);
        } else if (GenUtils.arraysContains(GenConstants.COLUMNTYPE_TIME, dataType)) {
            column.setJavaType("Date");
            column.setHtmlType("datetime");
        } else if (GenUtils.arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType)) {
            column.setHtmlType("input");
            String[] str = StringUtils.split((String)StringUtils.substringBetween((String)column.getColumnType(), (String)"(", (String)")"), (String)",");
            if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
                column.setJavaType("BigDecimal");
            } else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
                column.setJavaType("Integer");
            } else {
                column.setJavaType("Long");
            }
        }
        column.setIsInsert("1");
        if (!GenUtils.arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName) && !column.isPk()) {
            column.setIsEdit("1");
        }
        if (!GenUtils.arraysContains(GenConstants.COLUMNNAME_NOT_LIST, columnName) && !column.isPk()) {
            column.setIsList("1");
        }
        if (!GenUtils.arraysContains(GenConstants.COLUMNNAME_NOT_QUERY, columnName) && !column.isPk()) {
            column.setIsQuery("1");
        }
        if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"name")) {
            column.setQueryType("LIKE");
        }
        if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"status")) {
            column.setHtmlType("radio");
        } else if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"type") || StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"sex")) {
            column.setHtmlType("select");
        } else if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"image")) {
            column.setHtmlType("imageUpload");
        } else if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"file")) {
            column.setHtmlType("fileUpload");
        } else if (StringUtils.endsWithIgnoreCase((CharSequence)columnName, (CharSequence)"content")) {
            column.setHtmlType("editor");
        }
    }

    public static boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        int nameLength = packageName.length();
        return StringUtils.substring((String)packageName, (int)(lastIndex + 1), (int)nameLength);
    }

    public static String getBusinessName(String tableName) {
        int lastIndex = tableName.lastIndexOf("_");
        int nameLength = tableName.length();
        return StringUtils.substring((String)tableName, (int)(lastIndex + 1), (int)nameLength);
    }

    public static String convertClassName(String tableName) {
        boolean autoRemovePre = GenConfig.getAutoRemovePre();
        String tablePrefix = GenConfig.getTablePrefix();
        if (autoRemovePre && StringUtils.isNotEmpty((String)tablePrefix)) {
            String[] searchList = StringUtils.split((String)tablePrefix, (String)",");
            tableName = GenUtils.replaceFirst(tableName, searchList);
        }
        return StringUtils.convertToCamelCase((String)tableName);
    }

    public static String replaceFirst(String replacementm, String[] searchList) {
        String text = replacementm;
        for (String searchString : searchList) {
            if (!replacementm.startsWith(searchString)) continue;
            text = replacementm.replaceFirst(searchString, "");
            break;
        }
        return text;
    }

    public static String replaceText(String text) {
        return RegExUtils.replaceAll((String)text, (String)"(?:表|若依)", (String)"");
    }

    public static String getDbType(String columnType) {
        if (StringUtils.indexOf((CharSequence)columnType, (CharSequence)"(") > 0) {
            return StringUtils.substringBefore((String)columnType, (String)"(");
        }
        return columnType;
    }

    public static Integer getColumnLength(String columnType) {
        if (StringUtils.indexOf((CharSequence)columnType, (CharSequence)"(") > 0) {
            String length = StringUtils.substringBetween((String)columnType, (String)"(", (String)")");
            return Integer.valueOf(length);
        }
        return 0;
    }
}
