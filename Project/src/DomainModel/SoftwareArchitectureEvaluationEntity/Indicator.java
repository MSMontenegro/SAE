package DomainModel.SoftwareArchitectureEvaluationEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import DomainModel.AnalysisEntity.Metric;
import DomainModel.AnalysisEntity.Unit;

/**
 * This class defines join path element
 *  
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 */
@Entity
@Table(name = "INDICATOR")
public class Indicator implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private Double value;

	@ManyToOne(targetEntity = IndicatorType.class)
	private IndicatorType type;

	@ManyToOne(targetEntity = Metric.class)
	private Metric metric;
	
	@ManyToOne(targetEntity = Unit.class)
	private Unit unit;
	
	// CompareTo
	@Override
	public int compareTo(Object p) {
		Indicator t = (Indicator) p;
		return this.toString().compareTo(t.toString());
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public IndicatorType getType() {
		return type;
	}

	public void setType(IndicatorType type) {
		this.type = type;
	}

	public Metric getMetric() {
		return metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

}
