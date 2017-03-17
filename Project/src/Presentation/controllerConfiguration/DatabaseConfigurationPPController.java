package Presentation.controllerConfiguration;

import BusinessLogic.SystemConfigurationManager;
import Presentation.Controller;
import Presentation.preferenceConfiguration.DatabaseConfigurationPreferencePage;
import Presentation.preferences.Messages;

/**
 * Controller for SystemConfigurationPreferencePage
 * 
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 *
 */
public class DatabaseConfigurationPPController extends Controller {

	/**
	 * Attributes
	 */
	private SystemConfigurationManager manager;
	private DatabaseConfigurationPreferencePage form;
	private static DatabaseConfigurationPPController viewController;

	private DatabaseConfigurationPPController() {
		super();
	}

	/**
	 * Getters and Setters
	 */
	public static DatabaseConfigurationPPController getViewController() {
		if (viewController == null) {
			synchronized (DatabaseConfigurationPPController.class) {
				viewController = new DatabaseConfigurationPPController();
			}
		}
		return viewController;
	}
	
	public SystemConfigurationManager getManager() {
		return SystemConfigurationManager.getManager();
	}

	public void setManager(SystemConfigurationManager manager) {
		this.manager = manager;
	}

	public DatabaseConfigurationPreferencePage getForm() {
		return form;
	}

	public void setForm(DatabaseConfigurationPreferencePage form) {
		this.form = form;
	}

	public void testConnection() {
		if (this.getManager().isConnection(this.getForm().getDatabasePassword().getStringValue(),
				this.getForm().getDatabaseUserName().getStringValue(),
				this.getForm().getDatabasePortNumber().getStringValue(),
				this.getForm().getDatabaseName().getStringValue())) {
			this.getForm().getBtnSave().setEnabled(true);
			this.createSuccessDialog(Messages.getString("UCM2DEVS_ConnectionDatabase_Dialog"));
		} else {
			this.getForm().getBtnSave().setEnabled(false);
			this.createErrorDialog(Messages.getString("UCM2DEVS_ConnectionDatabase_ErrorDialog"));
		}
	}

	/**
	 * Configure the view
	 */
	public void getView() {
		this.getForm().getDatabasePassword().setStringValue(this.getManager().getPassword());
		this.getForm().getDatabasePortNumber().setStringValue(this.getManager().getPortNumber());
		this.getForm().getDatabaseUserName().setStringValue(this.getManager().getUserName());
		this.getForm().getDatabaseName().setStringValue(this.getManager().getDatabaseName());
	}

	public void updateConnectionData() {
		if (this.getManager().updateConnectionData(this.getForm().getDatabasePassword().getStringValue(),
				this.getForm().getDatabaseUserName().getStringValue(),
				this.getForm().getDatabasePortNumber().getStringValue(),
				this.getForm().getDatabaseName().getStringValue())) {
			this.createObjectSuccessDialog();
		} else {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SaveData_ErrorDialog"));
		}
	}

}
