package Presentation.controllerAnalysis;

import org.eclipse.jface.viewers.IStructuredSelection;

import BusinessLogic.AnalysisManager;
import DomainModel.AnalysisEntity.Artifact;
import DomainModel.AnalysisEntity.ArtifactType;
import DomainModel.AnalysisEntity.Environment;
import DomainModel.AnalysisEntity.EnvironmentType;
import DomainModel.AnalysisEntity.Metric;
import DomainModel.AnalysisEntity.QualityAttribute;
import DomainModel.AnalysisEntity.Response;
import DomainModel.AnalysisEntity.ResponseMeasure;
import DomainModel.AnalysisEntity.ResponseMeasureType;
import DomainModel.AnalysisEntity.ResponseType;
import DomainModel.AnalysisEntity.Stimulus;
import DomainModel.AnalysisEntity.StimulusSource;
import DomainModel.AnalysisEntity.StimulusSourceType;
import DomainModel.AnalysisEntity.StimulusType;
import DomainModel.AnalysisEntity.Unit;
import Presentation.Controller;
import Presentation.preferenceAnalysis.NewQualityRequirementPreferencePage;
import Presentation.preferences.Messages;

/**
 * Controller for NewQualityRequirementPreferencePage
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 *
 */
public class NewQualityRequirementPPController extends Controller {
	/**
	 * Attributes
	 */
	private AnalysisManager manager;
	private NewQualityRequirementPreferencePage form;
	private static NewQualityRequirementPPController viewController;

	private NewQualityRequirementPPController() {
		super();
	}

	/**
	 * Getters and Setters
	 */
	public static NewQualityRequirementPPController getViewController() {
		if (viewController == null) {
			synchronized (NewQualityRequirementPPController.class) {
				viewController = new NewQualityRequirementPPController();
			}
		}
		return viewController;
	}
	
	public static void setViewController(NewQualityRequirementPPController viewController) {
		NewQualityRequirementPPController.viewController = viewController;
	}

	public AnalysisManager getManager() {
		return AnalysisManager.getManager();
	}

	public void setManager(AnalysisManager manager) {
		this.manager = manager;
	}

	public NewQualityRequirementPreferencePage getForm() {
		return form;
	}

	public void setForm(NewQualityRequirementPreferencePage form) {
		this.form = form;
	}
	
	/**
	 * Sets the model of specific combo
	 */

	public void setModelSystem() {
		this.getForm().getCmbSystem().setInput(getManager().getComboModelSystem());
	}

	public void setModelQualityAttribute() {
		this.getForm().getCmbQualityAttribute().setInput(getManager().getComboModelQualityAttribute());
	}

	/**
	 * Sets the model of stimulus source type combo for a specific quality
	 * attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelStimulusSourceTypes(QualityAttribute qualityAttribute) {
		this.getForm().getCmbTypeStimulusSource()
				.setInput(getManager().getComboModelStimulusSourceType(qualityAttribute));
	}

	/**
	 * Sets the model of stimulus type combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelStimulusTypes(QualityAttribute qualityAttribute) {
		this.getForm().getCmbTypeStimulus().setInput(getManager().getComboModelStimulusType(qualityAttribute));
	}

	/**
	 * Sets the model of environment type combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelEnvironmentTypes(QualityAttribute qualityAttribute) {
		this.getForm().getCmbTypeEnvironment().setInput(getManager().getComboModelEnvironmentType(qualityAttribute));
	}

	/**
	 * Sets the model of artifact type combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelArtifactTypes(QualityAttribute qualityAttribute) {
		this.getForm().getCmbTypeArtifact().setInput(getManager().getComboModelArtifactType(qualityAttribute));
	}

	/**
	 * Sets the model of response type combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelResponseTypes(QualityAttribute qualityAttribute) {
		this.getForm().getCmbTypeResponse().setInput(getManager().getComboModelResponseType(qualityAttribute));
	}

	/**
	 * Sets the model of response measure type combo for a specific quality
	 * attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelResponseMeasureTypes(QualityAttribute qualityAttribute) {
		this.getForm().getCmbTypeResponseMeasure()
				.setInput(getManager().getComboModelResponseMeasureType(qualityAttribute));
	}

	/**
	 * Sets the model of metric combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelMetric(ResponseMeasureType type) {
		this.getForm().getCmbMetric().setInput(getManager().getComboModelMetric(type));
	}

	/**
	 * Sets the model of unit combo for a specific quality attribute
	 * 
	 * @param qualityAttribute
	 */
	public void setModelUnit(Metric type) {
		this.getForm().getCmbUnit().setInput(getManager().getComboModelUnit(type));
	}

	/**
	 * Create the quality requirement and prepare the view
	 */
	public int save() {
		int err = this.newQualityRequirement();
		if (err == 0) {
			this.getForm().clearView();
			this.getForm().prepareView(0);
			if (this.getManager().saveQualityRequirement()){
				return 0;
			}else {
				return 1;
			}
		}else{
			return 3;
		}
	}

