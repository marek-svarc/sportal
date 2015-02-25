package com.clubeek.ui;

import java.sql.SQLException;

import com.clubeek.db.Repository;
import com.clubeek.model.Unique;
import com.clubeek.ui.views.Navigation;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Trida zobrazuje komponenty splnujici Rozhrani ModalInput<T> v modalnim
 * dialogovem okne
 * 
 * @author Marek Svarc
 * 
 * @param <T>
 *            Trida dat modifikovanech v dialogu
 */
@SuppressWarnings("serial")
@Theme("baseTheme")
public class ModalDialog<T> extends Window {

	public enum Mode {
		ADD_ONCE, ADD_MULTI, EDIT;
	}

	/**
	 * Vytvori a inicializuje instanci tridy ModalDialog<T>
	 * 
	 * @param caption
	 *            Nadpis dialogoveho okna
	 * @param frame
	 *            Komponenta zobrazovan� v dialogovem okne
	 * @param data
	 *            Editovana data
	 * @param clickListener
	 *            Udalost pri kladnem ukonceni dialogu
	 */
	public ModalDialog(final Mode mode, String caption, final ModalInput<T> frame, final T data, final ClickListener clickListener) {

		super(caption);

		VerticalLayout container = new VerticalLayout();
		container.setStyleName("mainlayout"); //$NON-NLS-1$
		this.setContent(container);

		// vlastni osetreni chyb
		final Label laErrors = new Label();
		laErrors.setVisible(false);
		laErrors.setStyleName("label-error"); //$NON-NLS-1$
		container.addComponent(laErrors);

		UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				String cause = String.format("<strong>%s</strong><br/>", Messages.getString("wrongInputOfValues")); //$NON-NLS-1$ //$NON-NLS-2$

				for (Throwable t = event.getThrowable(); t != null; t = t.getCause())
					if ((t.getCause() == null) && (t.getLocalizedMessage() != null)) {
						cause += t.getLocalizedMessage() + "<br/>"; //$NON-NLS-1$
					}

				laErrors.setContentMode(ContentMode.HTML);
				laErrors.setValue(cause);
				laErrors.setVisible(true);
			}
		});

		// oblast zadavani
		container.addComponent(frame);

		// oblast tlacitek
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setStyleName("btnlayout"); //$NON-NLS-1$
		container.addComponent(buttonLayout);

		addButton(buttonLayout,
				mode == Mode.EDIT ? Messages.getString("ok") : Messages.getString("add"), new Button.ClickListener() { //$NON-NLS-1$ //$NON-NLS-2$

					@Override
					public void buttonClick(ClickEvent event) {
						// ziskani dat z dialogu
						frame.inputToData(data);
						// vyvolani udalosti
						clickListener.buttonClick(event);
						// ukonceni dialogu
						if (mode != Mode.ADD_MULTI)
							close();
					}
				});

		addButton(buttonLayout,
				mode != Mode.ADD_MULTI ? Messages.getString("storno") : Messages.getString("close"), new Button.ClickListener() { //$NON-NLS-1$ //$NON-NLS-2$

					@Override
					public void buttonClick(ClickEvent event) {
						close();
					}
				});

		// nastaveni vlastnosti dialogu
		this.center();
		this.setClosable(true);
		this.setModal(true);
		this.setResizable(false);
		this.addStyleName("modal"); //$NON-NLS-1$

		// inicializace komponent
		frame.dataToInput(data);
	}

	public static <T extends Unique> void show(final Component owner, final Mode mode, String caption, final ModalInput<T> frame,
			final T data, final Repository<T> dataAdmin, final Navigation navigation) {

		final ModalDialog<T> dlg = new ModalDialog<T>(mode, caption, frame, data, new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// zmena dat
				try {
					switch (mode) {
					case ADD_ONCE:
					case ADD_MULTI:
						dataAdmin.insertRow(data);
						break;
					case EDIT:
						dataAdmin.updateRow(data);
						break;
					}
				} catch (SQLException e) {
					Tools.msgBoxSQLException(e);
				}
				// aktualizace stranky
				if (owner instanceof View)
					((View) owner).enter(null);
				// aktualizace navigace v aplikaci
				if (navigation != null)
					navigation.updateNavigationMenu();
			}
		});

		// zobrazeni dialogu
		owner.getUI().addWindow(dlg);
	}

	/* PRIVATE */

	/**
	 * Pomocna metoda pro pridani tlacitka
	 * 
	 * @param layout
	 *            vlastnik tlacitka
	 * @param caption
	 *            popis tlacitka
	 * @param clickListener
	 *            udalost vyvolana kliknutim na tlacitko
	 */
	private void addButton(HorizontalLayout layout, String caption, Button.ClickListener clickListener) {
		Button button = new Button(caption, clickListener);
		button.addStyleName(ValoTheme.BUTTON_SMALL);
		layout.addComponent(button);
	}
}
