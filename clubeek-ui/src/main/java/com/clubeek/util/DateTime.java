package com.clubeek.util;

import com.clubeek.model.Event;
import com.clubeek.ui.Tools;
import static com.clubeek.ui.Tools.getLocale;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Provides date and time tools
 *
 * @author Marek Svarc
 */
public class DateTime {

    public static <T extends Event> T getEvent(Date date, List<T> events) {
        Date today = new Date();
        if (events != null) {
            for (T event : events) {
                if (today.before(event.getStart()) && sameDay(event.getStart(), date)) {
                    return event;
                }
            }
        }
        return null;
    }

    /**
     * Find the nearest event
     *
     * @param <T> Class of the event. Class must implements interface Event
     * @param events List of the events
     * @return Returns the nearest event
     */
    public static <T extends Event> T getEarliestEvent(List<T> events) {
        long timeDiff, minTimeDiff = Long.MAX_VALUE;
        T earliestEvent = null;

        Date today = new Date();
        for (T event : events) {
            if ((event.getStart() != null) && today.before(event.getStart())) {
                timeDiff = event.getStart().getTime() - today.getTime();
                if (timeDiff < minTimeDiff) {
                    earliestEvent = event;
                    minTimeDiff = timeDiff;
                }
            }
        }

        return earliestEvent;
    }

    /**
     * Porovna dva datumy a pokud znamenaji stejny den vraci true.
     *
     * @return vysledek porovnani
     */
    public static boolean sameDay(Date dateA, Date dateB) {
        Calendar calA = Calendar.getInstance(getLocale());
        calA.setTime(dateA);
        Calendar calB = Calendar.getInstance(getLocale());
        calB.setTime(dateB);

        return (calA.get(Calendar.YEAR) == calB.get(Calendar.YEAR))
                && (calA.get(Calendar.DAY_OF_YEAR) == calB.get(Calendar.DAY_OF_YEAR));
    }

    /** Styl prevodu datumu na text */
    public enum DateStyle {

        NONE, YEAR, SHORT_DAY, DAY, TIME, DAY_AND_TIME, SHORT_DAY_AND_TIME;
    }

    private static String dateToString(Date value, String style) {
        SimpleDateFormat format = new SimpleDateFormat(style, getLocale());
        return format.format(value);
    }

    /**
     * Prevede datum na text dle zvoleneho stylu
     *
     * @param value hodnota datumu
     * @param style styl vypisu
     * @return datum prevedeny na text
     */
    public static String dateToString(Date value, DateStyle style) {
        if (value != null) {
            return dateToString(value, getDateFormatString(style));
        }
        return null;
    }

    /**
     * Returns format string to convert date and time to string representation
     *
     * @param style Style of the date string
     * @return Formating string
     */
    public static String getDateFormatString(DateStyle style) {
        switch (style) {
            case YEAR:
                return "yyyy";
            case SHORT_DAY:
                return "d.M.yyyy";
            case DAY:
                return "EEEE d.M.yyyy";
            case TIME:
                return "k:mm";
            case DAY_AND_TIME:
                return "EEEE d.M.yyyy k:mm";
            case SHORT_DAY_AND_TIME:
                return "d.M.yyyy k:mm";
            default:
                return "";
        }
    }

    /**
     * Sestavi popis datumu a casu konani akce.
     *
     * @param event data konane akce
     * @param styleStart styl vypisu zacatku konani akce
     * @param styleEnd styl vypisu konce konani akce
     * @return datum prevedeny na text
     */
    public static String eventToString(Event event, DateStyle styleStart, DateStyle styleEnd) {
        if (styleEnd == DateStyle.NONE) {
            return dateToString(event.getStart(), styleStart);
        } else if (styleStart == DateStyle.NONE) {
            return dateToString(event.getEnd(), styleEnd);
        } else {
            boolean sameDay = sameDay(event.getStart(), event.getEnd());
            if (sameDay && (styleStart == DateStyle.DAY) && (styleEnd == DateStyle.DAY)) {
                return dateToString(event.getStart(), DateStyle.DAY);
            } else if (sameDay && (styleStart == DateStyle.DAY_AND_TIME) && (styleEnd == DateStyle.DAY_AND_TIME)) {
                return String.format("%s - %s", dateToString(event.getStart(), DateStyle.DAY_AND_TIME), //$NON-NLS-1$
                        dateToString(event.getEnd(), DateStyle.TIME));
            } else {
                return String.format("%s - %s", dateToString(event.getStart(), styleStart), //$NON-NLS-1$
                        dateToString(event.getEnd(), styleEnd));
            }
        }
    }

}
