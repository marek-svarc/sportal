package com.clubeek.dao.impl.performance.test.largetables;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.dao.impl.performance.model.LargeTableRow;

public class LargeTableMapper implements RowMapper<LargeTableRow> {

    @Override
    public LargeTableRow mapRow(ResultSet rs, int rowNum) throws SQLException {
        LargeTableRow l = new LargeTableRow();
        
        l.setId(rs.getInt("id"));
        
        l.setStrCol1(rs.getString("str_col_1"));
        l.setStrCol2(rs.getString("str_col_2"));
        l.setStrCol3(rs.getString("str_col_3"));
        l.setStrCol4(rs.getString("str_col_4"));
        l.setStrCol5(rs.getString("str_col_5"));
        l.setStrCol6(rs.getString("str_col_6"));
        l.setStrCol7(rs.getString("str_col_7"));
        l.setStrCol8(rs.getString("str_col_8"));
        l.setStrCol9(rs.getString("str_col_9"));
        l.setStrCol10(rs.getString("str_col_10"));
        
        l.setIntCol1(rs.getInt("int_col_1"));
        l.setIntCol2(rs.getInt("int_col_2"));
        l.setIntCol3(rs.getInt("int_col_3"));
        l.setIntCol4(rs.getInt("int_col_4"));
        l.setIntCol5(rs.getInt("int_col_5"));
        l.setIntCol6(rs.getInt("int_col_6"));
        l.setIntCol7(rs.getInt("int_col_7"));
        l.setIntCol8(rs.getInt("int_col_8"));
        l.setIntCol9(rs.getInt("int_col_9"));
        l.setIntCol10(rs.getInt("int_col_10"));
        
        return l;
    }

}
