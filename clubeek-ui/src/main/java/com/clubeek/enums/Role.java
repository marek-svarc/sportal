package com.clubeek.enums;

import com.clubeek.model.Messages;

public enum Role {

    EMPTY("member"), EDITOR("editor"), SPORT_MANAGER("sportManager"), CLUB_MANAGER("clubManager"), ADMINISTRATOR( //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "administrator"); //$NON-NLS-1$

    private Role(String textId) {
        this.textId = textId;
    }

    @Override
    public String toString() {
        return Messages.getString(textId);
    }

    public final String textId;
}
