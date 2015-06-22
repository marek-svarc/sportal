package com.clubeek.enums;

import com.clubeek.model.Messages;

public enum OwnerType {

    CLUB, CATEGORY, TEAM, CLUB_ALL;

    @Override
    public String toString() {
        switch (OwnerType.values()[this.ordinal()]) {
        case CLUB_ALL:
            return Messages.getString("wholeWeb"); //$NON-NLS-1$
        case CLUB:
            return Messages.getString("onlyClub"); //$NON-NLS-1$
        case CATEGORY:
            return Messages.getString("onlyCategory"); //$NON-NLS-1$
        case TEAM:
            return Messages.getString("onlyTeam"); //$NON-NLS-1$
        }
        return ""; //$NON-NLS-1$
    }
}
