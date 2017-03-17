package DomainModel.AnalysisEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class defines the concrete artifact of a particular scenario of quality
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */

@Entity
@Table(name = "ARTIFACT")
public class Artifact implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private String description;

	@ManyToOne(targetEntity = ArtifactType.class)
	private ArtifactType type;

	// Builder
	public Artifact() {

	}

	public Artifact(String pdescription, ArtifactType ptype) {
		super();
		this.description = pdescription;
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

	public ArtifactType getType() {
		return type;
	}

	public void setType(ArtifactType ptype) {
		this.type = ptype;
	}

	// CompareTo
	@Override
	public int compareTo(Object p) {
		Artifact t = (Artifact) p;
		return this.toString().compareTo(t.toString());
	}
}
