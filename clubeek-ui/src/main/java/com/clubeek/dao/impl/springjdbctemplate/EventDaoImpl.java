package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.clubeek.dao.EventDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.EventMapper;
import com.clubeek.model.Event;

public class EventDaoImpl extends DaoImpl implements EventDao {

    @Override
    public void updateRow(Event object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("update t_event set "
                + "start = :start, "
                + "finish = :finish, "
                + "caption = :caption, "
                + "place = :place, "
                + "description = :description, "
                + "sign_participation = :signParticipation, "
                + "club_team_id = :clubTeamId, "
                + "category_id = :categoryId "
                + "where id = :id", source);
    }

    @Override
    public void insertRow(Event object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("insert into t_event "
                + "(start, finish, caption, place, description, sign_participation, club_team_id, category_id) values "
                + "(:start, :finish, :caption, :place, :description, :signParticipation, :clubTeamId, :categoryId)", source);
    }

    @Override
    public void deleteRow(int id) {
        template.getJdbcOperations().update("delete from t_event where id = ?", new Object[] {id});        
    }

    @Override
    public void deleteRows(List<Event> objects) {
        SqlParameterSource[] sources = new SqlParameterSourceUtils().createBatch(objects.toArray());
        template.batchUpdate("delete from t_event where id = :id", sources);
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public List<Event> getAllEvents() {
        return template.query("select * from t_event", new EventMapper());
    }

}
