package com.clubeek.dao.impl.performance.test.enums;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.clubeek.dao.impl.performance.enums.MyEnum;

@Resource
public class MyEnumDaoImpl implements MyEnumDao {

    private NamedParameterJdbcTemplate template;

    @Autowired
    public void init(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void saveMyEnumAsValue(MyEnum object) {
        template.getJdbcOperations().update("insert into test_enum_value (enum_value) values (?)", object.name());
    }

    @Override
    public void saveMyEnumAsNumber(MyEnum object) {
        template.getJdbcOperations().update("insert into test_enum_number (enum_number) values (?)", object.ordinal());
    }

    @Override
    public void deleteAllEnumAsValue() {
        template.getJdbcOperations().update("delete from test_enum_value");
    }

    @Override
    public void deleteAllEnumAsNumber() {
        template.getJdbcOperations().update("delete from test_enum_number");
    }

    @Override
    public List<MyEnum> selectMyEnumByValue(String value) {
        return template.getJdbcOperations().query("select * from test_enum_value where enum_value = ?", new Object[] {value}, new RowMapper<MyEnum>() {

            @Override
            public MyEnum mapRow(ResultSet rs, int rowNum) throws SQLException {
                return MyEnum.valueOf(rs.getString("enum_value"));
            }
            
        });
    }

    @Override
    public List<MyEnum> selectMyEnumByNumber(int number) {
        return template.getJdbcOperations().query("select * from test_enum_number where enum_number = ?", new Object[] {number}, new RowMapper<MyEnum>() {

            @Override
            public MyEnum mapRow(ResultSet rs, int rowNum) throws SQLException {
                return MyEnum.values()[rs.getInt("enum_number")];
            }
            
        });
    }
}
