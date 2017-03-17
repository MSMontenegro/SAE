package Presentation.controllerAnalysis;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

import BusinessLogic.AnalysisManager;
import DomainModel.AnalysisEntity.ArtifactType;
import DomainModel.AnalysisEntity.EnvironmentType;
import DomainModel.AnalysisEntity.Metric;
import DomainModel.AnalysisEntity.QualityAttribute;
import DomainModel.AnalysisEntity.QualityRequirement;
import DomainModel.AnalysisEntity.ResponseMeasureType;
import DomainModel.AnalysisEntity.ResponseType;
import DomainModel.AnalysisEntity.StimulusSourceType;
import DomainModel.AnalysisEntity.StimulusType;
import DomainModel.AnalysisEntity.Unit;
import Presentation.Controller;
import Presentation.preferenceAnalysis.EditQualityRequirementPreferencePage;
import Presentation.preferences.Messages;

/**
 * Controller for EditQualityRequirementPreferencePage
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 *
 */
public class EditQualityRequirementPPController extends Controller {
	/**
	 * Attributes
	 */
	private AnalysisManager manager;
	private EditQualityRequirementPreferencePage formSearch;
	private static EditQualityRequirementPPController viewController;

	private EditQualityRequirementPPController() {
		super();
	}

	/**
	 * Getters and Setters
	 */
	public static EditQualityRequirementPPController getViewController() {
		if (viewController == null) {
			synchronized (EditQualityRequirementPPController.class) {
				viewController = new EditQualityRequirementPPController();
			}
		}
		return viewController;
	}
	
	public static void setViewController(EditQualityRequirementPPController viewController) {
		EditQualityRequirementPPController.viewController = viewController;
	}

	public AnalysisManager getManager() {
		return AnalysisManager.getManager();
	}

	public void setManager(AnalysisManager manager) {
		this.manager = manager;
	}

	public EditQualityRequirementPreferencePage getFormSearch() {
		return formSearch;
	}

	public void setFormSearch(EditQualityRequirementPreferencePage formSearch) {
		this.formSearch = formSearch;
	}

	/**
	 * Sets the model of system combo
	 */
	public void setModelSystemSearch() {
		this.getFormSearch().getCmbSystem().setInput(getManager().getComboModelSystemWithRequirements());
	}

	/**
	 * Sets the model of quality attribute combo
	 */
	public void setModelQualityAttribute() {
		this.getFormSearch().getCmbQualityAttribute().setInput(getManager().getComboModelQualityAttribute());
	}

	/**
	 * Sets the model of stimulus source type combo for a specific quality
	 * attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelStimulusSourceTypes(QualityAttribute qualityAttribute) {
		this.getFormSearch().getCmbTypeStimulusSource()
				.setInput(getManager().getComboModelStimulusSourceType(qualityAttribute));
	}

	/**
	 * Sets the model of stimulus type combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelStimulusTypes(QualityAttribute qualityAttribute) {
		this.getFormSearch().getCmbTypeStimulus().setInput(getManager().getComboModelStimulusType(qualityAttribute));
	}

	/**
	 * Sets the model of environment type combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelEnvironmentTypes(QualityAttribute qualityAttribute) {
		this.getFormSearch().getCmbTypeEnvironment()
				.setInput(getManager().getComboModelEnvironmentType(qualityAttribute));
	}

	/**
	 * Sets the model of artifact type combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelArtifactTypes(QualityAttribute qualityAttribute) {
		this.getFormSearch().getCmbTypeArtifact().setInput(getManager().getComboModelArtifactType(qualityAttribute));
	}

	/**
	 * Sets the model of response type combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelResponseTypes(QualityAttribute qualityAttribute) {
		this.getFormSearch().getCmbTypeResponse().setInput(getManager().getComboModelResponseType(qualityAttribute));
	}

	/**
	 * Sets the model of response measure type combo for a specific quality
	 * attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelResponseMeasureTypes(QualityAttribute qualityAttribute) {
		this.getFormSearch().getCmbTypeResponseMeasure()
				.setInput(getManager().getComboModelResponseMeasureType(qualityAttribute));
	}

	/**
	 * Sets the model of metric combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelMetric(ResponseMeasureType type) {
		this.getFormSearch().getCmbMetric().setInput(getManager().getComboModelMetric(type));
	}

	/**
	 * Sets the model of unit combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelUnit(Metric type) {
		this.getFormSearch().getCmbUnit().setInput(getManager().getComboModelUnit(type));
	}

	/**
	 * Update the quality requirement and prepare the view
	 */
	public int save() {
		int err;
		err = this.setQualityRequirement();
		if (err == 0) {
			this.getFormSearch().prepareView(7);
			this.getFormSearch().fillTable();
			if (this.getManager().updateQualityRequirement()) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 2;
		}
	}

	/**
	 * Remove a quality requirement and prepare the view
	 */
	public void remove() {
		this.getManager().removeQualityRequirement();
		this.getFormSearch().prepareView(7);
		this.getFormSearch().fillTable();
	}

