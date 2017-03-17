package DomainModel.AnalysisEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class defines the concrete environment of a particular scenario of
 * quality
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */

@Entity
@Table(name = "ENVIRONMENT")
public class Environment implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private String description;

	private String value;

	@ManyToOne(targetEntity = EnvironmentType.class)
	private EnvironmentType type;

	// Builder
	public Environment() {

	}

	public Environment(String pdescription, String pvalue, EnvironmentType ptype) {
		super();
		this.description = pdescription;
		this.value = pvalue;
		this.type = ptype;
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

	public String getValue() {
		return value;
	}

	public void setValue(String pvalue) {
		this.value = pvalue;
	}

	public EnvironmentType getType() {
		return type;
	}

	public void setType(EnvironmentType ptype) {
		this.type = ptype;
	}

	// CompareTo
	public int compareTo(Object p) {
		Environment t = (Environment) p;
		return this.toString().compareTo(t.toString());
	}
}
