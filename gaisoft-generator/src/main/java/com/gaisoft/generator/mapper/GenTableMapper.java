package com.gaisoft.generator.mapper;

import com.gaisoft.generator.domain.GenTable;
import java.util.List;

public interface GenTableMapper {
    public List<GenTable> selectGenTableList(GenTable var1);

    public List<GenTable> selectDbTableList(GenTable var1);

    public List<GenTable> selectDbTableListByNames(String[] var1);

    public List<GenTable> selectGenTableAll();

    public GenTable selectGenTableById(Long var1);

    public GenTable selectGenTableByName(String var1);

    public int insertGenTable(GenTable var1);

    public int updateGenTable(GenTable var1);

    public int deleteGenTableByIds(Long[] var1);

    public int createTable(String var1);
}
