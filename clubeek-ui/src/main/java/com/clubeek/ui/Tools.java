package com.clubeek.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.clubeek.model.TeamMatch;
import com.clubeek.model.TeamTraining;
import com.clubeek.model.Unique;
import com.clubeek.util.DateTime;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Trida dle vzoru singleton poskytujici ruzne knihovni funkce - pristup ke
 * zdrojum - pomocnĂ© metody pro prĂˇci s UI
 */
@Theme("baseTheme")
public final class Tools {

    /* PRIVATE */
    /** Staticka instance tridy Tools */
    private static Tools instance = new Tools();

    /** Lokalni nastaveni pro Ceskou republiku */
    private static Locale locale = new Locale("cs", "CZ"); //$NON-NLS-1$ //$NON-NLS-2$

    /** Absolutni cesta k aplikaci */
    private String mAppAbsolutePath;

    /** Privatni konstruktor */
    private Tools() {
        mAppAbsolutePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    }

    /* PUBLIC */
    /** Vraci instanci tridy Tools */
    public static Tools getInstance() {
        return instance;
    }

    /** Vraci absolutni cestu k aplikaci */
    public String getAppAbsolutePath() {
        return mAppAbsolutePath;
    }

    /** Vraci soubor pro dane ikony */
    public static enum IconId {

        IMG_EMPTY_PORTRAIT("images/img_empty_portrait.png"); //$NON-NLS-1$

        private IconId(String path) {
            this.path = path;
            this.image = new ThemeResource(path);
        }

        /** Local URL of the image */
        final public String path;

        /** Resource for repeated use */
        final public ThemeResource image;
    }

    /** Vraci narodni nastaveni */
    public static Locale getLocale() {
        return locale;
    }

    /* MESSAGE BOX */
    public static void msgBoxException(Exception e) {
        Notification.show(Messages.getString("error"), e.getLocalizedMessage(), Type.ERROR_MESSAGE); //$NON-NLS-1$
    }

    public static void msgBoxSQLException(SQLException e) {
        Notification.show(Messages.getString("errorInDatabase"), e.getLocalizedMessage(), Type.ERROR_MESSAGE); //$NON-NLS-1$
    }

    public static class Components {

        /** Vytvori a inicializuje komponentu PasswordField */
        public static PasswordField createPasswordField(String name) {
            return createPasswordField(name, false, null);
        }

        /** Vytvori a inicializuje komponentu PasswordField */
        public static PasswordField createPasswordField(String name, boolean required, String requiredError) {
            PasswordField pf = new PasswordField(name);
            pf.setStyleName(ValoTheme.TEXTFIELD_TINY);
            pf.setWidth(100, Unit.PERCENTAGE);
            Validators.setRequired(pf, required, requiredError);

            return pf;
        }

        /** Vytvori a inicializuje komponentu TextField */
        public static TextField createTextField(String name) {
            return createTextField(name, false, null);
        }

        /** Vytvori a inicializuje komponentu TextField */
        public static TextField createTextField(String name, boolean required, String requiredError) {
            TextField tf = new TextField(name);
            tf.setStyleName(ValoTheme.TEXTFIELD_TINY);
            tf.setWidth(100, Unit.PERCENTAGE);
            Validators.setRequired(tf, required, requiredError);

            return tf;
        }

        /** Vytvori a inicializuje komponentu TextArea */
        public static TextArea createTextArea(String name) {
            return createTextArea(name, false, null);
        }

        /** Vytvori a inicializuje komponentu TextArea */
        public static TextArea createTextArea(String name, boolean required, String requiredError) {
            TextArea ta = new TextArea(name);
            ta.setStyleName(ValoTheme.TEXTAREA_TINY);
            ta.setSizeFull();
            Validators.setRequired(ta, required, requiredError);

            return ta;
        }

        /** Vytvori a inicializuje komponentu DatField */
        public static DateField createDateField(String caption) {
            return createDateField(caption, false, null);
        }

        /** Vytvori a inicializuje komponentu DatField */
        public static DateField createDateField(String caption, boolean required, String requiredError) {
            DateField df = new DateField(caption);
            df.setStyleName(ValoTheme.DATEFIELD_TINY);
            df.setDateFormat("E dd.MM. yyyy"); //$NON-NLS-1$
            df.setSizeFull();
            df.setResolution(Resolution.DAY);
            df.setLenient(true);
            df.setLocale(Tools.getLocale());
            Validators.setRequired(df, required, requiredError);

            return df;
        }

