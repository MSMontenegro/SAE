package Presentation.preferences;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "Presentation.preferences.messages"; //$NON-NLS-1$
	
	 private static ResourceBundle RESOURCE_BUNDLE;
	    static {
	        RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	    }

	    private Messages() {
	    }

	    public static String getString(String key) {
	        try {
	            return RESOURCE_BUNDLE.getString(key);
	        } catch (MissingResourceException e) {
	            return e.toString();
	        }
	    }
}
