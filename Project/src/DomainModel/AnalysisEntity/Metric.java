package DomainModel.AnalysisEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * This class defines metrics
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */

@Entity
@Table(name = "METRIC")
public class Metric implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	protected int id;

	protected String name;

	@ManyToMany(targetEntity = Unit.class)
	protected Set<Unit> units = new HashSet<Unit>();

	// Builders
	public Metric() {

	}

	public Metric(String pname, Set<Unit> punits) {
		super();
		this.name = pname;
		this.units = punits;
	}

	public Metric(String pname) {
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

	public Set<Unit> getUnits() {
		return units;
	}

	public void setUnits(Set<Unit> punits) {
		this.units = punits;
	}

	// toString
	@Override
	public String toString() {
		return this.getName();
	}

	// CompareTo
	public int compareTo(Object p) {
		Metric t = (Metric) p;
		return this.toString().compareTo(t.toString());
	}
}
