package Presentation.controllerAnalysis;

import BusinessLogic.AnalysisManager;
import Presentation.Controller;
import Presentation.preferenceAnalysis.NewSystemPreferencePage;
import Presentation.preferences.Messages;

/**
 * Controller for NewSystemPreferencePage
 * 
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 *
 */
public class NewSystemPPController extends Controller {

	/**
	 * Attributes
	 */
	private AnalysisManager manager;
	private NewSystemPreferencePage form;
	private static NewSystemPPController viewController;

	private NewSystemPPController() {
		super();
	}

	/**
	 * Getters and Setters
	 */
	public static NewSystemPPController getViewController() {
		if (viewController == null) {
			synchronized (NewSystemPPController.class) {
				viewController = new NewSystemPPController();
			}
		}
		return viewController;
	}

	public static void setViewController(NewSystemPPController viewController) {
		NewSystemPPController.viewController = viewController;
	}

	public AnalysisManager getManager() {
		return AnalysisManager.getManager();
	}

	public void setManager(AnalysisManager manager) {
		this.manager = manager;
	}

	public NewSystemPreferencePage getForm() {
		return form;
	}

	public void setForm(NewSystemPreferencePage form) {
		this.form = form;
	}

	/**
	 * Create the system and prepare the view
	 */
	public int save() {
		int err;
		err = this.newSystem();
		if (err == 0) {
			this.getForm().prepareView(1);
			if (this.getManager().saveSystem()) {
				return 0;
			} else {
				return 1;
			}
		}
		return 2;
	}

	/**
	 * Create a new system
	 * 
	 * @return int (indicates if the system was created successfully)
	 */
	private int newSystem() {
		if (this.isValidData()) {
			this.getManager().newSystem(this.getForm().getSystemName().getStringValue(),
					this.getForm().getProjectName().getStringValue(),
					convertDateTimeToDate(this.getForm().getCalendarStartDate()),
					convertDateTimeToDate(this.getForm().getCalendarFinishDate()), true);
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Validate the necessary data for the creation of the system
	 * 
	 * @return boolean (is true if they have completed the required fields)
	 */
	private boolean isValidData() {
		if (this.isEmpty(this.getForm().getSystemName())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptySystemName_ErrorDialog"));
			return false;
		}
		if (this.isEmpty(this.getForm().getProjectName())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyProjectName_ErrorDialog"));
			return false;
		} else if (isAfter(this.getForm().getCalendarStartDate(), this.getForm().getCalendarFinishDate())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_CompareDate_ErrorDialog"));
			getForm().getCalendarStartDate().setFocus();
			return false;
		}

		return true;
	}

	public Boolean isConnection() {
		return this.getManager().isConnection();
	}

}
