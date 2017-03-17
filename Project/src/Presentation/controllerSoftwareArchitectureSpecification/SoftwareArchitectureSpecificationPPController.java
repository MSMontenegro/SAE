package Presentation.controllerSoftwareArchitectureSpecification;

import java.io.File;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import BusinessLogic.SoftwareArchitectureSpecificationManager;
import DomainModel.AnalysisEntity.Unit;
import DomainModel.SoftwareArchitectureSpecificationEntity.Architecture;
import DomainModel.SoftwareArchitectureSpecificationEntity.Path;
import DomainModel.SoftwareArchitectureSpecificationEntity.PathElement;
import DomainModel.SoftwareArchitectureSpecificationEntity.Responsibility;
import DomainModel.SoftwareArchitectureSpecificationEntity.SpecificationParameter;
import Presentation.Controller;
import Presentation.preferenceSoftwareArchitectureSpecification.SoftwareArchitectureSpecificationManagementPreferencePage;
import Presentation.preferences.Messages;

/**
 * Controller for SoftwareArchitectureEspecificationPreferencePage
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 *
 */
public class SoftwareArchitectureSpecificationPPController extends Controller {

	/**
	 * Attributes
	 */
	private SoftwareArchitectureSpecificationManager manager;
	private SoftwareArchitectureSpecificationManagementPreferencePage form;
	private static SoftwareArchitectureSpecificationPPController viewController;

	private SoftwareArchitectureSpecificationPPController() {
		super();
	}

	/**
	 * Getters and Setters
	 */
	public static SoftwareArchitectureSpecificationPPController getViewController() {
		if (viewController == null) {
			synchronized (SoftwareArchitectureSpecificationPPController.class) {
				viewController = new SoftwareArchitectureSpecificationPPController();
			}
		}
		return viewController;
	}

	public static void setViewController(SoftwareArchitectureSpecificationPPController viewController) {
		SoftwareArchitectureSpecificationPPController.viewController = viewController;
	}

	public SoftwareArchitectureSpecificationManager getManager() {
		return SoftwareArchitectureSpecificationManager.getManager();
	}

	public void setManager(SoftwareArchitectureSpecificationManager manager) {
		this.manager = manager;
	}

	public SoftwareArchitectureSpecificationManagementPreferencePage getForm() {
		return form;
	}

	public void setForm(SoftwareArchitectureSpecificationManagementPreferencePage form) {
		this.form = form;
	}

	/**
	 * Sets the model of system combo
	 */
	public void setModel() {
		this.getForm().getCmbSystem().setInput(getManager().getComboModelSystem());
	}

	public void setModel(ComboViewer pcmb) {
		this.setModel(
				(DomainModel.AnalysisEntity.System) ((IStructuredSelection) pcmb.getSelection()).getFirstElement());
	}

	private void setModel(DomainModel.AnalysisEntity.System pmodel) {
		this.getManager().setSystem(pmodel);
	}

	/**
	 * Sets the model of unit combo
	 */
	public void setModelUnit() {
		this.getForm().getCmbUnit().setInput(getManager().getComboModelUnit());
	}

	public void setModelUnit(ComboViewer pcmb) {
		this.setModelUnit((Unit) ((IStructuredSelection) pcmb.getSelection()).getFirstElement());
	}

	private void setModelUnit(Unit pmodel) {
		this.getManager().setUnit(pmodel);
	}

	/**
	 * Update the system with the UCM path and prepare the view
	 */
	public Boolean save() {
		int err;
		err = this.setSystem();
		if (err == 0) {
			this.getForm().clearView();
			this.getForm().prepareView(0);
			return this.getManager().updateSystem();
		}
		return null;
	}

