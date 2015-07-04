package com.clubeek.enums;

import com.clubeek.model.Messages;

public enum LocationType {

    BULLETIN_BOARD, NEWS;

    @Override
    public String toString() {
        switch (LocationType.values()[this.ordinal()]) {
        case BULLETIN_BOARD:
            return Messages.getString("bulletinBoard"); //$NON-NLS-1$
        case NEWS:
            return Messages.getString("news"); //$NON-NLS-1$
        }
        return ""; //$NON-NLS-1$
    }
}
