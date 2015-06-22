package com.clubeek.dao;

import java.util.List;

import com.clubeek.enums.LocationType;
import com.clubeek.model.Article;

public interface ArticleDao extends Dao<Article> {

    public Article getArticleById(int id);
    
    public List<Article> getAllArticles();
    
    public List<Article> selectArticles(int clubTeamId, int categoryId, LocationType location);
}
