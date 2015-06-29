package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import com.clubeek.dao.ClubMemberDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.ClubMemberMapper;
import com.clubeek.model.ClubMember;
import org.springframework.stereotype.Repository;

@Repository
public class ClubMemberDaoImpl extends DaoImpl implements ClubMemberDao {

    @Override
    public void updateRow(ClubMember object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("update t_club_member set "
                + "id_personal = :idPersonal, "
                + "id_registration = :idRegistration, "
                + "name = :name, "
                + "surname = :surname, "
                + "birthdate = :birthdate, "
                + "street = :street, "
                + "city = :city, "
                + "code = :code, "
                + "photo = :photo "
                + "where id = :id", source);
    }

    @Override
    public void insertRow(ClubMember object) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(object);
        template.update("insert into t_club_member "
                + "(id_personal, id_registration, name, surname, birthdate, street, city, code, photo) values "
                + "(:idPersonal, :idRegistration, :name, :surname, :birthdate, :street, :city, :code, :photo)"
                , source);
    }

    @Override
    public void deleteRow(int id) {
        template.getJdbcOperations().update("delete from t_club_member where id = ?", id);        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();  
    }

    @Override
    public ClubMember getClubMember(int id) {
        return template.getJdbcOperations().queryForObject("select * from t_club_member where id = ?", new Integer[] {id}, new ClubMemberMapper());
    }

    @Override
    public List<ClubMember> getClubMembersByTeamId(int teamId) {
        // TODO vitfo, created on 16. 6. 2015 - Is it correct? Should be optimized?
        return template.getJdbcOperations().query(
                "select * from t_club_member where id in (select club_member_id from t_team_member where club_team_id = ?)", 
                new Integer[] {teamId}, 
                new ClubMemberMapper());
    }

    @Override
    public List<ClubMember> getClubMembersByDateOfBirth(int yearMin, int yearMax) {
        // TODO vitfo, created on 16. 6. 2015 - Should be optimized?
        return template.getJdbcOperations().query(
                "select * from t_club_member where (date_part('year', birthdate) >= ? and date_part('year', birthdate) <= ?)", 
                new Integer[] {yearMin, yearMax},
                new ClubMemberMapper());
    }

    @Override
    public List<ClubMember> getAllClubMembers() {
        return template.query("select * from t_club_member", new ClubMemberMapper());
    }

    @Override
    public void updateClubMember(ClubMember clubMember) {
        // TODO vitfo, created on 16. 6. 2015 - not needed
        updateRow(clubMember);
    }

    @Override
    public List<ClubMember> getClubMembersByTeamTrainingId(int teamTrainingId) {
        return template.getJdbcOperations().query(""
                + "select * from t_club_member where id in (select club_member_id from t_participant_of_training where team_training_id = ?)", 
                new Object[] {teamTrainingId}, new ClubMemberMapper());
    }

    @Override
    public void addClubMemberToTeamTraining(int clubMemberId, int teamTrainingId) {
        template.getJdbcOperations().update("insert into t_participant_of_training (club_member_id, team_training_id) values (?, ?)",
                new Object[] {clubMemberId, teamTrainingId});        
    }
}
