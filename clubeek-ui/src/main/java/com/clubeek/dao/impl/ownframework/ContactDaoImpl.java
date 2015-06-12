package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.ContactDao;
import com.clubeek.dao.impl.ownframework.rep.RepContact;
import com.clubeek.model.Contact;
import com.clubeek.model.Unique;

public class ContactDaoImpl implements ContactDao {

    @Override
    public void updateRow(Contact object) {
        RepContact.update(object);        
    }

    @Override
    public void insertRow(Contact object) {
        insertRow(object);        
    }

    @Override
    public void deleteRow(int id) {
        RepContact.delete(id);        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public void updateContacts(List<Contact> oldContacts, List<Contact> newContacts) {
        RepContact.update(oldContacts, newContacts);        
    }

    @Override
    public List<Contact> getContactsByClubMemberId(int clubMemberId) {
        return RepContact.selectByClubMemberId(clubMemberId, null);
    }
}
