package com.gaisoft.common.utils.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelHandlerAdapter {
    public Object format(Object var1, String[] var2, Cell var3, Workbook var4);
}
