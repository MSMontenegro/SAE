package BusinessLogic;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import DataManager.HibernateManager;
import DomainModel.AnalysisEntity.Artifact;
import DomainModel.AnalysisEntity.ArtifactType;
import DomainModel.AnalysisEntity.Environment;
import DomainModel.AnalysisEntity.EnvironmentType;
import DomainModel.AnalysisEntity.Metric;
import DomainModel.AnalysisEntity.QualityAttribute;
import DomainModel.AnalysisEntity.QualityRequirement;
import DomainModel.AnalysisEntity.Response;
import DomainModel.AnalysisEntity.ResponseMeasure;
import DomainModel.AnalysisEntity.ResponseMeasureType;
import DomainModel.AnalysisEntity.ResponseType;
import DomainModel.AnalysisEntity.SpecificScenario;
import DomainModel.AnalysisEntity.Stimulus;
import DomainModel.AnalysisEntity.StimulusSource;
import DomainModel.AnalysisEntity.StimulusSourceType;
import DomainModel.AnalysisEntity.StimulusType;
import DomainModel.AnalysisEntity.Unit;

/**
 * This class is responsible for the management package: Analysis
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 * @version: 06/11/2015
 */

public class AnalysisManager extends HibernateManager {

	/**
	 * Attributes
	 */
	private DomainModel.AnalysisEntity.System system;
	private QualityRequirement qualityRequirement;
	private static AnalysisManager manager;

	/**
	 * Builder
	 */
	private AnalysisManager() {
		super();
	}

	/**
	 * Get AnalysisManager. Applied Singleton pattern
	 */
	public static AnalysisManager getManager() {
		if (manager == null) {
			synchronized (AnalysisManager.class) {
				manager = new AnalysisManager();
			}
		}
		return manager;
	}

	public static void setManager(AnalysisManager manager) {
		AnalysisManager.manager = manager;
	}

	/**
	 * Getters and Setters
	 */

	public void setSystem(DomainModel.AnalysisEntity.System psystem) {
		this.system = psystem;
	}

	public DomainModel.AnalysisEntity.System getSystem() {
		return this.system;
	}

	public void setQualityRequirement(QualityRequirement pqualityRequirement) {
		this.qualityRequirement = pqualityRequirement;
	}

	public QualityRequirement getQualityRequirement() {
		return qualityRequirement;
	}

	/**
	 * 
	 * @return ComboBoxModel with qualityAttribute names
	 */
	public QualityAttribute[] getComboModelQualityAttribute() {
		ArrayList<QualityAttribute> qualityAttributes = new ArrayList<QualityAttribute>();
		for (QualityAttribute auxTipo : this.listQualityAttribute()) {
			qualityAttributes.add(auxTipo);
		}
		QualityAttribute[] arrayQualityAttribute = new QualityAttribute[qualityAttributes.size()];
		qualityAttributes.toArray(arrayQualityAttribute);
		return arrayQualityAttribute;
	}

	/**
	 * 
	 * @return ComboBoxModel with system names whose state==true
	 */
	public DomainModel.AnalysisEntity.System[] getComboModelSystem() { // NOPMD
																		// by
																		// Usuario-Pc
																		// on
																		// 10/06/16
																		// 21:42
		ArrayList<DomainModel.AnalysisEntity.System> systems = new ArrayList<DomainModel.AnalysisEntity.System>();
		for (DomainModel.AnalysisEntity.System auxTipo : this.listSystem()) {
			systems.add(auxTipo);
		}
		DomainModel.AnalysisEntity.System[] arraySystem = new DomainModel.AnalysisEntity.System[systems.size()];
		systems.toArray(arraySystem);
		return arraySystem;
	}

