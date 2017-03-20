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
import Presentation.preferenceSoftwareArchitectureSpecification.NewSoftwareArchitectureSpecificationPreferencePage;
import Presentation.preferences.Messages;

/**
 * Controller for EditQualityRequirementPreferencePage
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 *
 */
public class ManagementSoftwareArchitectureSpecificationPPController extends Controller {
	/**
	 * Attributes
	 */
	private SoftwareArchitectureSpecificationManager manager;
	private NewSoftwareArchitectureSpecificationPreferencePage form;
	private static ManagementSoftwareArchitectureSpecificationPPController viewController;

	private ManagementSoftwareArchitectureSpecificationPPController() {
		super();
		manager = this.getManager();
	}

	/**
	 * Getters and Setters
	 */
	public static ManagementSoftwareArchitectureSpecificationPPController getViewController() {
		if (viewController == null) {
			synchronized (ManagementSoftwareArchitectureSpecificationPPController.class) {
				viewController = new ManagementSoftwareArchitectureSpecificationPPController();
			}
		}
		return viewController;
	}

	public static void setViewController(ManagementSoftwareArchitectureSpecificationPPController viewController) {
		ManagementSoftwareArchitectureSpecificationPPController.viewController = viewController;
	}

	public SoftwareArchitectureSpecificationManager getManager() {
		return SoftwareArchitectureSpecificationManager.getManager();
	}

	public void setManager(SoftwareArchitectureSpecificationManager manager) {
		this.manager = manager;
	}

	public NewSoftwareArchitectureSpecificationPreferencePage getForm() {
		return form;
	}

	public void setForm(NewSoftwareArchitectureSpecificationPreferencePage form) {
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
	 * Validate the necessary data for the update of the architecture
	 * 
	 * @return boolean (is true if they have completed the required fields)
	 */
	private boolean isValidData() {
		if (this.isEmpty(this.getForm().getCmbSystem())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectSystem_ErrorDialog"));
			this.getForm().getCmbSystem().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getForm().getTxtName()) || this.isEmpty(this.getForm().getTxtPath())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyName_ErrorDialog"));
			this.getForm().getTxtName().setFocus();
			return false;
		} else if (this.isEmpty(this.getForm().getCmbUnit())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectUnit_ErrorDialog"));
			this.getForm().getCmbUnit().getCombo().setFocus();
			return false;
		} 
		return true;
	}

	/**
	 * Configure the view when a table's item is selected
	 */
	public void getView() {
		this.getForm().loadCmbUnit();

		this.getForm().getTxtName().setStringValue(this.getManager().getName());
		this.getForm().getTxtPath().setStringValue(this.getManager().getPath());
		this.getForm().getCmbUnit()
				.setSelection(new StructuredSelection(this.setUnit(this.getManager().getArchitecture())));

	}

	public Boolean isConnection() {
		return this.getManager().isConnection();
	}

	public boolean existSystemTrue() {
		return this.getManager().existSystemTrue();
	}

	public void setModelPaths(DomainModel.AnalysisEntity.System ptype) {
		this.getManager().setSystem(ptype);
		while (this.getForm().getTable().getItems().length > 0) {
			this.getForm().getTable().remove(0);
		}
		if (!this.getManager().getArchitectures().isEmpty()) {
			for (Architecture arc : this.getManager().getArchitectures()) {
				addToTable(arc.getPathUCM(), arc);
			}
		}
	}

	/**
	 * set unit to table
	 * 
	 * @param architecture
	 */
	private Unit setUnit(Architecture architecture) {
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
						return spePar.getUnit();
					}
				}
			}
		}
		return null;
	}

	public String addToTable(String architecture, Architecture arc) {
		// String result = this.getManager().chequerUCM(architecture);
		// if (result.equals("")) {
		TableItem item = new TableItem(this.getForm().getTable(), SWT.NONE);
		item.setData(arc);
		item.setText(new String[] { architecture, architecture.substring(architecture.lastIndexOf("\\") + 1),
				architecture.substring(0, architecture.lastIndexOf("\\")), this.setUnit(arc).toString() });
		return "";

		// } else {
		// return result;
		// }

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

	public String addToCmb(String namePath) {
		String result = this.getManager().chequerUCM(namePath);
		if (result.equals("")) {
			this.getForm().getTxtName().setStringValue(namePath.substring(namePath.lastIndexOf("\\") + 1));
			this.getForm().getTxtPath().setStringValue(namePath.substring(0, namePath.lastIndexOf("\\")));
			return "";

		} else {
			return result;
		}
	}

	/**
	 * Sets the manager's architecture
	 * 
	 * @param pmodel
	 */
	public void setArchitecture(Architecture pmodel) {
		this.getManager().setArchitecture(pmodel);
	}

	/**
	 * Update the system with the UCM path and prepare the view
	 */
	public Boolean save() {
		int err;
		err = this.setSystem();
		if (err == 0) {
			this.getForm().clearSpecification();
			this.getForm().fillTable();
			this.getForm().prepareView(1);
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
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Update architectures to system
	 */
	private void setArchitecturesToSystem() {

		// Add news architectures
		for (int i = 0; i < this.getForm().getTable().getItemCount(); i++) {
			TableItem item = this.getForm().getTable().getItem(i);
			if (!item.getText(2).equals("")) {
				boolean isNotAdd = false;
				for (Architecture arc : this.getManager().getArchitectures()) {
					if (arc.getPathUCM().equals(this.getForm().getTxtPath().getStringValue() + "\\"
							+ this.getForm().getTxtName().getStringValue())) {
						isNotAdd = true;
					}
				}
				if (!isNotAdd) {
					Architecture architecture = new Architecture(this.getForm().getTxtPath().getStringValue() + "\\"
							+ this.getForm().getTxtName().getStringValue());
					this.getManager().createArchitecture(architecture);
					updateUnitsToArchitecture(architecture);
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

}
