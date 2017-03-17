package DomainModel.AnalysisEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class defines the concrete stimulus source of a particular scenario of
 * quality
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */

@Entity
@Table(name = "STIMULUSSOURCE")
public class StimulusSource implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private String description;

	private String value;

	@ManyToOne(targetEntity = StimulusSourceType.class)
	private StimulusSourceType type;

	// Builders
	public StimulusSource() {

	}

	public StimulusSource(String pdescription, String pvalue, StimulusSourceType ptype) {
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

	public StimulusSourceType getType() {
		return type;
	}

	public void setType(StimulusSourceType ptype) {
		this.type = ptype;
	}

	public int compareTo(Object p) {
		StimulusSource t = (StimulusSource) p;
		return this.toString().compareTo(t.toString());
	}
}
