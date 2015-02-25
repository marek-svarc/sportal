package com.clubeek.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	/* PUBLIC */

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/* PRIVATE */
	
	private Messages() {
	}
	
	private static final String BUNDLE_NAME = "com.clubeek.ui.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
}