	/**
	 * Update the system with the UCM path
	 * 
	 * @return int (indicates if the UCM path was saved successfully)
	 */
	private int setSystem() {
		if (this.isValidData()) {
			setArchitecturesToSystem();
			// setUnitsToArchitectures();
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Update architectures to system
	 */
	private void setArchitecturesToSystem() {

		deleteArchitecture();

		// Add news architectures
		for (int i = 0; i < this.getForm().getTable().getItemCount(); i++) {
			TableItem item = this.getForm().getTable().getItem(i);
			if (!item.getText(2).equals("")) {
				boolean isNotAdd = false;
				for (Architecture arc : this.getManager().getArchitectures()) {
					String var = item.getText(2) + "\\" + item.getText(1);
					if (arc.getPathUCM().equals(item.getText(2) + "\\" + item.getText(1))) {
						updateUnitsToArchitecture(arc);
						isNotAdd = true;
					}
				}
				if (!isNotAdd) {
					Architecture architecture = new Architecture(item.getText(2) + "\\" + item.getText(1));
					this.getManager().createArchitecture(architecture);
					this.getManager().getArchitectures().add(architecture);
				}
			}
		}

	}

	/**
	 * update specification parameter´s unit for existing architecture
	 * 
	 * @param arc
	 */
	private void updateUnitsToArchitecture(Architecture arc) {
		Iterator itPaths = arc.getPaths().iterator();
		while (itPaths.hasNext()) {
			Path a = (Path) itPaths.next();
			Iterator itPathElements = a.getPathElements().iterator();
			while (itPathElements.hasNext()) {
				PathElement pathElement = (PathElement) itPathElements.next();
				if (pathElement.isResponsibility()) {
					Responsibility responsability = (Responsibility) pathElement;
					Iterator itSpePar = responsability.getSpecificationParameter().iterator();
					while (itSpePar.hasNext()) {
						SpecificationParameter spePar = (SpecificationParameter) itSpePar.next();
						spePar.setUnit((Unit) ((IStructuredSelection) this.getForm().getCmbUnit().getSelection())
								.getFirstElement());
					}
				}
			}
		}
	}

	/**
	 * Delete to current system, architectures
	 */
	private void deleteArchitecture() {
		Iterator it = this.getManager().getArchitectures().iterator();
		while (it.hasNext()) {
			Architecture arc = (Architecture) it.next();

			boolean isNotDelete = false;

			for (int i = 0; i < this.getForm().getTable().getItemCount(); i++) {
				TableItem item = this.getForm().getTable().getItem(i);
				if (arc.getPathUCM().equals(item.getText(2) + "\\" + item.getText(1))) {
					isNotDelete = true;
				}
			}

			if (!isNotDelete) {
				it.remove();
			}
		}
	}

	/**
	 * Validate the necessary data for save the UCM path
	 * 
	 * @return boolean (is true if they have completed the required fields)
	 */
	private boolean isValidData() {
		if (this.isEmpty(this.getForm().getCmbSystem())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectSystem_ErrorDialog"));
			this.getForm().getCmbSystem().getCombo().setFocus();
			return false;
		}
		if (this.getForm().getTable().getItemCount() > 0 && this.isEmpty(this.getForm().getCmbUnit())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectUnit_ErrorDialog"));
			this.getForm().getCmbUnit().getCombo().setFocus();
			return false;
		}
		if (this.getForm().getTable().getItemCount() == 0 && !(this.isEmpty(this.getForm().getCmbUnit()))) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_UnitSelectedAndArchitecturesEmpty_ErrorDialog"));
			this.getForm().getTable().setFocus();
			return false;
		}

		return true;
	}

	/**
	 * Sets the model table of the quality requirements of a specific system
	 * 
	 * @param ptype
	 */
	public void setModelPaths(DomainModel.AnalysisEntity.System ptype) {
		Boolean first = true;
		this.getManager().setSystem(ptype);
		while (this.getForm().getTable().getItems().length > 0) {
			this.getForm().getTable().remove(0);
		}
		if (!this.getManager().getArchitectures().isEmpty()) {
			for (Architecture arc : this.getManager().getArchitectures()) {
				if (first) {
					// Solo lo hace la primera vez, para la primera architectura
					this.setUnit(arc);
					first = false;
				}
				addToTable(arc.getPathUCM());
			}
		}
	}

	/**
	 * set unit to combo
	 * 
	 * @param architecture
	 */
	private void setUnit(Architecture architecture) {
		Iterator it = architecture.getPaths().iterator();
		Boolean isSetUnit = false;
		while (it.hasNext()) {
			Path a = (Path) it.next();
			Iterator itPathElements = a.getPathElements().iterator();
			while (itPathElements.hasNext() && !isSetUnit) {
				PathElement pathElement = (PathElement) itPathElements.next();
				if (pathElement instanceof Responsibility) {
					Responsibility responsability = (Responsibility) pathElement;
					Iterator itSpePar = responsability.getSpecificationParameter().iterator();
					if (itSpePar.hasNext()) {
						SpecificationParameter spePar = (SpecificationParameter) itSpePar.next();
						this.getForm().getCmbUnit().setSelection(new StructuredSelection(spePar.getUnit()));
						isSetUnit = true;
					}
				}
			}
		}
	}

	public String addToTable(String namePath) {
		String result = this.getManager().chequerUCM(namePath);

		if (result.equals("")) {
			TableItem item = new TableItem(this.getForm().getTable(), SWT.NONE);
			item.setData(namePath);

			item.setText(new String[] { namePath, namePath.substring(namePath.lastIndexOf("\\") + 1),
					namePath.substring(0, namePath.lastIndexOf("\\")), });
			return "";

		} else {
			return result;
		}

	}

	public void deleteToTable() {
		this.getForm().getTable().remove(this.getForm().getTable().getSelectionIndices());
		if (this.getForm().getTable().getItemCount() == 0) {
			this.getForm().loadComboUnit();
			this.getForm().prepareView(4);
		} else {
			this.getForm().prepareView(2);
		}
	}

	public boolean isUCMDuplicate(String newPath) {
		for (int i = 0; i < this.getForm().getTable().getItemCount(); i++) {
			TableItem item = this.getForm().getTable().getItem(i);
			if (newPath.equals(item.getText(2) + "\\" + item.getText(1))) {
				return true;
			}
		}
		return false;
	}

	public void openJUCMNavEditor(Composite parent, String pathUCM) {
		File file = new File(pathUCM);
		if (file.exists()) {
			IFile ifile = convert(file);
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry()
						.getDefaultEditor(ifile.getName());
				parent.getShell().close();
				page.openEditor(new FileEditorInput(ifile), desc.getId());
			} catch (Exception e1) {
				createErrorDialog(Messages.getString("UCM2DEVS_ProjectOpenEclipse_ErrorDialog"));
			}
		} else {
			createErrorDialog(Messages.getString("UCM2DEVS_UCMNotExists_ErrorDialog"));
		}
	}

	public Boolean isConnection() {
		return this.getManager().isConnection();
	}

	public boolean existSystemTrue() {
		return this.getManager().existSystemTrue();
	}

}
