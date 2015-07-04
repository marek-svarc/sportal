package com.clubeek.dao.impl.performance.test.largetables;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.clubeek.dao.impl.performance.model.LargeTableRow;

public class LargeTableDaoImpl implements LargeTableDao {
    
    private NamedParameterJdbcTemplate template;

    @Autowired
    public void init(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public String getOneColumn(int id) {
        return template.getJdbcOperations().queryForObject("select str_col_1 from test_large_table where id = ?", new Object[] {id}, String.class);
    }

    @Override
    public LargeTableRow getAllColumns(int id) {
        return template.getJdbcOperations().queryForObject("select * from test_large_table where id = ?", new Object[] {id}, new LargeTableMapper());
    }

    @Override
    public List<String> getListOfOneColumns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<LargeTableRow> getListOfAllColumns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void insertRow(LargeTableRow ltr) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(ltr);
        template.update("insert into test_large_table "
                + "(str_col_1,"
                + "int_col_1, "
                + "str_col_2, "
                + "int_col_2, "
                + "str_col_3, "
                + "int_col_3, "
                + "str_col_4, "
                + "int_col_4, "
                + "str_col_5, "
                + "int_col_5, "
                + "str_col_6, "
                + "int_col_6, "
                + "str_col_7, "
                + "int_col_7, "
                + "str_col_8, "
                + "int_col_8, "
                + "str_col_9, "
                + "int_col_9, "
                + "str_col_10, "
                + "int_col_10) values "
                + "(:strCol1, "
                + ":intCol1, "
                + ":strCol2, "
                + ":intCol2, "
                + ":strCol3, "
                + ":intCol3, "
                + ":strCol4, "
                + ":intCol4, "
                + ":strCol5, "
                + ":intCol5, "
                + ":strCol6, "
                + ":intCol6, "
                + ":strCol7, "
                + ":intCol7, "
                + ":strCol8, "
                + ":intCol8, "
                + ":strCol9, "
                + ":intCol9, "
                + ":strCol10, "
                + ":intCol10)"
                + "", source);        
    }

    @Override
    public void insertList(List<LargeTableRow> list) {
        SqlParameterSource[] sources = new SqlParameterSourceUtils().createBatch(list.toArray());
        template.batchUpdate("insert into test_large_table "
                + "(str_col_1,"
                + "int_col_1, "
                + "str_col_2, "
                + "int_col_2, "
                + "str_col_3, "
                + "int_col_3, "
                + "str_col_4, "
                + "int_col_4, "
                + "str_col_5, "
                + "int_col_5, "
                + "str_col_6, "
                + "int_col_6, "
                + "str_col_7, "
                + "int_col_7, "
                + "str_col_8, "
                + "int_col_8, "
                + "str_col_9, "
                + "int_col_9, "
                + "str_col_10, "
                + "int_col_10) values "
                + "(:strCol1, "
                + ":intCol1, "
                + ":strCol2, "
                + ":intCol2, "
                + ":strCol3, "
                + ":intCol3, "
                + ":strCol4, "
                + ":intCol4, "
                + ":strCol5, "
                + ":intCol5, "
                + ":strCol6, "
                + ":intCol6, "
                + ":strCol7, "
                + ":intCol7, "
                + ":strCol8, "
                + ":intCol8, "
                + ":strCol9, "
                + ":intCol9, "
                + ":strCol10, "
                + ":intCol10)", sources);
        
    }

    @Override
    public void deleteRow(LargeTableRow ltr) {
        template.getJdbcOperations().update("delete from test_large_table where id = :id", new Object[] {ltr.getId()});        
    }

    @Override
    public void deleteList(List<LargeTableRow> list) {
        SqlParameterSource[] sources = new SqlParameterSourceUtils().createBatch(list.toArray());
        template.batchUpdate("delete from test_large_table where id = :id", sources);
    }

}
