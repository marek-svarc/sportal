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

public interface MyEnumDao {

    public void saveMyEnumAsValue(MyEnum object);

    public void saveMyEnumAsNumber(MyEnum object);

    public void deleteAllEnumAsValue();

    public void deleteAllEnumAsNumber();

    public List<MyEnum> selectMyEnumByValue(String value);

    public List<MyEnum> selectMyEnumByNumber(int number);
}
