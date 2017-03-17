package DomainModel.AnalysisEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class defines the unity of a or more metric
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */

@Entity
@Table(name = "UNIT")
public class Unit implements Comparable { // NOPMD by Usuario-Pc on 11/06/16 12:33

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id; // NOPMD by Usuario-Pc on 11/06/16 12:43

	private String name; // NOPMD by Usuario-Pc on 11/06/16 12:43

	// Builders
	public Unit() {

	}

	public Unit(String pname) { // NOPMD by Usuario-Pc on 11/06/16 12:43
		super();
		this.name = pname;
	}

	// Getters and Setters
	public int getId() { // NOPMD by Usuario-Pc on 11/06/16 12:43
		return id;
	}

	public void setId(int pid) { // NOPMD by Usuario-Pc on 11/06/16 12:43
		this.id = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String pname) { // NOPMD by Usuario-Pc on 11/06/16 12:43
		this.name = pname;
	}

	// toString
	@Override
	public String toString() { // NOPMD by Usuario-Pc on 11/06/16 12:43
		return this.getName();
	}

	// compareTo
	public int compareTo(Object p) { // NOPMD by Usuario-Pc on 11/06/16 12:43
		Unit t = (Unit) p;
		return this.toString().compareTo(t.toString());
	}

}
