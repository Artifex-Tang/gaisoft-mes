package com.gaisoft.generator.mapper;

import com.gaisoft.generator.domain.GenTableColumn;
import java.util.List;

public interface GenTableColumnMapper {
    public List<GenTableColumn> selectDbTableColumnsByName(String var1);

    public List<GenTableColumn> selectGenTableColumnListByTableId(Long var1);

    public int insertGenTableColumn(GenTableColumn var1);

    public int updateGenTableColumn(GenTableColumn var1);

    public int deleteGenTableColumns(List<GenTableColumn> var1);

    public int deleteGenTableColumnByIds(Long[] var1);
}
