package com.gaisoft.common.utils;

import com.gaisoft.common.core.page.PageDomain;
import com.gaisoft.common.core.page.TableSupport;
import com.gaisoft.common.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;

public class PageUtils
extends PageHelper {
    public static void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage((int)pageNum, (int)pageSize, (String)orderBy).setReasonable(reasonable);
    }

    public static void clearPage() {
        PageHelper.clearPage();
    }
}
