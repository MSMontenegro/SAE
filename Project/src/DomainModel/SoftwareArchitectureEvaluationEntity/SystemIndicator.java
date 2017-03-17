package DomainModel.SoftwareArchitectureEvaluationEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This class defines join path element
 *  
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 */
@Entity
@Table(name = "SYSTEMINDICATOR")
public class SystemIndicator extends IndicatorType {

	public SystemIndicator() {
		super();
	}

	public SystemIndicator(String pname) {
		super(pname);
	}

}
