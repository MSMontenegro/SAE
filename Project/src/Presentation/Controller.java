package Presentation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;

import Presentation.preferences.Messages;

/**
 * Parent controller with the reusable methods
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 * @author: Florencia Rossini. E-mail: flori.rossini@gmail.com
 */
public class Controller {

	/**
	 * Validate whether a StringFieldEditor is empty
	 * 
	 * @param ptxt
	 */
	public boolean isEmpty(StringFieldEditor ptxt) {
		return ptxt.getStringValue().equals("");
	}

	/**
	 * Validate whether a Text is empty
	 * 
	 * @param ptxt
	 */
	public boolean isEmpty(Text ptxt) {
		return ptxt.getText().equals("");
	}

	/**
	 * Validate whether a JComboBox is empty
	 * 
	 * @param pcmb
	 */
	public boolean isEmpty(ComboViewer pcmb) {
		return ((IStructuredSelection) pcmb.getSelection()).getFirstElement() == null;
	}

	/**
	 * Convert a DateTime to Date
	 * 
	 * @param dt
	 * @return
	 */
	public Date convertDateTimeToDate(DateTime pdt) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.YEAR, pdt.getYear());
		cal.set(Calendar.MONTH, pdt.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, pdt.getDay());

		return cal.getTime();

	}

	/**
	 * Methods to get day, month and year of date
	 * 
	 * @param pdate
	 * @return
	 */
	public int getDay(Date pdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String dateFormat = sdf.format(pdate); // NOPMD by Usuario-Pc on
												// 10/06/16 21:36
		dateFormat = dateFormat.substring(0, 2);
		return Integer.parseInt(dateFormat);
	}

	public int getMonth(Date pdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String dateFormat = sdf.format(pdate); // NOPMD by Usuario-Pc on
												// 10/06/16 21:36
		dateFormat = dateFormat.substring(3, 5);
		return Integer.parseInt(dateFormat) - 1;
	}

	public int getYear(Date pdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String dateFormat = sdf.format(pdate); // NOPMD by Usuario-Pc on
												// 10/06/16 21:36
		dateFormat = dateFormat.substring(6, 10);
		return Integer.parseInt(dateFormat);
	}

	/**
	 * Valid if a date is before that other
	 * 
	 * @param pcs
	 * @param pcf
	 * @return boolean (indicates if finish date is before start date)
	 */
	public boolean isAfter(DateTime pcs, DateTime pcf) {
		Date dcs = convertDateTimeToDate(pcs);
		Date dcf = convertDateTimeToDate(pcf);
		return dcs.after(dcf);
	}

	/**
	 * Created a dialog for error
	 * 
	 * @param message
	 */
	public void createErrorDialog(String message) {
		MessageDialog.openError(null, Messages.getString("UCM2DEVS_Error_Label"), message);
	}

	/**
	 * Created a dialog for error for saved to object
	 * 
	 */
	public void createObjectDontSaveErrorDialog() {
		MessageDialog.openError(null, Messages.getString("UCM2DEVS_Error_Label"),
				Messages.getString("UCM2DEVS_SaveObject_ErrorDialog"));
	}

	/**
	 * Created a dialog for error for update to object
	 * 
	 */
	public void createObjectDontUpdateErrorDialog() {
		MessageDialog.openError(null, Messages.getString("UCM2DEVS_Error_Label"),
				Messages.getString("UCM2DEVS_UpdateObject_ErrorDialog"));
	}

	/**
	 * Created a dialog for system's delete
	 * 
	 * @return boolean
	 */
	public boolean createDeleteSystemDialog() {
		return MessageDialog.openQuestion(null, Messages.getString("UCM2DEVS_Delete_Label"),
				Messages.getString("UCM2DEVS_WantDeleteSystem_QuestionDialog"));
	}

	/**
	 * Created a dialog for information about success
	 * 
	 */
	public void createObjectSuccessDialog() {
		MessageDialog.openInformation(null, Messages.getString("UCM2DEVS_Info_Label"),
				Messages.getString("UCM2DEVS_SuccessObject_InfoDialog"));
	}

	/**
	 * Created a dialog for information
	 * 
	 */
	public void createSuccessDialog(String message) {
		MessageDialog.openInformation(null, Messages.getString("UCM2DEVS_Info_Label"), message);
	}

	/**
	 * Created a dialog for save changed
	 * 
	 * @return boolean
	 */
	public boolean createSaveChangedDialog() {
		return MessageDialog.openQuestion(null, Messages.getString("UCM2DEVS_Question_Label"),
				Messages.getString("UCM2DEVS_WantSaveChanges_QuestionDialog"));
	}

	/**
	 * Created a dialog for quality requirement's delete
	 * 
	 * @return boolean
	 */
	public boolean createDeleteRequirementDialog() {
		return MessageDialog.openQuestion(null, Messages.getString("UCM2DEVS_Delete_Label"),
				Messages.getString("UCM2DEVS_WantDeleteQualityRequirement_QuestionDialog"));
	}

	/**
	 * Created a dialog for specification's delete
	 * 
	 * @return boolean
	 */
	public boolean createDeleteSpecificationDialog() {
		return MessageDialog.openQuestion(null, Messages.getString("UCM2DEVS_Delete_Label"),
				Messages.getString("UCM2DEVS_WantDeleteSpecification_QuestionDialog"));
	}
	
	/**
	 * Convert a File to IFile
	 * 
	 * @param file
	 * @return IFile
	 */
	public IFile convert(File file) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath location = Path.fromOSString(file.getAbsolutePath());
		IFile ifile = workspace.getRoot().getFileForLocation(location);
		return ifile;

	}

}