	/**
	 * 
	 * @return ComboBoxModel with system names whose state==true,
	 *         qualityRequirement!=empty and qualityRequirement.state==true
	 * 
	 */
	public DomainModel.AnalysisEntity.System[] getComboModelSystemWithRequirements() { // NOPMD
																						// by
																						// Usuario-Pc
																						// on
																						// 10/06/16
																						// 21:41
		ArrayList<DomainModel.AnalysisEntity.System> systems = new ArrayList<DomainModel.AnalysisEntity.System>();
		for (DomainModel.AnalysisEntity.System auxTipo : this.listSystem()) {
			if (auxTipo.getQualityRequirements().isEmpty() == false) {
				Iterator it = auxTipo.getQualityRequirements().iterator();
				boolean i = true;
				while (it.hasNext() && i) {
					QualityRequirement q = (QualityRequirement) it.next();
					if (q.isState()) {
						systems.add(auxTipo);
						i = false;
					}
				}
			}
		}
		DomainModel.AnalysisEntity.System[] arraySystem = new DomainModel.AnalysisEntity.System[systems.size()];
		systems.toArray(arraySystem);
		return arraySystem;
	}

	/**
	 * @param QualityAttribute
	 * @return ComboBoxModel with stimulusSourceType names for an specific
	 *         attribute of quality
	 * 
	 */
	public StimulusSourceType[] getComboModelStimulusSourceType(QualityAttribute qualityAttribute) {
		ArrayList<StimulusSourceType> stimulusSourceTypes = new ArrayList<StimulusSourceType>();
		Iterator it = qualityAttribute.getGenericScenario().getStimulusSourceTypes().iterator();
		while (it.hasNext()) {
			StimulusSourceType auxTipo = (StimulusSourceType) it.next();
			stimulusSourceTypes.add(auxTipo);
		}
		StimulusSourceType[] arrayStimulusSourceType = new StimulusSourceType[stimulusSourceTypes.size()];
		stimulusSourceTypes.toArray(arrayStimulusSourceType);
		return arrayStimulusSourceType;
	}

	/**
	 * @param QualityAttribute
	 * @return ComboBoxModel with stimulusType names for an specific attribute
	 *         of quality
	 * 
	 */
	public StimulusType[] getComboModelStimulusType(QualityAttribute qualityAttribute) {
		ArrayList<StimulusType> stimulusTypes = new ArrayList<StimulusType>();
		Iterator it = qualityAttribute.getGenericScenario().getStimulusTypes().iterator();
		while (it.hasNext()) {
			StimulusType auxTipo = (StimulusType) it.next();
			stimulusTypes.add(auxTipo);
		}
		StimulusType[] arrayStimulusType = new StimulusType[stimulusTypes.size()];
		stimulusTypes.toArray(arrayStimulusType);
		return arrayStimulusType;
	}

	/**
	 * @param QualityAttribute
	 * @return ComboBoxModel with environmentType names for an specific
	 *         attribute of quality
	 * 
	 */
	public EnvironmentType[] getComboModelEnvironmentType(QualityAttribute qualityAttribute) {
		ArrayList<EnvironmentType> environmentTypes = new ArrayList<EnvironmentType>();
		Iterator it = qualityAttribute.getGenericScenario().getEnvironmentTypes().iterator();
		while (it.hasNext()) {
			EnvironmentType auxTipo = (EnvironmentType) it.next();
			environmentTypes.add(auxTipo);
		}
		EnvironmentType[] arrayEnvironmentType = new EnvironmentType[environmentTypes.size()];
		environmentTypes.toArray(arrayEnvironmentType);
		return arrayEnvironmentType;
	}

	/**
	 * @param QualityAttribute
	 * @return ComboBoxModel with artifactType names for an specific attribute
	 *         of quality
	 * 
	 */
	public ArtifactType[] getComboModelArtifactType(QualityAttribute qualityAttribute) {
		ArrayList<ArtifactType> artifactTypes = new ArrayList<ArtifactType>();
		Iterator it = qualityAttribute.getGenericScenario().getArtifactTypes().iterator();
		while (it.hasNext()) {
			ArtifactType auxTipo = (ArtifactType) it.next();
			artifactTypes.add(auxTipo);
		}
		ArtifactType[] arrayArtifactType = new ArtifactType[artifactTypes.size()];
		artifactTypes.toArray(arrayArtifactType);
		return arrayArtifactType;
	}

	/**
	 * @param QualityAttribute
	 * @return ComboBoxModel with responseType names for an specific attribute
	 *         of quality
	 * 
	 */
	public ResponseType[] getComboModelResponseType(QualityAttribute qualityAttribute) {
		ArrayList<ResponseType> responseTypes = new ArrayList<ResponseType>();
		Iterator it = qualityAttribute.getGenericScenario().getResponseTypes().iterator();
		while (it.hasNext()) {
			ResponseType auxTipo = (ResponseType) it.next();
			responseTypes.add(auxTipo);
		}
		ResponseType[] arrayResponseType = new ResponseType[responseTypes.size()];
		responseTypes.toArray(arrayResponseType);
		return arrayResponseType;
	}

