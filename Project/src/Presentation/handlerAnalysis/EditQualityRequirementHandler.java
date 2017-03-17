package Presentation.handlerAnalysis;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class EditQualityRequirementHandler extends AbstractHandler {

	/**
	 * The constructor.
	 */
	public EditQualityRequirementHandler() {

	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) {
		PreferenceDialog pref = PreferencesUtil.createPreferenceDialogOn(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Presentation.preferenceAnalysis.EditQualityRequirementPreferencePage",
				new String[] { "Presentation.preferenceAnalysis.EditQualityRequirementPreferencePage" }, null);
		if (pref != null)
			pref.open();
		return null;
	}

}
