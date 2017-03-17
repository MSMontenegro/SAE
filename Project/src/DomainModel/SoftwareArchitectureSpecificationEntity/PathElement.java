package DomainModel.SoftwareArchitectureSpecificationEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

/**
 * This class defines the pats elements
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/09/2016
 */

@Entity
@Inheritance(strategy = javax.persistence.InheritanceType.TABLE_PER_CLASS)
public class PathElement {
	
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	protected int id;
	
	protected String name;

	public PathElement(String name) {
		super();
		this.name = name;
	}
	
	public PathElement() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isResponsibility(){
		if (this.getClass()== Responsibility.class){
			return true;
		}else{
			return false;
		}
	}
	
}
