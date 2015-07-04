package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.impl.ownframework.rep.RepCategory;
import com.clubeek.model.Category;

public class CategoryDaoImpl implements CategoryDao {

    @Override
    public Category getCategory(int id) {
        return RepCategory.selectById(id, null);
    }

    @Override
    public List<Category> getActiveCategories() {
        return RepCategory.select(true, null);
    }

    @Override
    public List<Category> getAllCategories() {
        return RepCategory.selectAll(null);
    }

    @Override
    public void updateRow(Category object) {
        RepCategory.update(object);        
    }

    @Override
    public void insertRow(Category object) {
        RepCategory.insert(object);        
    }

    @Override
    public void deleteRow(int id) {
        RepCategory.delete(id);        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        RepCategory.exchange(idA, idB);        
    }

    @Override
    public void deleteRows(List<Category> objects) {
        throw new UnsupportedOperationException("Not supported.");
    }

}
