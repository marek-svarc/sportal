package com.clubeek.enums;

import com.clubeek.model.Messages;

public enum UserRoleType {

    EMPTY("member"),  //$NON-NLS-1$
    EDITOR("editor"),  //$NON-NLS-1$
    SPORT_MANAGER("sportManager"),  //$NON-NLS-1$
    CLUB_MANAGER("clubManager"),  //$NON-NLS-1$
    ADMINISTRATOR("administrator"); //$NON-NLS-1$

    private UserRoleType(String textId) {
        this.textId = textId;
    }

    @Override
    public String toString() {
        return Messages.getString(textId);
    }

    public final String textId;
}
