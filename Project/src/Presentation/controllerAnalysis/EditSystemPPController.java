package Presentation.controllerAnalysis;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;

import BusinessLogic.AnalysisManager;
import Presentation.Controller;
import Presentation.preferenceAnalysis.EditSystemPreferencePage;
import Presentation.preferences.Messages;

/**
 * Controller for EditSystemPreferencePage
 * 
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 *
 */
public class EditSystemPPController extends Controller {

	/**
	 * Attributes
	 */
	private AnalysisManager manager;
	private EditSystemPreferencePage form;
	private static EditSystemPPController viewController;

	private EditSystemPPController() {
		super();
	}

	/**
	 * Getters and Setters
	 */
	public static EditSystemPPController getViewController() {
		if (viewController == null) {
			synchronized (EditSystemPPController.class) {
				viewController = new EditSystemPPController();
			}
		}
		return viewController;
	}
	
	public static void setViewController(EditSystemPPController viewController) {
		EditSystemPPController.viewController = viewController;
	}

	public AnalysisManager getManager() {
		return AnalysisManager.getManager();
	}

	public void setManager(AnalysisManager manager) {
		this.manager = manager;
	}

	public EditSystemPreferencePage getForm() {
		return form;
	}

	public void setForm(EditSystemPreferencePage form) {
		this.form = form;
	}

	/**
	 * Update the system and prepare the view
	 */
	public int save() {
		int err;
		err = this.setSystem();
		if (err == 0) {
			this.getForm().prepareView(2);
			if (this.getManager().updateSystem()) {
				return 0;
			} else {
				return 1;
			}
		}
		return 2;
	}

	/**
	 * Remove a system and prepare the view
	 */
	public void remove() {
		this.getManager().removeSystem();
		this.getForm().prepareView(0);
	}

	/**
	 * Update a system
	 * 
	 * @return int (indicates if the update was successfully)
	 */
	private int setSystem() {
		if (this.isValidData()) {
			this.getManager().setSystemName(
					((IStructuredSelection) this.getForm().getCboSystem().getSelection()).getFirstElement().toString());
			this.getManager().setProjectName(this.getForm().getProjectName().getStringValue());
			this.getManager().setStartDate(convertDateTimeToDate(this.getForm().getCalendarStartDate()));
			this.getManager().setFinishDate(convertDateTimeToDate(this.getForm().getCalendarFinishDate()));
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Configure the view when a Combo's item is selected
	 */
	public void getView() {
		this.getForm().getProjectName().setStringValue(this.getManager().getProjectName());
		this.getForm().getCalendarStartDate().setDay(getDay(this.getManager().getStartDate()));
		this.getForm().getCalendarStartDate().setMonth(getMonth(this.getManager().getStartDate()));
		this.getForm().getCalendarStartDate().setYear(getYear(this.getManager().getStartDate()));
		this.getForm().getCalendarFinishDate().setDay(getDay(this.getManager().getFinishDate()));
		this.getForm().getCalendarFinishDate().setMonth(getMonth(this.getManager().getFinishDate()));
		this.getForm().getCalendarFinishDate().setYear(getYear(this.getManager().getFinishDate()));
	}

	/**
	 * Validate the necessary data for the update of the system
	 * 
	 * @return boolean (is true if they have completed the required fields)
	 */
	private boolean isValidData() {
		if (this.isEmpty(this.getForm().getCboSystem())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptySystemName_ErrorDialog"));
			this.getForm().getCboSystem().getCombo().setFocus();
			return false;
		}
		if (this.isEmpty(this.getForm().getProjectName())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyProjectName_ErrorDialog"));
			this.getForm().getProjectName().getTextControl(this.getForm().getParent()).setFocus();
			return false;
		} else if (isAfter(this.getForm().getCalendarStartDate(), this.getForm().getCalendarFinishDate())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_CompareDate_ErrorDialog"));
			getForm().getCalendarStartDate().setFocus();
			return false;
		}

		return true;
	}

	/**
	 * Sets the model of system combo
	 */
	public void setModel() {
		this.getForm().getCboSystem().setInput(getManager().getComboModelSystem());
	}

	public void setModel(ComboViewer pcmb) {
		this.setModel(
				(DomainModel.AnalysisEntity.System) ((IStructuredSelection) pcmb.getSelection()).getFirstElement());
	}

	private void setModel(DomainModel.AnalysisEntity.System pmodel) {
		this.getManager().setSystem(pmodel);
	}

	public Boolean isConnection() {
		return this.getManager().isConnection();
	}

	public boolean existSystemTrue() {
		return this.getManager().existSystemTrue();
	}

}
