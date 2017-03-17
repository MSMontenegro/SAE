package Presentation.controllerSoftwareArchitectureEvaluation;

import java.io.IOException;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import BusinessLogic.SoftwareArchitectureEvaluationManager;
import DomainModel.AnalysisEntity.QualityRequirement;
import DomainModel.AnalysisEntity.Unit;
import DomainModel.SoftwareArchitectureSpecificationEntity.Architecture;
import Main.TransformerSimulator;
import Presentation.Controller;
import Presentation.preferenceSoftwareArchitectureEvaluation.SoftwareArchitectureEvaluationPreferencePage;
import Presentation.preferences.Messages;

/**
 * Controller for SoftwareArchitectureEspecificationPreferencePage
 * 
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 *
 */
public class SoftwareArchitectureEvaluationPPController extends Controller {

	/**
	 * Attributes
	 */
	private static SoftwareArchitectureEvaluationPPController viewController;
	private SoftwareArchitectureEvaluationManager manager;
	private SoftwareArchitectureEvaluationPreferencePage form;

	private SoftwareArchitectureEvaluationPPController() {
		super();
	}

	/**
	 * Getters and Setters
	 */
	public static SoftwareArchitectureEvaluationPPController getViewController() {
		if (viewController == null) {
			synchronized (SoftwareArchitectureEvaluationPPController.class) {
				viewController = new SoftwareArchitectureEvaluationPPController();
			}
		}
		return viewController;
	}

	public static void setViewController(SoftwareArchitectureEvaluationPPController viewController) {
		SoftwareArchitectureEvaluationPPController.viewController = viewController;
	}

	public SoftwareArchitectureEvaluationManager getManager() {
		return SoftwareArchitectureEvaluationManager.getManager();
	}

	public void setManager(SoftwareArchitectureEvaluationManager manager) {
		this.manager = manager;
	}

	public SoftwareArchitectureEvaluationPreferencePage getForm() {
		return form;
	}

	public void setForm(SoftwareArchitectureEvaluationPreferencePage form) {
		this.form = form;
	}

	/**
	 * Sets the model of system combo
	 */
	public void setModel() {
		this.getForm().getCmbSystem().setInput(getManager().getComboModelSystemWithArchitecture());
	}

	/**
	 * Sets the model of unit combo
	 */
	public void setModelUnit() {
		this.getForm().getCmbUnit().setInput(getManager().getComboModelUnit());
	}

	public void setModel(ComboViewer pcmb) {
		this.setModel(
				(DomainModel.AnalysisEntity.System) ((IStructuredSelection) pcmb.getSelection()).getFirstElement());
	}

	private void setModel(DomainModel.AnalysisEntity.System pmodel) {
		this.getManager().setSystem(pmodel);
	}

	/**
	 * Evaluate a architecture
	 * 
	 * @throws IOException
	 */
	public void evaluate() throws IOException {
		if (isValidData()) {
			this.getManager().setArchitecture((Architecture) this.getForm().getTableSoftArc()
					.getItem(this.getForm().getTableSoftArc().getSelectionIndex()).getData());

			TableItem item = this.getForm().getTableSoftArc()
					.getItem(this.getForm().getTableSoftArc().getSelectionIndex());
			String UCMpath = item.getText(2) + "\\" + item.getText(1);

			// String chequerUCMResult = this.getManager().chequerUCM(UCMpath);

			double simulationTime = this.getForm().getSimulationTime().getDoubleValue();

			String simulationTimeS = this.getForm().getSimulationTime().getStringValue();

			Unit unit = (Unit) ((IStructuredSelection) this.getForm().getCmbUnit().getSelection()).getFirstElement();

			Table table = this.getForm().getTable();

			DomainModel.AnalysisEntity.System system = (DomainModel.AnalysisEntity.System) ((IStructuredSelection) this
					.getForm().getCmbSystem().getSelection()).getFirstElement();

			switch (this.getManager().evaluate(UCMpath, simulationTime, unit, system, simulationTimeS, table)) {
			case 0:
				this.createSuccessDialog(Messages.getString("UCM2DEVS_Simulation_Dialog"));
				this.createObjectSuccessDialog();
				break;
			case 1:
				this.createErrorDialog(Messages.getString("UCM2DEVS_Simulation_ErrorDialog"));
				this.createObjectDontUpdateErrorDialog();
				break;
			case 2:
				this.createErrorDialog(Messages.getString("UCM2DEVS_Transformation_ErrorDialog"));
				this.createObjectDontUpdateErrorDialog();
				break;
			case 3:
				this.createErrorDialog(Messages.getString("UCM2DEVS_UCMNotExists_ErrorDialog"));
				this.createObjectDontUpdateErrorDialog();
				break;
			case 4:
				this.createObjectDontUpdateErrorDialog();
				break;

			}
		}
	}

