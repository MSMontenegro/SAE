package DomainModel.AnalysisEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */
@Entity
@Table(name = "SPECIFICSCENARIO")
public class SpecificScenario implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private String description;

	@ManyToOne(targetEntity = QualityAttribute.class)
	private QualityAttribute qualityAttribute;

	@ManyToOne(targetEntity = StimulusSource.class, cascade = CascadeType.ALL)
	private StimulusSource stimulusSource;

	@ManyToOne(targetEntity = Stimulus.class, cascade = CascadeType.ALL)
	private Stimulus stimulus;

	@ManyToOne(targetEntity = Artifact.class, cascade = CascadeType.ALL)
	private Artifact artifact;

	@ManyToOne(targetEntity = Environment.class, cascade = CascadeType.ALL)
	private Environment enviroment;

	@ManyToOne(targetEntity = Response.class, cascade = CascadeType.ALL)
	private Response response;

	@ManyToOne(targetEntity = ResponseMeasure.class, cascade = CascadeType.ALL)
	private ResponseMeasure responseMeasure;

	// Builders
	public SpecificScenario() {

	}

	public SpecificScenario(String pdescription, QualityAttribute pqualityAttribute, StimulusSource pstimulusSource,
			Stimulus pstimulus, Artifact partifact, Environment penviroment, Response presponse,
			ResponseMeasure presponseMeasure) {
		super();
		this.description = pdescription;
		this.qualityAttribute = pqualityAttribute;
		this.stimulusSource = pstimulusSource;
		this.stimulus = pstimulus;
		this.artifact = partifact;
		this.enviroment = penviroment;
		this.response = presponse;
		this.responseMeasure = presponseMeasure;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int pid) {
		this.id = pid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String pdescription) {
		this.description = pdescription;
	}

	public QualityAttribute getQualityAttribute() {
		return qualityAttribute;
	}

	public void setQualityAttribute(QualityAttribute pqualityAttribute) {
		this.qualityAttribute = pqualityAttribute;
	}

	public StimulusSource getStimulusSource() {
		return stimulusSource;
	}

	public void setStimulusSource(StimulusSource pstimulusSource) {
		this.stimulusSource = pstimulusSource;
	}

	public Stimulus getStimulus() {
		return stimulus;
	}

	public void setStimulus(Stimulus pstimulus) {
		this.stimulus = pstimulus;
	}

	public Artifact getArtifact() {
		return artifact;
	}

	public void setArtifact(Artifact partifact) {
		this.artifact = partifact;
	}

	public Environment getEnvironment() {
		return enviroment;
	}

	public void setEnvironment(Environment penvironment) {
		this.enviroment = penvironment;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response presponse) {
		this.response = presponse;
	}

	public ResponseMeasure getResponseMeasure() {
		return responseMeasure;
	}

	public void setResponseMeasure(ResponseMeasure presponseMeasure) {
		this.responseMeasure = presponseMeasure;
	}

	// CompareTo
	public int compareTo(Object p) {
		SpecificScenario t = (SpecificScenario) p;
		return this.toString().compareTo(t.toString());
	}
}
