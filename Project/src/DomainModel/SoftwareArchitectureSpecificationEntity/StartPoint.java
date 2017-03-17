package DomainModel.SoftwareArchitectureSpecificationEntity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * This class defines the path element: StartPoint
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/09/2016
 */

@Entity
@Table(name = "STARTPOINT")
@PrimaryKeyJoinColumn(name = "id")
public class StartPoint extends PathElement {

	private double meanTimeBRequest;
	
	public StartPoint(String name, double meanTimeBRequest) {
		super(name);
		this.meanTimeBRequest = meanTimeBRequest;
	}
	
	public StartPoint() {
	}
}
