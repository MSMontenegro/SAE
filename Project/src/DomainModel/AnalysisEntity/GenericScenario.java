package DomainModel.AnalysisEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This class defines a generic scenario of quality
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */

@Entity
@Table(name = "GENERICSCENARIO")
public class GenericScenario implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	@OneToOne(targetEntity = QualityAttribute.class, cascade = CascadeType.ALL)
	private QualityAttribute qualityAttribute;

	@OneToMany(targetEntity = StimulusSourceType.class, cascade = CascadeType.ALL)
	private Set<StimulusSourceType> stimulusSourceTypes = new HashSet<StimulusSourceType>();

	@OneToMany(targetEntity = StimulusType.class, cascade = CascadeType.ALL)
	private Set<StimulusType> stimulusTypes = new HashSet<StimulusType>();

	@OneToMany(targetEntity = EnvironmentType.class, cascade = CascadeType.ALL)
	private Set<EnvironmentType> environmentTypes = new HashSet<EnvironmentType>();

	@OneToMany(targetEntity = ArtifactType.class, cascade = CascadeType.ALL)
	private Set<ArtifactType> artifactTypes = new HashSet<ArtifactType>();

	@OneToMany(targetEntity = ResponseType.class, cascade = CascadeType.ALL)
	private Set<ResponseType> responseTypes = new HashSet<ResponseType>();

	@OneToMany(targetEntity = ResponseMeasureType.class, cascade = CascadeType.ALL)
	private Set<ResponseMeasureType> responseMeasureTypes = new HashSet<ResponseMeasureType>();

	// Builders
	public GenericScenario() {

	}

	public GenericScenario(Set<StimulusSourceType> pstimulusSourceTypes, Set<StimulusType> pstimulusTypes,
			Set<EnvironmentType> penvironmentTypes, Set<ArtifactType> partifactTypes, Set<ResponseType> presponseTypes,
			Set<ResponseMeasureType> presponseMeasureTypes) {
		super();
		this.stimulusSourceTypes = pstimulusSourceTypes;
		this.stimulusTypes = pstimulusTypes;
		this.environmentTypes = penvironmentTypes;
		this.artifactTypes = partifactTypes;
		this.responseTypes = presponseTypes;
		this.responseMeasureTypes = presponseMeasureTypes;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int pid) {
		this.id = pid;
	}

	public QualityAttribute getQualityAttribute() {
		return qualityAttribute;
	}

	public void setQualityAttribute(QualityAttribute pqualityAttribute) {
		this.qualityAttribute = pqualityAttribute;
	}

	public Set<StimulusSourceType> getStimulusSourceTypes() {
		return stimulusSourceTypes;
	}

	public void setStimulusSourceTypes(Set<StimulusSourceType> pstimulusSourceTypes) {
		this.stimulusSourceTypes = pstimulusSourceTypes;
	}

	public Set<StimulusType> getStimulusTypes() {
		return stimulusTypes;
	}

	public void setStimulusTypes(Set<StimulusType> pstimulusTypes) {
		this.stimulusTypes = pstimulusTypes;
	}

	public Set<ArtifactType> getArtifactTypes() {
		return artifactTypes;
	}

	public void setArtifactTypes(Set<ArtifactType> partifactTypes) {
		this.artifactTypes = partifactTypes;
	}

	public Set<EnvironmentType> getEnvironmentTypes() {
		return environmentTypes;
	}

	public void setEnvironmentTypes(Set<EnvironmentType> penvironmentTypes) {
		this.environmentTypes = penvironmentTypes;
	}

	public Set<ResponseType> getResponseTypes() {
		return responseTypes;
	}

	public void setResponseTypes(Set<ResponseType> presponseTypes) {
		this.responseTypes = presponseTypes;
	}

	public Set<ResponseMeasureType> getResponseMeasureTypes() {
		return responseMeasureTypes;
	}

	public void setResponseMeasureTypes(Set<ResponseMeasureType> presponseMeasureTypes) {
		this.responseMeasureTypes = presponseMeasureTypes;
	}

	// CompareTo
	public int compareTo(Object p) {
		GenericScenario t = (GenericScenario) p;
		return this.toString().compareTo(t.toString());
	}
}
