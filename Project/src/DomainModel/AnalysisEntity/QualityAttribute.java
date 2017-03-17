package DomainModel.AnalysisEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This class defines a attribute of quality (availability, reliability and
 * performance)
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */

@Entity
@Table(name = "QUALITYATTRIBUTE")
public class QualityAttribute implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private String name;
	
	@ManyToMany(targetEntity = Tactic.class)
	private Set<Tactic> tactics = new HashSet<Tactic>();

	@OneToOne(targetEntity = GenericScenario.class, cascade = CascadeType.ALL)
	private GenericScenario genericScenario;

	// Builders
	public QualityAttribute() {

	}

	public QualityAttribute(String pname, GenericScenario pgenericScenario, Set<Tactic> ptactics) {
		super();
		this.genericScenario = pgenericScenario;
		this.name = pname;
		this.tactics = ptactics;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int pid) {
		this.id = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String pname) {
		this.name = pname;
	}
	
	

	public Set<Tactic> getTactics() {
		return tactics;
	}

	public void setTactics(Set<Tactic> tactics) {
		this.tactics = tactics;
	}

	public GenericScenario getGenericScenario() {
		return genericScenario;
	}

	public void setGenericScenario(GenericScenario pgenericScenario) {
		this.genericScenario = pgenericScenario;
	}

	// toString
	@Override
	public String toString() {
		return this.getName();
	}

	// CompareTo
	public int compareTo(Object p) {
		QualityAttribute t = (QualityAttribute) p;
		return this.toString().compareTo(t.toString());
	}

	public void setAttributeGenericScenario() {
		this.getGenericScenario().setQualityAttribute(this);
	}

}
