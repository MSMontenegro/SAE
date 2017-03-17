package DomainModel.AnalysisEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class defines the generic response of a generic scenario of quality
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */

@Entity
@Table(name = "RESPONSETYPE")
public class ResponseType implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private String name;

	// Builders
	public ResponseType() {

	}

	public ResponseType(String pname) {
		super();
		this.name = pname;
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

	// toString
	@Override
	public String toString() {
		return this.getName();
	}

	// CompareTo
	public int compareTo(Object p) {
		ResponseType t = (ResponseType) p;
		return this.toString().compareTo(t.toString());
	}

}
