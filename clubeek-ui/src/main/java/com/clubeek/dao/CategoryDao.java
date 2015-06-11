package com.clubeek.dao;

import java.util.List;

import com.clubeek.dao.impl.ownframework.rep.RepCategory;
import com.clubeek.model.Category;

public interface CategoryDao {

    public Category getCategory(int id);
    
    public List<Category> getActiveCategories();
    
    public List<Category> getAllCategories();
    
    public RepCategory getInstance();
}
