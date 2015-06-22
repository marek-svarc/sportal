package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.clubeek.dao.ArticleDao;
import com.clubeek.dao.impl.ownframework.rep.RepArticle.TableColumn;
import com.clubeek.dao.impl.springjdbctemplate.mappers.ArticleMapper;
import com.clubeek.enums.LocationType;
import com.clubeek.enums.OwnerType;
import com.clubeek.model.Article;

/**
 * Implementation of @link {@link ArticleDao} interface.
 *
 * @author vitfo
 */
@Repository
public class ArticleDaoImpl extends DaoImpl implements ArticleDao {

    @Override
    public void updateRow(Article object) {
        template.getJdbcOperations().update("update t_article set "
                + "location_type = ?, "
                + "priority = ?, "
                + "caption = ?, "
                + "summary = ?, "
                + "content = ?, "
                + "creation_date = ?, "
                + "expiration_date = ?, "
                + "owner_type = ?, "
                + "club_team_id = ?, "
                + "category_id = ?"
                , new Object[] {
                        object.getLocationType().ordinal(),
                        object.getPriority(),
                        object.getCaption(),
                        object.getSummary(),
                        object.getContent(),
                        object.getCreationDate(),
                        object.getExpirationDate(),
                        object.getOwnerType().ordinal(),
                        object.getClubTeamId(),
                        object.getCategoryId()
                });
    }

    @Override
    public void insertRow(Article object) {
        template.getJdbcOperations().update("insert into t_article ("
                + "location_type, "
                + "priority, "
                + "caption, "
                + "summary, "
                + "content, "
                + "creation_date, "
                + "expiration_date, "
                + "owner_type, "
                + "club_team_id, "
                + "category_id"
                + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
                new Object[] {
                        object.getLocationType().ordinal(),
                        object.getPriority(),
                        object.getCaption(),
                        object.getSummary(),
                        object.getContent(),
                        object.getCreationDate(),
                        object.getExpirationDate(),
                        object.getOwnerType().ordinal(),
                        object.getClubTeamId(),
                        object.getCategoryId()
                });
    }

    @Override
    public void deleteRow(int id) {
        template.getJdbcOperations().update("delete from t_article where id = ?", new Integer[]{id});
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();
    }   

    @Override
    public Article getArticleById(int id) {
        return template.getJdbcOperations().queryForObject("select * from t_article where id = ?", new Integer[]{id}, new ArticleMapper());
    }

    @Override
    public List<Article> getAllArticles() {
        return template.query("select * from t_article", new ArticleMapper());
    }

    @Override
    public List<Article> selectArticles(int clubTeamId, int categoryId, LocationType location) {
        String str = String.format("SELECT * FROM t_article WHERE %s AND %s AND %s ORDER BY %s DESC, %s DESC",
                sqlOwnerCondition(clubTeamId, categoryId),
                sqlLocationCondition(location), 
                sqlExpiredDateCondition(), 
                "priority", 
                "creation_date");
        return template.query(str, new ArticleMapper());
    }
    
    private String sqlOwnerCondition(int clubTeamId, int categoryId) {
        if ((clubTeamId > 0) || (categoryId > 0)) {
            return String.format("((%s = %d) OR (%s = %d) OR (%s = %d))", TableColumn.CLUB_TEAM_ID, clubTeamId,
                    "category_id", categoryId, "owner_type", OwnerType.CLUB_ALL.ordinal());
        } else {
            return String.format("((%s = %d) OR (%s = %d))", "owner_type", OwnerType.CLUB_ALL.ordinal(),
                    "owner_type", OwnerType.CLUB.ordinal());
        }
    }

    private String sqlExpiredDateCondition() {
        return String.format("((%s is null) OR (LOCALTIMESTAMP <= %s))", "expiration_date", "expiration_date");
    }
    
    private static String sqlLocationCondition(LocationType location) {
        return String.format("(%s = %d)", "location_type", location.ordinal());
    }
}