	/**
	 * Update a quality requirement
	 * 
	 * @return int (indicates if the quality requirement was updated
	 *         successfully)
	 */
	private int setQualityRequirement() {
		if (this.isValidData()) {
			this.getManager().setDescriptionScenario(this.getFormSearch().getTxtDescription().getText());
			this.getManager().setStimulusSource(this.getFormSearch().getTxtDescriptionStimulusSource().getText(),
					this.getFormSearch().getTxtValueStimulusSource().getStringValue(),
					(StimulusSourceType) ((IStructuredSelection) this.getFormSearch().getCmbTypeStimulusSource()
							.getSelection()).getFirstElement());
			this.getManager().setStimulus(this.getFormSearch().getTxtDescriptionStimulus().getText(),
					this.getFormSearch().getTxtValueStimulus().getStringValue(),
					(StimulusType) ((IStructuredSelection) this.getFormSearch().getCmbTypeStimulus().getSelection())
							.getFirstElement());
			this.getManager().setEnvironment(this.getFormSearch().getTxtDescriptionEnvironment().getText(),
					this.getFormSearch().getTxtValueEnvironment().getStringValue(),
					(EnvironmentType) ((IStructuredSelection) this.getFormSearch().getCmbTypeEnvironment()
							.getSelection()).getFirstElement());
			this.getManager().setArtifact(this.getFormSearch().getTxtDescriptionArtifact().getText(),
					(ArtifactType) ((IStructuredSelection) this.getFormSearch().getCmbTypeArtifact().getSelection())
							.getFirstElement());
			this.getManager().setResponse(this.getFormSearch().getTxtDescriptionResponse().getText(),
					this.getFormSearch().getTxtValueResponse().getStringValue(),
					(ResponseType) ((IStructuredSelection) this.getFormSearch().getCmbTypeResponse().getSelection())
							.getFirstElement());
			this.getManager().setResponseMeasure(this.getFormSearch().getTxtDescriptionResponseMeasure().getText(),
					this.getFormSearch().getTxtValueResponseMeasure().getDoubleValue(),
					(ResponseMeasureType) ((IStructuredSelection) this.getFormSearch().getCmbTypeResponseMeasure()
							.getSelection()).getFirstElement(),
					(Metric) ((IStructuredSelection) this.getFormSearch().getCmbMetric().getSelection())
							.getFirstElement(),
					(Unit) ((IStructuredSelection) this.getFormSearch().getCmbUnit().getSelection()).getFirstElement());
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Validate the necessary data for the update of the quality requirement
	 * 
	 * @return boolean (is true if they have completed the required fields)
	 */
	private boolean isValidData() {
		Metric m = (Metric) ((IStructuredSelection) this.getFormSearch().getCmbMetric().getSelection())
				.getFirstElement();
		if (this.isEmpty(this.getFormSearch().getCmbSystem())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectSystem_ErrorDialog"));
			this.getFormSearch().getCmbSystem().getCombo().setFocus();
			return false;
		}
		if (this.isEmpty(this.getFormSearch().getTxtDescription())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyDescription_ErrorDialog"));
			this.getFormSearch().getTxtDescription().setFocus();
			return false;
		}
		if (this.isEmpty(this.getFormSearch().getCmbQualityAttribute())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectQualityAttribute_ErrorDialog"));
			this.getFormSearch().getCmbQualityAttribute().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getFormSearch().getTxtValueStimulusSource())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyStimulusSourceValue_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getFormSearch().getTxtValueStimulus())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyStimulusValue_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getFormSearch().getTxtValueEnvironment())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyEnvironmentValue_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getFormSearch().getTxtValueResponse())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyResponseValue_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getFormSearch().getCmbTypeStimulusSource())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectStimulusSourceType_ErrorDialog"));
			this.getFormSearch().getCmbTypeStimulusSource().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getFormSearch().getCmbTypeStimulus())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectStimulusType_ErrorDialog"));
			this.getFormSearch().getCmbTypeStimulus().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getFormSearch().getCmbTypeEnvironment())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectEnvironmentType_ErrorDialog"));
			this.getFormSearch().getCmbTypeEnvironment().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getFormSearch().getCmbTypeArtifact())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectArtifactType_ErrorDialog"));
			this.getFormSearch().getCmbTypeArtifact().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getFormSearch().getCmbTypeResponse())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectResponseType_ErrorDialog"));
			this.getFormSearch().getCmbTypeResponse().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getFormSearch().getCmbTypeResponseMeasure())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectResponseMeasureType_ErrorDialog"));
			this.getFormSearch().getCmbTypeResponseMeasure().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getFormSearch().getCmbMetric())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectMetric_ErrorDialog"));
			this.getFormSearch().getCmbMetric().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getFormSearch().getTxtValueResponseMeasure())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyResponseMeasureValue_ErrorDialog"));
			return false;
		} else if (!this.getFormSearch().getTxtValueResponseMeasure().isValid()) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_InvalidResponseMeasureValue_ErrorDialog"));
			return false;
		} else if (this.getFormSearch().getTxtValueResponseMeasure().getDoubleValue() <= 0) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_InvalidResponseMeasureValueNegative_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getFormSearch().getCmbUnit()) && !m.getName().equals("Number of failures")) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectUnit_ErrorDialog"));
			this.getFormSearch().getCmbUnit().getCombo().setFocus();
			return false;
		}

		return true;
	}

	/**
	 * Sets the model table of the quality requirements of a specific system
	 * 
	 * @param ptype
	 */
	public void setModelQualityRequirement(DomainModel.AnalysisEntity.System ptype) { // NOPMD
																						// by
																						// Usuario-Pc
																						// on
																						// 10/06/16
																						// 21:46
		this.getManager().setSystem(ptype);
		while (this.getFormSearch().getTable().getItems().length > 0) {
			this.getFormSearch().getTable().remove(0);
		}
		for (QualityRequirement dp : this.getManager().getQualityRequirements()) {
			if (dp.isState()) {
				TableItem item = new TableItem(this.getFormSearch().getTable(), SWT.NONE);
				item.setData(dp);
				item.setText(new String[] { dp.toString(), dp.getQualityScenario().getQualityAttribute().toString(),
						dp.getQualityScenario().getDescription().toString() });
			}
		}
	}

	/**
	 * Sets the manager's model (its quality requirement)
	 * 
	 * @param pmodel
	 */
	public void setModel(QualityRequirement pmodel) {
		this.getManager().setQualityRequirement(pmodel);
	}

	/**
	 * Configure the view when a table's item is selected
	 */
	public void getView() {
		this.getFormSearch().loadCmbQualityAttribute();

		this.getFormSearch().loadGenericScenario(this.getManager().getQualityAttribute());
		this.getFormSearch().loadCmbMetric(this.getManager().getTypeResponseMeasure());
		this.getFormSearch().loadCmbUnit(this.getManager().getMetric());

		this.getFormSearch().getTxtDescription().setText(this.getManager().getDescriptionScenario());
		this.getFormSearch().getCmbQualityAttribute()
				.setSelection(new StructuredSelection(this.getManager().getQualityAttribute()));

		this.getFormSearch().getTxtDescriptionStimulusSource()
				.setText(this.getManager().getDescriptionStimulusSource());
		this.getFormSearch().getCmbTypeStimulusSource()
				.setSelection(new StructuredSelection(this.getManager().getTypeStimulusSource()));
		this.getFormSearch().getTxtValueStimulusSource().setStringValue(this.getManager().getValueStimulusSource());

		this.getFormSearch().getTxtDescriptionStimulus().setText(this.getManager().getDescriptionStimulus());
		this.getFormSearch().getCmbTypeStimulus()
				.setSelection(new StructuredSelection(this.getManager().getTypeStimulus()));
		this.getFormSearch().getTxtValueStimulus().setStringValue(this.getManager().getValueStimulus());

		this.getFormSearch().getTxtDescriptionEnvironment().setText(this.getManager().getDescriptionEnvironment());
		this.getFormSearch().getCmbTypeEnvironment()
				.setSelection(new StructuredSelection(this.getManager().getTypeEnvironment()));
		this.getFormSearch().getTxtValueEnvironment().setStringValue(this.getManager().getValueEnvironment());

		this.getFormSearch().getTxtDescriptionArtifact().setText(this.getManager().getDescriptionArtifact());
		this.getFormSearch().getCmbTypeArtifact()
				.setSelection(new StructuredSelection(this.getManager().getTypeArtifact()));

		this.getFormSearch().getTxtDescriptionResponse().setText(this.getManager().getDescriptionResponse());
		this.getFormSearch().getCmbTypeResponse()
				.setSelection(new StructuredSelection(this.getManager().getTypeResponse()));
		this.getFormSearch().getTxtValueResponse().setStringValue(this.getManager().getValueResponse());

		this.getFormSearch().getTxtDescriptionResponseMeasure()
				.setText(this.getManager().getDescriptionResponseMeasure());
		this.getFormSearch().getCmbTypeResponseMeasure()
				.setSelection(new StructuredSelection(this.getManager().getTypeResponseMeasure()));
		this.getFormSearch().getCmbMetric().setSelection(new StructuredSelection(this.getManager().getMetric()));
		this.getFormSearch().getTxtValueResponseMeasure().setStringValue(this.getManager().getValueResponseMeasure());
		if (!this.getManager().getMetric().getName().equals("Number of failures")) {
			this.getFormSearch().getCmbUnit().setSelection(new StructuredSelection(this.getManager().getUnit()));
		}
	}

	public Boolean isConnection() {
		return this.getManager().isConnection();
	}

	public boolean existSystemTrueWithQualityRequirementTrue() {
		return this.getManager().existSystemTrueWithQualityRequirementTrue();
	}
}