        /** Vytvori a inicializuje komponentu OptionGroup */
        public static ComboBox createComboBox(String caption, Object[] items) {
            ComboBox cx = new ComboBox(caption);
            cx.setStyleName(ValoTheme.COMBOBOX_TINY);
            cx.setWidth(100, Unit.PERCENTAGE);
            for (int i = 0; i < items.length; ++i) {
                cx.addItem(i);
                cx.setItemCaption(i, items[i].toString());
            }
            return cx;
        }

        /** Vytvori a inicializuje komponentu OptionGroup */
        public static OptionGroup createOptionGroup(String caption, String[] items) {
            OptionGroup og = new OptionGroup(caption);
            og.setStyleName(ValoTheme.OPTIONGROUP_SMALL);
            og.setSizeFull();
            for (int i = 0; i < items.length; ++i) {
                og.addItem(i);
                og.setItemCaption(i, items[i]);
            }
            return og;
        }

        /** Vytvori a inicializuje komponentu CheckBox */
        public static CheckBox createCheckBox(String name) {
            CheckBox cb = new CheckBox(name);
            cb.setSizeFull();

            return cb;
        }

        /** Vytvori a inicializuje komponentu NativeSelect */
        public static <T> NativeSelect createNativeSelect(String name, List<T> items) {
            return createNativeSelect(name, items, false);
        }

        /** Vytvori a inicializuje komponentu NativeSelect */
        public static <T> NativeSelect createNativeSelect(String name, List<T> items, Boolean useUniqueId) {
            NativeSelect ns = new NativeSelect(name);
            ns.setStyleName(ValoTheme.COMBOBOX_TINY);
            ns.setWidth(100, Unit.PERCENTAGE);
            initNativeSelect(ns, items, useUniqueId);
            return ns;
        }

        /** Vytvori a inicializuje komponentu PopupDateField */
        public static PopupDateField createPopupDateField(String name) {
            PopupDateField df = new PopupDateField(name);
            df.setStyleName(ValoTheme.DATEFIELD_TINY);
            df.setSizeFull();

            return df;
        }

        /** Rozmisti formularove komponenty do dvou sloupcu */
        public static HorizontalLayout createMultipleColumnsForm(int count, Component[] components) {
            HorizontalLayout hlForms = new HorizontalLayout();
            hlForms.setSizeFull();
            hlForms.setSpacing(false);
            hlForms.setMargin(false);

            FormLayout[] layouts = new FormLayout[count];
            for (int i = 0; i < count; ++i) {
                layouts[i] = new FormLayout();
                layouts[i].setSizeFull();
                if (i < (count - 1)) {
                    layouts[i].setStyleName("horzgap"); //$NON-NLS-1$
                }
                hlForms.addComponent(layouts[i]);
            }

            int maxCount = Math.round((float) components.length / count);

            int column = 0, counter = 0;
            for (int i = 0; i < components.length; ++i) {
                if (components[i] != null) {
                    layouts[column].addComponent(components[i]);
                }
                ++counter;
                if (counter >= maxCount) {
                    ++column;
                    counter = 0;
                }
            }

            return hlForms;
        }

        /** Zarovná komponenty do řady a aplikuje stylování */
        public static HorizontalLayout createHorizontalLayout(String caption, Component... components) {
            // vytvoreni vodorovneho zarovnani komponent
            HorizontalLayout layout = new HorizontalLayout(components);
            if (caption != null) {
                layout.setCaption(caption);
            }

            // stylovani komponent dle poradi
            for (int i = 0; i < components.length; ++i) {
                if (i == 0) {
                    components[i].addStyleName("rowLowFirst"); //$NON-NLS-1$
                } else if (i == (components.length - 1)) {
                    components[i].addStyleName("rowLowLast"); //$NON-NLS-1$
                } else {
                    components[i].addStyleName("rowLow"); //$NON-NLS-1$
                }
            }

            return layout;
        }

        /**
         * Inicializuje vyber polozky. Pokud id neodpovida zadne polozce, vybere
         * se prvni v seznamu
         */
        public static void initSelection(AbstractSelect component, Object id) {
            component.setValue(id);
            if (component.getValue() == null) {
                Iterator<?> iterator = component.getItemIds().iterator();
                if (iterator.hasNext()) {
                    component.setValue(iterator.next());
                }
            }
        }

        /** Vytvori a inicializuje komponentu NativeSelect */
        public static <T> void initNativeSelect(NativeSelect select, List<T> items) {
            initNativeSelect(select, items, false);
        }

