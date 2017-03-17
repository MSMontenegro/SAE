package DomainModel.SoftwareArchitectureEvaluationEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class defines join path element
 *  
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 */
@Entity
@Table(name = "INDICATORTYPE")
public class IndicatorType implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private String name;

	public IndicatorType() {
		super();
	}

	public IndicatorType(String pname) {
		this.name = pname;
	}

	// CompareTo
	@Override
	public int compareTo(Object p) {
		IndicatorType t = (IndicatorType) p;
		return this.toString().compareTo(t.toString());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
