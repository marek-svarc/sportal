package com.clubeek.enums;

import com.clubeek.model.Messages;

public enum TeamFunction {

    PLAYER(Messages.getString("player")), CAPTAIN(Messages.getString("captain")), TEAM_LEADERSHIP(Messages.getString("teamManager")), COACH_ASSISTANT(Messages.getString("assistant")), COACH(Messages.getString("coach")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    /** Vraci hodnotu flagu pro binarni skladani */
    public int toFlag() {
        return 1 << ordinal();
    }

    /**
     * Kontroluje zda parametr "flags" obsahuje
     * 
     * @param flags
     * @return
     */
    public boolean isFlag(int flags) {
        return (toFlag() & flags) != 0;
    }

    @Override
    public String toString() {
        return this.text;
    }

    private TeamFunction(String text) {
        this.text = text;
    }

    private String text;
}