	/**
	 * Create a new quality requirement
	 * 
	 * @return int (indicates if the quality requirement was created
	 *         successfully)
	 */
	public int newQualityRequirement() {
		if (this.isValidData()) {
			StimulusSource stimulusSource = new StimulusSource(
					this.getForm().getTxtDescriptionStimulusSource().getText(),
					this.getForm().getTxtValueStimulusSource().getStringValue(),
					(StimulusSourceType) ((IStructuredSelection) this.getForm().getCmbTypeStimulusSource()
							.getSelection()).getFirstElement());
			Stimulus stimulus = new Stimulus(this.getForm().getTxtDescriptionStimulus().getText(),
					this.getForm().getTxtValueStimulus().getStringValue(),
					(StimulusType) ((IStructuredSelection) this.getForm().getCmbTypeStimulus().getSelection())
							.getFirstElement());
			Environment environment = new Environment(this.getForm().getTxtDescriptionEnvironment().getText(),
					this.getForm().getTxtValueEnvironment().getStringValue(),
					(EnvironmentType) ((IStructuredSelection) this.getForm().getCmbTypeEnvironment().getSelection())
							.getFirstElement());
			Artifact artifact = new Artifact(this.getForm().getTxtDescriptionArtifact().getText(),
					(ArtifactType) ((IStructuredSelection) this.getForm().getCmbTypeArtifact().getSelection())
							.getFirstElement());
			Response response = new Response(this.getForm().getTxtDescriptionResponse().getText(),
					this.getForm().getTxtValueResponse().getStringValue(),
					(ResponseType) ((IStructuredSelection) this.getForm().getCmbTypeResponse().getSelection())
							.getFirstElement());
			ResponseMeasure responseMeasure = new ResponseMeasure(
					this.getForm().getTxtDescriptionResponseMeasure().getText(),
					this.getForm().getTxtValueResponseMeasure().getDoubleValue(),
					(ResponseMeasureType) ((IStructuredSelection) this.getForm().getCmbTypeResponseMeasure()
							.getSelection()).getFirstElement(),
					(Metric) ((IStructuredSelection) this.getForm().getCmbMetric().getSelection()).getFirstElement(),
					(Unit) ((IStructuredSelection) this.getForm().getCmbUnit().getSelection()).getFirstElement());

			this.getManager()
					.newQualityRequirement(
							(DomainModel.AnalysisEntity.System) ((IStructuredSelection) this.getForm().getCmbSystem()
									.getSelection()).getFirstElement(),
							this.getForm().getTxtDescription().getText(), true,
							(QualityAttribute) ((IStructuredSelection) this.getForm().getCmbQualityAttribute()
									.getSelection()).getFirstElement(),
							stimulusSource, stimulus, artifact, environment, response, responseMeasure);
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Validate the necessary data for the creation of the quality requirement
	 * 
	 * @return boolean (is true if they have completed the required fields)
	 */
	private boolean isValidData() {
		Metric m = (Metric) ((IStructuredSelection) this.getForm().getCmbMetric().getSelection()).getFirstElement();
		if (this.isEmpty(this.getForm().getCmbSystem())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectSystem_ErrorDialog"));
			this.getForm().getCmbSystem().getCombo().setFocus();
			return false;
		}
		if (this.isEmpty(this.getForm().getTxtDescription())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyDescription_ErrorDialog"));
			this.getForm().getTxtDescription().setFocus();
			return false;
		}
		if (this.isEmpty(this.getForm().getCmbQualityAttribute())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectQualityAttribute_ErrorDialog"));
			this.getForm().getCmbQualityAttribute().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getForm().getTxtValueStimulusSource())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyStimulusSourceValue_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getForm().getTxtValueStimulus())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyStimulusValue_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getForm().getTxtValueEnvironment())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyEnvironmentValue_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getForm().getTxtValueResponse())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyResponseValue_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getForm().getCmbTypeStimulusSource())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectStimulusSourceType_ErrorDialog"));
			this.getForm().getCmbTypeStimulusSource().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getForm().getCmbTypeStimulus())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectStimulusType_ErrorDialog"));
			this.getForm().getCmbTypeStimulus().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getForm().getCmbTypeEnvironment())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectEnvironmentType_ErrorDialog"));
			this.getForm().getCmbTypeEnvironment().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getForm().getCmbTypeArtifact())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectArtifactType_ErrorDialog"));
			this.getForm().getCmbTypeArtifact().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getForm().getCmbTypeResponse())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectResponseType_ErrorDialog"));
			this.getForm().getCmbTypeResponse().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getForm().getCmbTypeResponseMeasure())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectResponseMeasureType_ErrorDialog"));
			this.getForm().getCmbTypeResponseMeasure().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getForm().getCmbMetric())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectMetric_ErrorDialog"));
			this.getForm().getCmbMetric().getCombo().setFocus();
			return false;
		} else if (this.isEmpty(this.getForm().getTxtValueResponseMeasure())) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_EmptyResponseMeasureValue_ErrorDialog"));
			return false;
		} else if (!this.getForm().getTxtValueResponseMeasure().isValid()) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_InvalidResponseMeasureValue_ErrorDialog"));
			return false;
		} else if (this.getForm().getTxtValueResponseMeasure().getDoubleValue() <= 0) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_InvalidResponseMeasureValueNegative_ErrorDialog"));
			return false;
		} else if (this.isEmpty(this.getForm().getCmbUnit()) && !m.getName().equals("Number of failures")) {
			this.createErrorDialog(Messages.getString("UCM2DEVS_SelectUnit_ErrorDialog"));
			this.getForm().getCmbUnit().getCombo().setFocus();
			return false;
		}

		return true;
	}
	
	public Boolean isConnection(){
		return this.getManager().isConnection();
	}
	
	public boolean existSystemTrue() {
		return this.getManager().existSystemTrue();
	}

}
