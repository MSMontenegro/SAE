package DomainModel.SoftwareArchitectureSpecificationEntity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * This class defines the path element: ANDJoin
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/09/2016
 */

@Entity
@Table(name = "ANDJOIN")
@PrimaryKeyJoinColumn(name = "id")
public class ANDJoin extends Join {

	public ANDJoin(String name) {
		super(name);
	}
	
	public ANDJoin() {
	}
}
