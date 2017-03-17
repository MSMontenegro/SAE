package Presentation.handlerSoftwareArchitectureSpecification;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SoftwareArchitectureSpecificationOpenHandler extends AbstractHandler {

	/**
	 * The constructor.
	 */
	public SoftwareArchitectureSpecificationOpenHandler() {

	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) {
		// Open a FileDialog that show only jucm file
		FileDialog chooseFile = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				SWT.OPEN);
		chooseFile.setFilterNames(new String[] { "Jucm Files" });
		chooseFile.setFilterExtensions(new String[] { "*.jucm" });
		String filePath = chooseFile.open();
		if (filePath != null) {
			// With de path get, open jUCMNav
			File file = new File(filePath);
			IFile ifile = convert(file);
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry()
						.getDefaultEditor(ifile.getName());
				page.openEditor(new FileEditorInput(ifile), desc.getId());
			} catch (Exception e1) {
				MessageDialog.openError(null, "Error", "There must be a project in Eclipse to open a file");
			}
		}
		return null;
	}

	private IFile convert(File file) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath location = Path.fromOSString(file.getAbsolutePath());
		IFile ifile = workspace.getRoot().getFileForLocation(location);
		return ifile;

	}

}