	private boolean isValidData() {
		if (this.isEmpty(this.getForm().getCmbSystem())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectSystem_ErrorDialog"));
			this.getForm().getCmbSystem().getCombo().setFocus();
			return false;
		} else if (this.getForm().getTableSoftArc().getSelectionIndex() == -1) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectArchitecture_ErrorDialog"));
			return false;
		} else if (this.isNotChecked(this.getForm().getTable())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectRequirement_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getForm().getSimulationTime())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_InputSimulationTime_ErrorDialog"));
			return false;
		} else if (!this.getForm().getSimulationTime().isValid()) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_InvalidSimulationTime_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getForm().getCmbUnit())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectUnit_ErrorDialog"));
			this.getForm().getCmbUnit().getCombo().setFocus();
			return false;
		} else {
			return true;
		}
	}

	public boolean isNotChecked(Table ptable) {
		TableItem[] items = ptable.getItems();
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			if (item.getChecked()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets the model table of the software architecture of a specific system
	 * 
	 * @param ptype
	 */
	public void setModelPaths(DomainModel.AnalysisEntity.System ptype) {
		this.getManager().setSystem(ptype);
		while (this.getForm().getTableSoftArc().getItems().length > 0) {
			this.getForm().getTableSoftArc().remove(0);
		}
		if (!this.getManager().getArchitectures().isEmpty()) {
			for (Architecture dp : this.getManager().getArchitectures()) {
				this.addToTable(dp);
			}
		}
	}

	/**
	 * Sets the model table of the quality requirements of a specific system
	 * 
	 * @param ptype
	 */
	public void setModelQualityRequirement(DomainModel.AnalysisEntity.System ptype) {
		this.getManager().setSystem(ptype);
		while (this.getForm().getTable().getItems().length > 0) {
			this.getForm().getTable().remove(0);
		}
		for (QualityRequirement dp : this.getManager().getQualityRequirements()) {
			if (dp.isState()) {
				TableItem item = new TableItem(this.getForm().getTable(), SWT.NONE);
				item.setData(dp);
				item.setText(new String[] { "", dp.toString(), dp.getQualityScenario().getQualityAttribute().toString(),
						dp.getQualityScenario().getDescription().toString() });
			}
		}
	}

	private void addToTable(Architecture arch) {
		TableItem item = new TableItem(this.getForm().getTableSoftArc(), SWT.NONE);
		item.setData(arch);

		item.setText(
				new String[] { arch.toString(), arch.getPathUCM().substring(arch.getPathUCM().lastIndexOf("\\") + 1),
						arch.getPathUCM().substring(0, arch.getPathUCM().lastIndexOf("\\")), });
	}

	public Boolean isConnection() {
		return this.getManager().isConnection();
	}

	public void setModelArchitecture(Architecture pdata) {
		this.getManager().setArchitecture(pdata);
	}

	public boolean existSystemTrueWithArchitecture() {
		return this.getManager().existSystemTrueWithArchitecture();
	}

	public boolean existSystemTrueWithQualityRequirementTrue() {
		return this.getManager().existSystemTrueWithQualityRequirementTrue();
	}

}