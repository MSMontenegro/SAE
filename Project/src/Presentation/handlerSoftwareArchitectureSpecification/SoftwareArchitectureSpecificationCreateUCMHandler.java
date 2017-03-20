package Presentation.handlerSoftwareArchitectureSpecification;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SoftwareArchitectureSpecificationCreateUCMHandler extends AbstractHandler {

	/**
	 * The constructor.
	 */
	public SoftwareArchitectureSpecificationCreateUCMHandler() {

	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) {
		IWizard wizard = new seg.jUCMNav.views.wizards.NewUcmFileWizard();
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.open();
		return null;
	}

}
