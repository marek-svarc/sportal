package com.clubeek.ui.components;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 * Label that format text using table
 *
 * @author Marek Svarc
 */
public class FormatedLabel extends Label {

    private StringBuilder text = null;

    private void initLabel() {
        this.setContentMode(ContentMode.HTML);
        this.setWidth(100, Unit.PERCENTAGE);

    }

    public FormatedLabel() {
        initLabel();
    }

    public void beginWrite() {
        if (text == null) {
            text = new StringBuilder();
            text.append("<div class='formatedLabel'>");
        }
    }

    public void endWrite() {
        if (text != null) {
            text.append("</div>");
            this.setValue(text.toString());
            text = null;
        }
    }

    public void writeTableBegin(String title) {
        if ((title != null) && !title.isEmpty()) {
            text.append(String.format("<span class='title'>%s</span><table>", title));
        } else {
            text.append("<table>");
        }
    }

    public void writeTableEnd() {
        text.append("</table>");
    }

    public void writeTableRow(String title, String value) {
        if ((value != null) && !value.isEmpty()) {
            text.append(String.format("<tr><td class='rowTitle'><em>%s</em>:</td><td>%s</td></tr>", title, value));
        }
    }

    public void writeTableRow(String... values) {
        if (values != null) {
            text.append("<tr>");
            for (String value : values) {
                text.append(String.format("<td>%s</td>", value));
            }
            text.append("</tr>");
        }
    }

}