	/**
	 * @param QualityAttribute
	 * @return ComboBoxModel with responseMeasureType names for an specific
	 *         attribute of quality
	 * 
	 */
	public ResponseMeasureType[] getComboModelResponseMeasureType(QualityAttribute qualityAttribute) {
		ArrayList<ResponseMeasureType> responseMeasureTypes = new ArrayList<ResponseMeasureType>();
		Iterator it = qualityAttribute.getGenericScenario().getResponseMeasureTypes().iterator();
		while (it.hasNext()) {
			ResponseMeasureType auxTipo = (ResponseMeasureType) it.next();
			responseMeasureTypes.add(auxTipo);
		}
		ResponseMeasureType[] arrayResponseMeasureType = new ResponseMeasureType[responseMeasureTypes.size()];
		responseMeasureTypes.toArray(arrayResponseMeasureType);
		return arrayResponseMeasureType;
	}

	/**
	 * @param ResponseMeasureType
	 * @return ComboBoxModel with metric names for an specific response measure
	 *         type
	 * 
	 */
	public Metric[] getComboModelMetric(ResponseMeasureType type) {
		ArrayList<Metric> metrics = new ArrayList<Metric>();
		Iterator it = type.getMetrics().iterator();
		while (it.hasNext()) {
			Metric auxTipo = (Metric) it.next();
			metrics.add(auxTipo);
		}
		Metric[] arrayMetric = new Metric[metrics.size()];
		metrics.toArray(arrayMetric);
		return arrayMetric;
	}

	/**
	 * @param Metric
	 * @return ComboBoxModel with unit names for an specific metric
	 * 
	 */
	public Unit[] getComboModelUnit(Metric type) {
		ArrayList<Unit> units = new ArrayList<Unit>();
		Iterator it = type.getUnits().iterator();
		while (it.hasNext()) {
			Unit auxTipo = (Unit) it.next();
			units.add(auxTipo);
		}
		Unit[] arrayUnit = new Unit[units.size()];
		units.toArray(arrayUnit);
		return arrayUnit;
	}

	/**
	 * 
	 * @return List<QualityAttribute> with the names of the quality attributes
	 */
	private List<QualityAttribute> listQualityAttribute() {
		return this.listClass(QualityAttribute.class, "name");
	}

	/**
	 * 
	 * @return List<System> with the system names whose state==true
	 */
	private List<DomainModel.AnalysisEntity.System> listSystem() {
		return this.listClass(DomainModel.AnalysisEntity.System.class, "systemName", true);
	}

