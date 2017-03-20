package Presentation.handlerSoftwareArchitectureSpecification;

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
public class SoftwareArchitectureSpecificationNewtHandler extends AbstractHandler {

	/**
	 * The constructor.
	 */
	public SoftwareArchitectureSpecificationNewtHandler() {

	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) {
		PreferenceDialog pref = PreferencesUtil.createPreferenceDialogOn(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				//"Presentation.preferenceSoftwareArchitectureSpecification.SoftwareArchitectureSpecificationManagementPreferencePage",
				"Presentation.preferenceSoftwareArchitectureSpecification.NewSoftwareArchitectureSpecificationPreferencePage",
				new String[] {
						//"Presentation.preferenceSoftwareArchitectureSpecification.SoftwareArchitectureSpecificationManagementPreferencePage" },
						"Presentation.preferenceSoftwareArchitectureSpecification.NewSoftwareArchitectureSpecificationManagementPreferencePage" },
				null);
		if (pref != null)
			pref.open();
		return null;
	}

}