        /** Vytvori a inicializuje komponentu NativeSelect */
        public static <T> void initNativeSelect(NativeSelect select, List<T> items, Boolean useUniqueId) {
            select.setNullSelectionAllowed(false);
            select.removeAllItems();
            if (items != null) {
                Object id;
                for (int i = 0; i < items.size(); ++i) {
                    id = useUniqueId ? ((Unique) items.get(i)).getId() : items.get(i);
                    select.addItem(id);
                    select.setItemCaption(id, items.get(i).toString());
                }
            }
        }

        /** Nastavi komponentu TextField pro editaci celych cisel */
        public static void textFieldSetInt(TextField textField, int value) {
            final ObjectProperty<Integer> property = new ObjectProperty<Integer>(value);
            textField.setConverter(new StringToIntegerConverter());
            textField.setPropertyDataSource(property);
        }

        /** Z komponenty TextField vraci zadavane cele cislo */
        public static int textFieldGetInt(TextField textField) {
            return ((Integer) textField.getPropertyDataSource().getValue()).intValue();
        }

        /**
         * Do komponenty "Image" vlozi obrazek. Pokud je obrazek null, pouzije
         * se implicitni obrazek.
         *
         * @param image Komponenta pro zobrazeni obrazku
         * @param photoData Data obrazku
         * @param photoFile cesta k souboru, kde jsou data obrazku ulozena
         */
        @SuppressWarnings("serial")
        public static void fillImageByPortrait(Image image, final byte[] photoData, String photoFile) {
            image.setSource(null);

            if (photoData != null) {
                StreamSource imageSrc = new StreamSource() {

                    @Override
                    public InputStream getStream() {
                        return new ByteArrayInputStream(photoData);
                    }
                };
                image.setSource(new StreamResource(imageSrc, photoFile));
            } else {
                image.setSource(Tools.IconId.IMG_EMPTY_PORTRAIT.image);
            }

        }

    }

    public static class Strings {

        public static String getErrorString(String caption, String error) {
            if (caption != null) {
                return String.format("%s: %s", caption, error); //$NON-NLS-1$
            } else {
                return error;
            }
        }

        public static String getEmptyValueErrorString(String caption) {
            return getErrorString(caption, Messages.getString("emptyValue")); //$NON-NLS-1$
        }

        public static String getCheckString(boolean checked) {
            return checked ? "✔" : "✘"; //$NON-NLS-1$ //$NON-NLS-2$
        }

        public static String getStrHomeGame(boolean homeGame) {
            return homeGame ? Messages.getString("home") : Messages.getString("foreign"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        /** Sestavi popis tymu hrajici zapas */
        public static String getStrGameTeams(TeamMatch game) {
            if ((game.getClubTeam() != null) && (game.getClubRival() != null)) {
                String team = game.getClubTeam().getName();
                String club = game.getClubRivalTitle();
                return game.getHomeCourt() ? team + " - " + club : club + " - " + team; //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                return ""; //$NON-NLS-1$
            }
        }

        /** Zapise cas a misto treninku ve formtu HTML */
        public static String getHtmlTraining(TeamTraining training) {
            return String.format("<span>%s</span></br><span>%s</span>", DateTime.eventToString(training, //$NON-NLS-1$
                    DateTime.DateStyle.DAY_AND_TIME, DateTime.DateStyle.DAY_AND_TIME), training.getPlace());
        }

        /** Zapise cas a misto zapasu ve formtu HTML */
        public static String getHtmlGame(TeamMatch game) {
            return String.format("<strong>%s</strong></br>%s", getStrGameTeams(game), //$NON-NLS-1$
                    DateTime.dateToString(game.getStart(), DateTime.DateStyle.DAY_AND_TIME));
        }

        /**
         * Spoji seznam textu do jednoho textu
         *
         * @param text pole textu, ktere budou spojene do jednoho textu
         * @param separator oddelovac jednotlivych slov
         */
        public static String concatenateText(String[] text, String separator) {
            StringBuilder builder = new StringBuilder();

            for (String item : text) {
                if (!"".equals(item)) { //$NON-NLS-1$
                    if (builder.length() > 0) {
                        builder.append(separator);
                    }
                    builder.append(item);
                }
            }

            return builder.toString();
        }

        /** Vraci identifikator predany jako jediny parametr */
        public static int analyzeParameters(ViewChangeEvent event) {
            try {
                return Integer.parseInt(event.getParameters());
            } catch (Exception e) {
                return 0;
            }
        }
    }

    public static class Validators {

        public static void setRequired(AbstractField<?> field, boolean required, String requiredError) {
            if (required) {
                field.setRequired(required);
                field.setRequiredError(requiredError != null ? requiredError : Strings.getEmptyValueErrorString(field
                        .getCaption()));
                field.setImmediate(true);
            }
        }

    }
}