	/**
	 * 
	 * @return True if there are systems whose state==true, else return false
	 * 
	 */
	public boolean existSystemTrue() {
		if (listSystem().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @return True if there are systems whose state==true,
	 *         qualityRequirement!=empty and qualityRequirement.state==true,
	 *         else return false
	 * 
	 */
	public boolean existSystemTrueWithQualityRequirementTrue() { // NOPMD by
																	// Usuario-Pc
																	// on
																	// 10/06/16
																	// 21:44
		for (DomainModel.AnalysisEntity.System auxTipo : this.listSystem()) {
			if (auxTipo.getQualityRequirements().isEmpty() == false) {
				Iterator it = auxTipo.getQualityRequirements().iterator();
				while (it.hasNext()) {
					QualityRequirement q = (QualityRequirement) it.next();
					if (q.isState()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Sets the parameters of its attribute system
	 * 
	 * @param systemname
	 *            , projectName, projectStartDate, projectFinishDate, state
	 * 
	 */
	public void newSystem(String psystemname, String pprojectName, Date pprojectStartDate, Date pprojectFinishDate,
			Boolean pstate) {
		this.setSystem(new DomainModel.AnalysisEntity.System(psystemname, pprojectName, pprojectStartDate,
				pprojectFinishDate, pstate));
	}

	/**
	 * Creates the quality requirement and this is responsible for creating the
	 * specific scenario (Creator Pattern). The quality requirement is added to
	 * the system.
	 * 
	 * @param psystem
	 *            , pdescription, pqualityAttribute, pstimulusSource, pstimulus,
	 *            partifact, penvironment, presponse, presponseMeasure,
	 *            pcondition
	 * 
	 */
	public void newQualityRequirement(DomainModel.AnalysisEntity.System psystem, String pdescription, boolean pstate,
			QualityAttribute pqualityAttribute, StimulusSource pstimulusSource, Stimulus pstimulus, Artifact partifact,
			Environment penvironment, Response presponse, ResponseMeasure presponseMeasure) {
		psystem.getQualityRequirements().add(new QualityRequirement(pstate, new SpecificScenario(pdescription,
				pqualityAttribute, pstimulusSource, pstimulus, partifact, penvironment, presponse, presponseMeasure)));
		this.setSystem(psystem);
	}

	// Setters of system atribbutes (name, projectName, startDate, finishDate)
	public void setSystemName(String psystemName) {
		this.getSystem().setSystemName(psystemName);
	}

	public void setProjectName(String pprojectName) {
		this.getSystem().setProjectName(pprojectName);
	}

	public void setStartDate(Date pstartDate) {
		this.getSystem().setProjectStartDate(pstartDate);
	}

	public void setFinishDate(Date pfinishDate) {
		this.getSystem().setProjectFinishDate(pfinishDate);
	}

	// Getters of system attributes (name, projectName, startDate, finishDate)
	public String getSystemName() {
		return this.getSystem().getSystemName();
	}

	public String getProjectName() {
		return this.getSystem().getProjectName();
	}

	public Date getStartDate() {
		return this.getSystem().getProjectStartDate();
	}

	public Date getFinishDate() {
		return this.getSystem().getProjectFinishDate();
	}

	// Saves and updates
	public Boolean saveSystem(int popcABM) {
		switch (popcABM) {
		case 0:
			return this.saveSystem();
		case 1:
			return this.updateSystem();
		case 2:
			return this.saveSystem();
		}
		return null;
	}

	public Boolean saveQualityRequirement(int popcABM) {
		switch (popcABM) {
		case 0:
			return this.saveQualityRequirement();
		case 1:
			return this.updateQualityRequirement();
		case 2:
			return this.saveQualityRequirement();
		}
		return null;
	}

	public Boolean saveSystem() {
		return this.saveObject(this.getSystem());

	}

	public Boolean saveQualityRequirement() {
		return this.updateSystem();
	}

	public Boolean updateSystem() {
		return this.updateObject(this.getSystem());
	}

	public Boolean updateQualityRequirement() {
		return this.updateSystem();
	}

	public Boolean removeSystem() {
		this.getSystem().remove();
		return this.updateSystem();
	}

	public Boolean removeQualityRequirement() {
		this.getQualityRequirement().remove();
		return this.updateQualityRequirement();
	}

	public Set<QualityRequirement> getQualityRequirements() {
		return this.getSystem().getQualityRequirements();
	}

	// Getters of specificScenario atribbutes
	public String getDescriptionScenario() {
		return this.getQualityRequirement().getQualityScenario().getDescription();
	}

	public QualityAttribute getQualityAttribute() {
		return this.getQualityRequirement().getQualityScenario().getQualityAttribute();
	}

	public String getDescriptionStimulusSource() {
		return this.getQualityRequirement().getQualityScenario().getStimulusSource().getDescription();
	}

	public StimulusSourceType getTypeStimulusSource() {
		return this.getQualityRequirement().getQualityScenario().getStimulusSource().getType();
	}

	public String getValueStimulusSource() {
		return this.getQualityRequirement().getQualityScenario().getStimulusSource().getValue();
	}

	public String getDescriptionStimulus() {
		return this.getQualityRequirement().getQualityScenario().getStimulus().getDescription();
	}

	public StimulusType getTypeStimulus() {
		return this.getQualityRequirement().getQualityScenario().getStimulus().getType();
	}

	public String getValueStimulus() {
		return this.getQualityRequirement().getQualityScenario().getStimulus().getValue();
	}

	public String getDescriptionEnvironment() {
		return this.getQualityRequirement().getQualityScenario().getEnvironment().getDescription();
	}

	public EnvironmentType getTypeEnvironment() {
		return this.getQualityRequirement().getQualityScenario().getEnvironment().getType();
	}

	public String getValueEnvironment() {
		return this.getQualityRequirement().getQualityScenario().getEnvironment().getValue();
	}

	public String getDescriptionArtifact() {
		return this.getQualityRequirement().getQualityScenario().getArtifact().getDescription();
	}

	public ArtifactType getTypeArtifact() {
		return this.getQualityRequirement().getQualityScenario().getArtifact().getType();
	}

	public String getDescriptionResponse() {
		return this.getQualityRequirement().getQualityScenario().getResponse().getDescription();
	}

	public ResponseType getTypeResponse() {
		return this.getQualityRequirement().getQualityScenario().getResponse().getType();
	}

	public String getValueResponse() {
		return this.getQualityRequirement().getQualityScenario().getResponse().getValue();
	}

	public String getDescriptionResponseMeasure() {
		return this.getQualityRequirement().getQualityScenario().getResponseMeasure().getDescription();
	}

	public ResponseMeasureType getTypeResponseMeasure() {
		return this.getQualityRequirement().getQualityScenario().getResponseMeasure().getType();
	}

	public Metric getMetric() {
		return this.getQualityRequirement().getQualityScenario().getResponseMeasure().getMetric();
	}

	public String getValueResponseMeasure() {
		return Double.toString(this.getQualityRequirement().getQualityScenario().getResponseMeasure().getValue());
	}

	public Unit getUnit() {
		return this.getQualityRequirement().getQualityScenario().getResponseMeasure().getUnit();
	}

	public void setDescriptionScenario(String pdescription) {
		this.getQualityRequirement().getQualityScenario().setDescription(pdescription);
	}

	// Setters of specificScenario atribbutes (stimulusSource, stimulus,
	// Enviroment, Artifact, Response, ResponseMeasure)
	public void setStimulusSource(String pdescription, String pvalue, StimulusSourceType ptype) {
		this.getQualityRequirement().getQualityScenario().getStimulusSource().setDescription(pdescription);
		this.getQualityRequirement().getQualityScenario().getStimulusSource().setValue(pvalue);
		this.getQualityRequirement().getQualityScenario().getStimulusSource().setType(ptype);
	}

	public void setStimulus(String pdescription, String pvalue, StimulusType ptype) {
		this.getQualityRequirement().getQualityScenario().getStimulus().setDescription(pdescription);
		this.getQualityRequirement().getQualityScenario().getStimulus().setValue(pvalue);
		this.getQualityRequirement().getQualityScenario().getStimulus().setType(ptype);
	}

	public void setEnvironment(String pdescription, String pvalue, EnvironmentType ptype) {
		this.getQualityRequirement().getQualityScenario().getEnvironment().setDescription(pdescription);
		this.getQualityRequirement().getQualityScenario().getEnvironment().setValue(pvalue);
		this.getQualityRequirement().getQualityScenario().getEnvironment().setType(ptype);
	}

	public void setArtifact(String pdescription, ArtifactType ptype) {
		this.getQualityRequirement().getQualityScenario().getArtifact().setDescription(pdescription);
		this.getQualityRequirement().getQualityScenario().getArtifact().setType(ptype);
	}

	public void setResponse(String pdescription, String pvalue, ResponseType ptype) {
		this.getQualityRequirement().getQualityScenario().getResponse().setDescription(pdescription);
		this.getQualityRequirement().getQualityScenario().getResponse().setValue(pvalue);
		this.getQualityRequirement().getQualityScenario().getResponse().setType(ptype);
	}

	public void setResponseMeasure(String pdescription, double pvalue, ResponseMeasureType ptype, Metric pmetric,
			Unit punit) {
		this.getQualityRequirement().getQualityScenario().getResponseMeasure().setDescription(pdescription);
		this.getQualityRequirement().getQualityScenario().getResponseMeasure().setValue(pvalue);
		this.getQualityRequirement().getQualityScenario().getResponseMeasure().setType(ptype);
		this.getQualityRequirement().getQualityScenario().getResponseMeasure().setMetric(pmetric);
		this.getQualityRequirement().getQualityScenario().getResponseMeasure().setUnit(punit);

	}

}