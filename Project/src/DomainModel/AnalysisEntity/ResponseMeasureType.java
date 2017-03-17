package DomainModel.AnalysisEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */
@Entity
@Table(name = "RESPONSEMEASURETYPE")
public class ResponseMeasureType implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private String name;

	@ManyToMany(targetEntity = Metric.class)
	private Set<Metric> metrics = new HashSet<Metric>();

	// Builders
	public ResponseMeasureType() {

	}

	public ResponseMeasureType(String pname, Set<Metric> pmetrics) {
		super();
		this.name = pname;
		this.metrics = pmetrics;
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

	public Set<Metric> getMetrics() {
		return metrics;
	}

	public void setMetrics(Set<Metric> pmetrics) {
		this.metrics = pmetrics;
	}

	// toString
	@Override
	public String toString() {
		return this.getName();
	}

	// CompareTo
	public int compareTo(Object p) {
		ResponseMeasureType t = (ResponseMeasureType) p;
		return this.toString().compareTo(t.toString());
	}

}
