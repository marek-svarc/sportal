package com.clubeek.dao.impl.performance.test.indexes;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.dao.impl.performance.model.MyEntry;

public class MyEntryMapper implements RowMapper<MyEntry> {

    @Override
    public MyEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        MyEntry me = new MyEntry();
        
        me.setId(rs.getInt("id"));
        me.setStringCol(rs.getString("string_col"));
        me.setStringColIndexed(rs.getString("string_col_indexed"));
        me.setIntCol(rs.getInt("int_col"));
        me.setIntColIndexed(rs.getInt("int_col_indexed"));
        me.setMyEnum(rs.getInt("my_enum"));
        me.setMyEnumIndexed(rs.getInt("my_enum_indexed"));
        
        return me;
    }
}
