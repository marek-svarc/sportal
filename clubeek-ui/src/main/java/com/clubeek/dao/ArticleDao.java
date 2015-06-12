package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.Article;
import com.clubeek.model.Article.Location;

public interface ArticleDao extends Dao<Article> {

    public Article getArticleById(int id);
    
    public List<Article> getAllArticles();
    
    public List<Article> selectArticles(int clubTeamId, int categoryId, Location location);
}
