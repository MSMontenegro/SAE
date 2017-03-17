package DomainModel.SoftwareArchitectureSpecificationEntity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/**
 * This class defines the path element: ANDFork
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/09/2016
 */

@Entity
@Table(name = "ANDFORK")
@PrimaryKeyJoinColumn(name = "id")
public class ANDFork extends Fork {

	public ANDFork(String name) {
		super(name);
	}
	
	public ANDFork() {
	}
}
