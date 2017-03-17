package DomainModel.SoftwareArchitectureSpecificationEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This class defines the paths for an specific architecture view
 *  
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/09/2016
 */

@Entity
@Table(name = "PATH")
public class Path {
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	@OneToMany(targetEntity = PathElement.class)
	private Set<PathElement> pathElements = new HashSet<PathElement>();

	public Set<PathElement> getPathElements() {
		return pathElements;
	}

	public void setPathElements(Set<PathElement> pathElements) {
		this.pathElements = pathElements;
	}

}
