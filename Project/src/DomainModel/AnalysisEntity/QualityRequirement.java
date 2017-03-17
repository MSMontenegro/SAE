package DomainModel.AnalysisEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This class defines the requirement of quality which will be specified by
 * specific quality scenarios
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */

@Entity
@Table(name = "QUALITYREQUIREMENT")
public class QualityRequirement implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private boolean state;

	@OneToOne(targetEntity = SpecificScenario.class, cascade = CascadeType.ALL)
	private SpecificScenario qualityScenario;

	// Builders
	public QualityRequirement() {

	}

	public QualityRequirement(boolean pstate, SpecificScenario pqualityScenario) {
		super();
		this.state = pstate;
		this.qualityScenario = pqualityScenario;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int pid) {
		this.id = pid;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean pstate) {
		this.state = pstate;
	}

	public SpecificScenario getQualityScenario() {
		return qualityScenario;
	}

	public void setQualityScenario(SpecificScenario pqualityScenario) {
		this.qualityScenario = pqualityScenario;
	}

	// CompareTo
	public int compareTo(Object p) {
		QualityRequirement t = (QualityRequirement) p;
		return this.toString().compareTo(t.toString());
	}

	public void remove() {
		this.setState(false);
	}
}
