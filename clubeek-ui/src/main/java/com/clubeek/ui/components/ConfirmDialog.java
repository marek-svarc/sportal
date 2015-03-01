package com.clubeek.ui.components;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Simple confirm dialog.
 * @author elopin
 */
public class ConfirmDialog {

    /**
     * Creates and add simple modal confirm dialog with "YES" "NO" options.
     * @param title title of modal window
     * @param text confirmation text
     * @param confirmation confirmation listener
     */
    public static void addConfirmDialog(String title, String text, final Confirmation confirmation) {
        final Window window = new Window(title);
        window.setModal(true);
        window.center();

        Button yes = new Button("Ano", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                confirmation.onConfirm();
                window.close();
            }
        });
        yes.addStyleName(ValoTheme.BUTTON_TINY);
        
        Button no = new Button("Ne", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window.close();
            }
        });
        no.addStyleName(ValoTheme.BUTTON_TINY);

        HorizontalLayout buttons = new HorizontalLayout(yes, no);
        buttons.setSpacing(true);
        
        Label textLabel = new Label(text, ContentMode.HTML);
        textLabel.setWidth(null);
        
        VerticalLayout layout = new VerticalLayout(textLabel, buttons);
        layout.setSpacing(true);
        layout.setMargin(true);
        window.setContent(layout);
        UI.getCurrent().addWindow(window);
    }

    /**
     * Listener for confirmation action.
     */
    public interface Confirmation {

        /**
         * Confirmation action.
         */
        void onConfirm();
    }
}
