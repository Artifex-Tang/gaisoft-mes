package com.gaisoft.generator.service;

import com.gaisoft.generator.domain.GenTable;
import java.util.List;
import java.util.Map;

public interface IGenTableService {
    public List<GenTable> selectGenTableList(GenTable var1);

    public List<GenTable> selectDbTableList(GenTable var1);

    public List<GenTable> selectDbTableListByNames(String[] var1);

    public List<GenTable> selectGenTableAll();

    public GenTable selectGenTableById(Long var1);

    public void updateGenTable(GenTable var1);

    public void deleteGenTableByIds(Long[] var1);

    public boolean createTable(String var1);

    public void importGenTable(List<GenTable> var1, String var2);

    public Map<String, String> previewCode(Long var1);

    public byte[] downloadCode(String var1);

    public void generatorCode(String var1);

    public void synchDb(String var1);

    public byte[] downloadCode(String[] var1);

    public void validateEdit(GenTable var1);
}
