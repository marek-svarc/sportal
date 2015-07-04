package com.clubeek.enums;

import com.clubeek.model.Messages;

public enum ContactType {

    EMAIL, PHONE;

    public String toString() {
        switch (ordinal()) {
        case 0:
            return Messages.getString("email"); //$NON-NLS-1$
        case 1:
            return Messages.getString("phone"); //$NON-NLS-1$
        default:
            return ""; //$NON-NLS-1$
        }
    };
}
