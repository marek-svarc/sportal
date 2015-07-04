package com.clubeek.dao.impl.performance.test.indexes;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.clubeek.dao.impl.performance.model.MyEntry;

@Resource
public class IndexedTableDaoImpl implements IndexedTableDao {
    
    private NamedParameterJdbcTemplate template;

    @Autowired
    public void init(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<MyEntry> getAllMyEntries() {
        return template.query("select * from indexed_table", new MyEntryMapper());
    }

    @Override
    public MyEntry getMyEntryById(int id) {
        return template.getJdbcOperations().queryForObject("select * from indexed_table id = ?", new Integer[] {id}, new MyEntryMapper());
    }

    @Override
    public MyEntry getMyEntryByInt(int number) {
        return template.getJdbcOperations().queryForObject("select * from indexed_table where int_col = ?", new Integer[] {number}, new MyEntryMapper());
    }

    @Override
    public MyEntry getMyEntryByIntIndexed(int number) {
        return template.getJdbcOperations().queryForObject("select * from indexed_table where int_col_indexed = ?", new Integer[] {number}, new MyEntryMapper());
    }

    @Override
    public List<MyEntry> getMyEntriesByMyEnum(int myEnum) {
        return template.getJdbcOperations().query("select * from indexed_table where my_enum = ?", new Integer[] {myEnum}, new MyEntryMapper());
    }

    @Override
    public List<MyEntry> getMyEntriesByMyEnumIndexed(int myEnum) {
        return template.getJdbcOperations().query("select * from indexed_table where my_enum_indexed = ?", new Integer[] {myEnum}, new MyEntryMapper());
    }

    @Override
    public void deleteAllMyEntries() {
        template.getJdbcOperations().update("delete from indexed_table");        
    }

    @Override
    public void insertRow(MyEntry myEntry) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(myEntry);
        template.update("insert into indexed_table ("
                + "string_col, "
                + "string_col_indexed, "
                + "int_col, "
                + "int_col_indexed, "
                + "my_enum, "
                + "my_enum_indexed"
                + ")"
                + "values ("
                + ":stringCol, "
                + ":stringColIndexed, "
                + ":intCol, "
                + ":intColIndexed, "
                + ":myEnum, "
                + ":myEnumIndexed"
                + ")", source);        
    }
}
