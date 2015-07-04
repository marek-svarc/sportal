package com.clubeek.enums;

import com.clubeek.model.Messages;

public enum NotificationType {

    NEVER, ALWAYS, ONLY_IMPORTANT;

    public String toString() {
        switch (ordinal()) {
        case 0:
            return Messages.getString("no"); //$NON-NLS-1$
        case 1:
            return Messages.getString("yesAlways"); //$NON-NLS-1$
        case 2:
            return Messages.getString("yesSignificantOnly"); //$NON-NLS-1$
        default:
            return ""; //$NON-NLS-1$
        }
    };
}
