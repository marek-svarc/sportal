package com.clubeek.util;

import com.vaadin.data.util.converter.StringToDateConverter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Provides data conversion according to DateStyle
 *
 * @author Marek Svarc
 */
public class StyleToDateConverter extends StringToDateConverter {

    final private DateFormat format;

    public StyleToDateConverter(DateTime.DateStyle style) {
        this.format = new SimpleDateFormat(DateTime.getDateFormatString(style));
    }

    @Override
    protected DateFormat getFormat(Locale locale) {
        return format;
    }
}
