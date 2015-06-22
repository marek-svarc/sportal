package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.CategoryDao;
import com.clubeek.model.Category;

/**
* Class that tests {@link CategoryDaoImpl}.
* 
* @author vitfo
*
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("test-config.xml")
public class CategoryDaoImplTest {
    
    @Autowired
    CategoryDao categoryDao;
    
    /**
     * Deletes all categories before each test.
     */
    @Before
    public void deleleCategoriesAll() {
        deleteAll(categoryDao);
    }

    /**
     * Test insert, update, delete.
     */
    @Test
    public void test() {
        insertCategory(categoryDao, "Test category abc", false);
        
        // get all categories
        List<Category> categories = categoryDao.getAllCategories();
        assertTrue(categories.size() > 0);
        
        // get category with description "Test category abc"
        Category c = null;
        for (Category pom : categoryDao.getAllCategories()) {
            if (pom.getDescription().equals("Test category abc")) {
                c = pom;
            }
        }
        assertNotNull(c);
        
        // update category
        c.setDescription("Test category xyz");
        categoryDao.updateRow(c);
        
        // get category with description "Test category xyz"
        c = null;
        for (Category pom : categoryDao.getAllCategories()) {
            if (pom.getDescription().equals("Test category xyz")) {
                c = pom;
            }
        }
        assertNotNull(c);
        
        // delete category
        categoryDao.deleteRow(c.getId());
        
        // get category with description "Test category xyz" -> shoud be null
        c = null;
        for (Category pom : categoryDao.getAllCategories()) {
            if (pom.getDescription().equals("Test category xyz")) {
                c = pom;
            }
        }
        assertNull(c);
    }

    @Test
    public void testExchangeRows() {
        // TODO vitfo, created on 15. 6. 2015 - not yet implemented
    }

    @Test
    public void testGetCategory() {
        insertCategory(categoryDao, "Test category abc", true);
        
        // get category with description "Test category abc"
        Category c = null;
        for (Category pom : categoryDao.getAllCategories()) {
            if (pom.getDescription().equals("Test category abc")) {
                c = pom;
            }
        }
        assertNotNull(c);
        
        int id = c.getId();
        Category idCat = categoryDao.getCategory(id);
        assertNotNull(idCat);
        
        categoryDao.deleteRow(id);
    }

    @Test
    public void testGetActiveCategories() {
        insertCategories(categoryDao, 3, 2);
        
        int numOfActive = categoryDao.getActiveCategories().size();
        assertTrue(numOfActive == 3);
        
        deleteAll(categoryDao);
    }

    @Test
    public void testGetAllCategories() {
        insertCategories(categoryDao, 5, 7);
        
        int numOfCategories = categoryDao.getAllCategories().size();
        System.out.println("Working: " + numOfCategories);
        assertTrue(numOfCategories == 12);
        
        deleteAll(categoryDao);
    }

    /**
     * Inserts categories.
     * 
     * @param categoryDao - implementation of @link {@link CategoryDao}.
     * @param numOfActive - number of active categories
     * @param numOfNotActive - number of not active categories
     */
    public void insertCategories(CategoryDao categoryDao, int numOfActive, int numOfNotActive) {
        List<Category> categories = new ArrayList<Category>();
        for (int i = 0; i < numOfActive; i++) {
            Category c = new Category();
            c.setActive(true);
            categories.add(c);
        }
        for (int j = 0; j < numOfNotActive; j++) {
            Category c = new Category();
            c.setActive(false);
            categories.add(c);
        }
        for (Category c : categories) {
            categoryDao.insertRow(c);
        }
    }
    
    /**
     * Deletes all categories.
     * 
     * @param categoryDao - implementation of @link {@link CategoryDao}.
     */
    public void deleteAll(CategoryDao categoryDao) {
        List<Category> categories = categoryDao.getAllCategories();
        for (Category c : categories) {
            categoryDao.deleteRow(c.getId());
        }
    }
    
    /**
     * Inserts one @link {@link Category}.
     * 
     * @param categoryDao - implementation of @link {@link CategoryDao}.
     * @param description - description of category
     * @param active - if the category should be active or not
     */
    public void insertCategory(CategoryDao categoryDao, String description, boolean active) {
        Category category = new Category();
        category.setActive(active);
        category.setDescription(description);
        category.setSorting(10);
        categoryDao.insertRow(category);
    }
}
