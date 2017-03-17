package DomainModel.AnalysisEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class defines the concrete response measure of a particular scenario of
 * quality
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */

@Entity
@Table(name = "RESPONSEMEASURE")
public class ResponseMeasure implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private String description;

	private Double value;

	private boolean indicator;

	@ManyToOne(targetEntity = ResponseMeasureType.class)
	private ResponseMeasureType type;

	@ManyToOne(targetEntity = Metric.class)
	private Metric metric;

	@ManyToOne(targetEntity = Unit.class)
	private Unit unit;

	// Builders
	public ResponseMeasure() {

	}

	public ResponseMeasure(String pdescription, Double pvalue, ResponseMeasureType ptype, Metric pmetric, Unit punit) {
		super();
		this.description = pdescription;
		this.value = pvalue;
		this.type = ptype;
		// this.indicator = indicator;
		this.metric = pmetric;
		this.unit = punit;
	}

	// Getters y Setters
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

	public Double getValue() {
		return value;
	}

	public void setValue(Double pvalue) {
		this.value = pvalue;
	}

	public boolean getIndicator() {
		return indicator;
	}

	public void setIndicator(boolean pindicator) {
		this.indicator = pindicator;
	}

	public ResponseMeasureType getType() {
		return type;
	}

	public void setType(ResponseMeasureType ptype) {
		this.type = ptype;
	}

	public Metric getMetric() {
		return metric;
	}

	public void setMetric(Metric pmetric) {
		this.metric = pmetric;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit punit) {
		this.unit = punit;
	}

	// CompareTo
	public int compareTo(Object p) {
		ResponseMeasure t = (ResponseMeasure) p;
		return this.toString().compareTo(t.toString());
	}
}
