package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.ClubMember;

public class ClubMemberMapper implements RowMapper<ClubMember> {

    @Override
    public ClubMember mapRow(ResultSet rs, int rowNum) throws SQLException {
        ClubMember cm = new ClubMember();
        
        cm.setId(rs.getInt("id"));
        cm.setIdPersonal(rs.getString("id_personal"));
        cm.setIdRegistration(rs.getString("id_registration"));
        cm.setName(rs.getString("name"));
        cm.setSurname(rs.getString("surname"));
        cm.setBirthdate(rs.getDate("birthdate"));
        cm.setStreet(rs.getString("street"));
        cm.setCity(rs.getString("city"));
        cm.setCode(rs.getString("code"));
        cm.setPhoto(rs.getBytes("photo"));
        cm.setClubId(rs.getInt("club_id"));
        
        return cm;
    }

}
