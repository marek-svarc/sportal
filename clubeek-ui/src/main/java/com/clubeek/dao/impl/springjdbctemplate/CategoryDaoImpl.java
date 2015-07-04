package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.CategoryMapper;
import com.clubeek.model.Category;

/**
 * Implementation of @link {@link Category} interface.
 *
 * @author vitfo
 */
@Repository
public class CategoryDaoImpl implements CategoryDao{
    private NamedParameterJdbcTemplate template;

    @Autowired
    public void init(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void updateRow(Category object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update(
                "update t_category set "
                + "description = :description, "
                + "active = :active, "
                + "sorting = :sorting "
                + "where id = :id", 
                source);
    }

    @Override
    public void insertRow(Category object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("insert into t_category "
                + "(description, active, sorting) values "
                + "(:description, :active, :sorting)", source);
    }

    @Override
    public void deleteRow(int id) {
        template.getJdbcOperations().update("delete from t_category where id = ?", new Object[]{id});  
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public Category getCategory(int id) {
        return template.getJdbcOperations().queryForObject("select * from t_category where id = ?", new Integer[] {id}, new CategoryMapper());
    }

    @Override
    public List<Category> getActiveCategories() {
        return template.query("select * from t_category where active = true", new CategoryMapper());
    }

    @Override
    public List<Category> getAllCategories() {
        return template.query("select * from t_category", new CategoryMapper());
    }

    @Override
    public void deleteRows(List<Category> objects) {
        SqlParameterSource[] sources = new SqlParameterSourceUtils().createBatch(objects.toArray());
        template.batchUpdate("delete from t_category where id = :id", sources);            
    }
}
