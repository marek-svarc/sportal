package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import com.clubeek.dao.ContactDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.ContactMapper;
import com.clubeek.model.Contact;
import org.springframework.stereotype.Repository;

@Repository
public class ContactDaoImpl extends DaoImpl implements ContactDao {

    @Override
    public void updateRow(Contact object) {
        template.getJdbcOperations().update("update t_contact set "
                + "contact = ?, "
                + "description = ?, "
                + "contact_type = ?, "
                + "notification_type = ? "
                + "where id = ?", new Object[] {
                        object.getContact(),
                        object.getDescription(),
                        object.getContactType().ordinal(),
                        object.getNotificationType().ordinal(),
                        object.getId()
                });        
    }

    @Override
    public void insertRow(Contact object) {
        template.getJdbcOperations().update("insert into t_contact "
                + "(contact, description, contact_type, notification_type, club_member_id) values (?, ?, ?, ?, ?)", 
                new Object[] {
                        object.getContact(),
                        object.getDescription(),
                        object.getContactType().ordinal(),
                        object.getNotificationType().ordinal(),
                        object.getClubMemberId()
                });        
    }

    @Override
    public void deleteRow(int id) {
        template.getJdbcOperations().update("delete from t_contact where id = ?", new Integer[]{id});
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public void updateContacts(List<Contact> oldContacts, List<Contact> newContacts) {
        for (Contact newContac : newContacts) {
            // if the contact in the new-list IS contained in the old-list -> update
            if (containsContact(newContac, oldContacts)) {
                updateRow(newContac);
            }
            // if the contact in the new-list IS NOT contained in the old-list -> insert
            else {
                insertRow(newContac);
            }
        }
        
        for (Contact oldContact : oldContacts) {
            // if the contact in the old-list IS NOT contained in the new-list -> delete
            if (!containsContact(oldContact, newContacts)) {
                deleteRow(oldContact.getId());
            }
        }
    }

    @Override
    public List<Contact> getContactsByClubMemberId(int clubMemberId) {
        return template.getJdbcOperations().query("select * from t_contact where club_member_id = ?", new Integer[] {clubMemberId}, new ContactMapper());
    }

    @Override
    public List<Contact> getAllContacts() {
        return template.query("select * from t_contact", new ContactMapper());
    }

    /**
     * Searches @link {@link Contact} in list of contacts by its id.
     * @param contactToBeSearch - contact to be searched
     * @param listOfContacts - list in which the contact will be searched
     * @return true if found, false otherwise
     */
    private boolean containsContact(Contact contactToBeSearch, List<Contact> listOfContacts) {
        for (Contact contactInList : listOfContacts) {
            if (contactInList.getId() == contactToBeSearch.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteRows(List<Contact> objects) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not supported.");
    }
}
