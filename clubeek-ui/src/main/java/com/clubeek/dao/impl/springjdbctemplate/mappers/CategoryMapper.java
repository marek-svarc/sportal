package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.Category;

/**
 * Mapper for {@link Category} model object.
 * 
 * @author vitfo
 */
public class CategoryMapper implements RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        Category c = new Category();
        
        c.setId(rs.getInt("id"));
        c.setDescription(rs.getString("description"));
        c.setActive(rs.getBoolean("active"));
        c.setSorting(rs.getInt("sorting"));
        
        return c;
    }

}
