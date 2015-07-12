package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

import com.clubeek.dao.ClubUrlDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.ClubUrlMapper;
import com.clubeek.model.ClubUrl;

/**
 * Implementation of {@link ClubUrlDao} interface.
 * 
 * @author vitfo
 */
@Repository
public class ClubUrlDaoImpl extends DaoImpl implements ClubUrlDao {
    private Logger logger = LoggerFactory.getLogger(ClubUrlDaoImpl.class);

    @Override
    public List<ClubUrl> getAllClubUrls(int clubId) {
        return template.getJdbcOperations().query(
                "select * from t_club_url where club_id = ?", new Object[] {clubId}, new ClubUrlMapper());
    }

    @Override
    public Integer getClubId(String url) {
        Integer result = null;
        try {
            // In JdbcTemplate following methods: queryForInt, queryForLong, queryForObject expect that executed query will return one and only one row. 
            // If you get no rows or more than one row that will result in IncorrectResultSizeDataAccessException.
            result = template.getJdbcOperations().queryForObject("select club_id from t_club_url where url = ?", new Object[] {url}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("not result found");
        }
        return result;
    }

    @Override
    public void updateClubUrl(ClubUrl clubUrl) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(clubUrl);
        template.update("update t_club_url set "
                + "url = :url "
                + "where id = :id", source);
    }

    @Override
    public List<ClubUrl> getAllClubUrls() {
        return template.query("select * from t_club_url", new ClubUrlMapper());
    }

    @Override
    public void addClubUrl(ClubUrl clubUrl) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(clubUrl);
        template.update("insert into t_club_url "
                + "(url, club_id) values "
                + "(:url, :clubId)", source);
    }

    @Override
    public void deleteClubUrl(int id) {
        template.getJdbcOperations().update("delete from t_club_url where id = ?", new Object[] {id});
    }
}
