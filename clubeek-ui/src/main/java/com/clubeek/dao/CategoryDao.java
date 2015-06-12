package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.Category;

public interface CategoryDao extends Dao<Category> {

    public Category getCategory(int id);
    
    public List<Category> getActiveCategories();
    
    public List<Category> getAllCategories();
}
