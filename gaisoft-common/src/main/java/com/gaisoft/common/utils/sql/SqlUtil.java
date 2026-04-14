package com.gaisoft.common.utils.sql;

import com.gaisoft.common.exception.UtilException;
import com.gaisoft.common.utils.StringUtils;

public class SqlUtil {
    public static String SQL_REGEX = "|and |extractvalue|updatexml|sleep|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |or |union |like |+|/*|user()";
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";
    private static final int ORDER_BY_MAX_LENGTH = 500;

    public static String escapeOrderBySql(String value) {
        if (StringUtils.isNotEmpty(value) && !SqlUtil.isValidOrderBySql(value)) {
            throw new UtilException("参数不符合规范，不能进行查询");
        }
        if (StringUtils.length((CharSequence)value) > 500) {
            throw new UtilException("参数已超过最大限制，不能进行查询");
        }
        return value;
    }

    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

    public static void filterKeyword(String value) {
        String[] sqlKeywords;
        if (StringUtils.isEmpty(value)) {
            return;
        }
        for (String sqlKeyword : sqlKeywords = StringUtils.split((String)SQL_REGEX, (String)"\\|")) {
            if (StringUtils.indexOfIgnoreCase((CharSequence)value, (CharSequence)sqlKeyword) <= -1) continue;
            throw new UtilException("参数存在SQL注入风险");
        }
    }
}
