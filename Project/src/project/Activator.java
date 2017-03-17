package project;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import Configuration.DatabaseConnection;
import Configuration.Installation;
import DataManager.HibernateUtil;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "Project"; // NOPMD by Usuario-Pc on
														// 11/06/16 12:41

	// The shared instance
	private static Activator plugin; // NOPMD by Usuario-Pc on 11/06/16 12:39

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception { // NOPMD by
																// Usuario-Pc on
																// 11/06/16
																// 12:38


		/**
		 * Create installation folder if not exists
		 */
		Installation inst = new Installation();
		inst.exists();
		/**
		 * Initialize Hibernate
		 */
		HibernateUtil.initialize(new DatabaseConnection());
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	public void stop(BundleContext context) throws Exception { // NOPMD by
																// Usuario-Pc on
																// 11/06/16
																// 12:38
		plugin = null; // NOPMD by Usuario-Pc on 11/06/16 12:40
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) { // NOPMD by										// 12:39
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
