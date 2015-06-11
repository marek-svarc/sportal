package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.ArticleDao;
import com.clubeek.dao.impl.ownframework.rep.RepArticle;
import com.clubeek.model.Article;
import com.clubeek.model.Article.Location;

public class ArticleDaoImpl implements ArticleDao {

    @Override
    public Article getArticle(int id) {
//        return RepArticle.selectById(id, new RepArticle.TableColumn[]{RepArticle.TableColumn.CAPTION,
//                RepArticle.TableColumn.CONTENT, RepArticle.TableColumn.CREATION_DATE});
        // TODO vitfo, created on 11. 6. 2015 - zkontorlovat zda funguje i pro ne null
        return RepArticle.selectById(id, null);
    }

    @Override
    public List<Article> getAllArticles() {
        return RepArticle.selectAll(null);
    }

    @Override
    public RepArticle getRepArticleInstance() {
        return RepArticle.getInstance();
    }

    @Override
    public List<Article> selectArticles(int clubTeamId, int categoryId, Location location) {
        return RepArticle.select(clubTeamId, categoryId, location, null);
    }

}
